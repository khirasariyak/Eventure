package com.eventure.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventSearchRequest {
    private String keyword;
    private List<String> tags;
    private String city;
    private Double lat;
    private Double lon;
    private String distance;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
