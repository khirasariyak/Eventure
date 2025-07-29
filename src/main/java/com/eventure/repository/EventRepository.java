package com.eventure.repository;

import com.eventure.model.Event;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EventRepository extends ElasticsearchRepository<Event, String> {
    // You can add custom query methods later
}
