package com.eventure.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.time.LocalDateTime;
import java.util.List;

@Document(indexName = "events")
@Data
public class Event {

    @Id
    private String id;
    private String title;
    private String description;
    @GeoPointField
    private GeoPoint location;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Field(type = FieldType.Date, format = DateFormat.strict_date_hour_minute_second)
    private LocalDateTime date;
    private List<String> tags;
    private String city;
    private String organizer;
}
