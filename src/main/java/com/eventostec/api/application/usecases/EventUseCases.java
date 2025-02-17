package com.eventostec.api.application.usecases;

import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventDetailsDTO;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EventUseCases {
    Event createEvent(EventRequestDTO data);

    List<EventResponseDTO> getUpcomingEvents(int page, int size);

    EventDetailsDTO getEventDetails(UUID eventId);

    void deleteEvent(UUID eventId, String adminKey);

    List<EventResponseDTO> searchEvents(String title);

    List<EventResponseDTO> getFilteredEvents(int page, int size, String city, String uf, LocalDate startDate, LocalDate endDate);
}
