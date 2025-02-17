package com.eventostec.api.adapters;

import com.eventostec.api.application.service.EventServiceImpl;
import com.eventostec.api.application.usecases.EventUseCases;
import com.eventostec.api.domain.event.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class EventServiceAdapter implements EventUseCases {

    private final EventServiceImpl eventService;

    @Value("${admin.key}")
    private String adminKey;

    @Autowired
    public EventServiceAdapter(EventServiceImpl eventService) {
        this.eventService = eventService;
    }

    @Override
    public Event createEvent(EventRequestDTO data) {
        return eventService.createEvent(data);
    }

    @Override
    public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
        return eventService.getUpcomingEvents(page, size);
    }

    @Override
    public EventDetailsDTO getEventDetails(UUID eventId) {
        return eventService.getEventDetails(eventId);
    }

    @Override
    public void deleteEvent(UUID eventId, String adminKey) {
        eventService.deleteEvent(eventId, adminKey);
    }

    @Override
    public List<EventResponseDTO> searchEvents(String title) {
        return eventService.searchEvents(title);
    }

    @Override
    public List<EventResponseDTO> getFilteredEvents(int page, int size, String city, String uf, LocalDate startDate, LocalDate endDate) {
        return eventService.getFilteredEvents(page, size, city, uf, startDate, endDate);
    }
}

