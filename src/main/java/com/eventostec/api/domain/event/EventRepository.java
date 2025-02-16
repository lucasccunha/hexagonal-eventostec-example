package com.eventostec.api.domain.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository {

    Event save(Event event);

    Optional<Event> findById(UUID id);

    List<Event> findAll();

    void deleteById(UUID id);

    Page<EventAddressProjection> findUpcomingEvents(LocalDate currentDate, Pageable pageable);

    Page<EventAddressProjection> findFilteredEvents(String city, String uf, LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<EventAddressProjection> findEventsByTitle(String title);
}
