package com.eventostec.api.adapters.output.repositories;

import com.eventostec.api.adapters.output.entities.JpaEventEntity;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventAddressProjection;
import com.eventostec.api.domain.event.EventRepository;
import com.eventostec.api.utils.mappers.EventMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class EventRepositoryImpl implements EventRepository {

    private final JpaEventRepository jpaEventRepository;
    private final EventMapper eventMapper;

    public EventRepositoryImpl(JpaEventRepository jpaEventRepository, EventMapper eventMapper) {
        this.jpaEventRepository = jpaEventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public Event save(Event event) {
        JpaEventEntity eventEntity = new JpaEventEntity(event);
        jpaEventRepository.save(eventEntity);
        return new Event(
                eventEntity.getId(),
                eventEntity.getTitle(),
                eventEntity.getDescription(),
                eventEntity.getImgUrl(),
                eventEntity.getEventUrl(),
                eventEntity.getRemote(),
                eventEntity.getDate()
        );
    }

    @Override
    public Optional<Event> findById(UUID id) {
        Optional<JpaEventEntity> eventEntity = this.jpaEventRepository.findById(id);
        return eventEntity.map(eventMapper::jpaToDomain);
    }

    @Override
    public List<Event> findAll() {
        return jpaEventRepository.findAll().stream()
                .map(entity -> new Event(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getDescription(),
                        entity.getImgUrl(),
                        entity.getEventUrl(),
                        entity.getRemote(),
                        entity.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaEventRepository.deleteById(id);
    }

    @Override
    public Page<EventAddressProjection> findUpcomingEvents(LocalDate currentDate, Pageable pageable) {
        return this.jpaEventRepository.findUpcomingEvents(currentDate, pageable);
    }

    @Override
    public Page<EventAddressProjection> findFilteredEvents(String city, String uf, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return this.jpaEventRepository.findFilteredEvents(city, uf, startDate, endDate, pageable);
    }

    @Override
    public List<EventAddressProjection> findEventsByTitle(String title) {
        return this.jpaEventRepository.findEventsByTitle(title);
    }
}
