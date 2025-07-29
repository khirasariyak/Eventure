package com.eventure.rest;

import com.eventure.dto.EventSearchRequest;
import com.eventure.model.Event;
import com.eventure.service.EventSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/search")
public class EventSearchController {

    @Autowired
    private EventSearchService eventSearchService;

    @PostMapping
    public List<Event> search(@RequestBody EventSearchRequest request) {
        return eventSearchService.search(request);
    }
}
