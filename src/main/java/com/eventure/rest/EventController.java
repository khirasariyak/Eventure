package com.eventure.rest;

import com.eventure.model.Event;
import com.eventure.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @PostMapping("/_bulk")
    public List<Event> createEvents(@RequestBody List<Event> events) {
        List<Event> result = new ArrayList<>();
        for (Event event : events) {
            result.add(createEvent(event));
        }
        return result;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return (List<Event>) eventRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable String id) {
        eventRepository.deleteById(id);
    }
}
