package com.eventostec.api.domain.event;

import java.util.List;
import java.util.UUID;

public interface EventRepository {

    Event save(Event event);

    Event findById(UUID id);

    List<Event> findAll();

    void deleteById(UUID id);
}
