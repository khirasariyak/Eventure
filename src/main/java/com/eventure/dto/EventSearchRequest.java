package com.eventure.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventSearchRequest {
    private String keyword;
    private String city;
    private Double lat;
    private Double lon;
    private String distance;
    private LocalDateTime fromDate;
}
