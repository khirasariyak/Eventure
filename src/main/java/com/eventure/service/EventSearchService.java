package com.eventure.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.DateRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.eventure.dto.EventSearchRequest;
import com.eventure.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventSearchService {

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    public List<Event> search(EventSearchRequest request) {
        // 1. Build base bool query
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        // Keyword match on title/description
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            MatchQuery titleMatch = MatchQuery.of(m -> m
                    .field("title")
                    .query(request.getKeyword())
            );
            MatchQuery descMatch = MatchQuery.of(m -> m
                    .field("description")
                    .query(request.getKeyword())
            );

            boolQuery.should(Query.of(q -> q.match(titleMatch)))
                    .should(Query.of(q -> q.match(descMatch)))
                    .minimumShouldMatch("1");
        }

        // tags match
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            boolQuery.must(Query.of(q -> q.terms(t -> t
                    .field("tags.keyword")
                    .terms(ts -> ts.value(request.getTags().stream().map(FieldValue::of).toList()))
            )));
        }

        // City match
        if (request.getCity() != null && !request.getCity().isEmpty()) {
            TermQuery cityQuery = TermQuery.of(m -> m
                    .field("city.keyword") // use .keyword for exact match
                    .value(request.getCity())
            );
            boolQuery.must(Query.of(q -> q.term(cityQuery)));
        }

        // Date range
        if (request.getFromDate() != null || request.getToDate() != null) {
            DateRangeQuery.Builder dateRange = new DateRangeQuery.Builder().field("date");
            if (request.getFromDate() != null) {
                dateRange.gte(request.getFromDate().toString());
            }
            if (request.getToDate() != null) {
                dateRange.lte(request.getToDate().toString());
            }
            RangeQuery rangeQuery = QueryBuilders.range().date(dateRange.build()).build();
            boolQuery.must(Query.of(q -> q.range(rangeQuery)));
        }

        // Geo distance
        if (request.getLat() != null && request.getLon() != null) {
            GeoDistanceQuery geoQuery = GeoDistanceQuery.of(g -> g
                    .field("location")
                    .distance(request.getDistance())
                    .location(l -> l
                            .latlon(ll -> ll
                                    .lat(request.getLat())
                                    .lon(request.getLon())
                            )
                    )
            );
            boolQuery.filter(Query.of(q -> q.geoDistance(geoQuery)));
        }

        // 2. Build final query
        Query finalQuery = Query.of(q -> q.bool(boolQuery.build()));

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(finalQuery)
                .build();

        // 3. Search
        SearchHits<Event> hits = elasticsearchOperations.search(nativeQuery, Event.class);

        return hits.get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}