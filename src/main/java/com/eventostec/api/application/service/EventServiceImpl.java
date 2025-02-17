package com.eventostec.api.application.service;

import com.eventostec.api.adapters.output.storage.ImageUploaderPort;
import com.eventostec.api.application.usecases.EventUseCases;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.event.*;
import com.eventostec.api.utils.mappers.EventMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventUseCases {

    @Value("${admin.key}")
    private String adminKey;

    private final AddressService addressService;
    private final CouponService couponService;
    private final EventRepository repository;
    private final ImageUploaderPort imageUploaderPort;
    private final EventMapper mapper;


    public EventServiceImpl(AddressService addressService, CouponService couponService, EventRepository repository, ImageUploaderPort imageUploaderPort, EventMapper mapper) {
        this.addressService = addressService;
        this.couponService = couponService;
        this.repository = repository;
        this.imageUploaderPort = imageUploaderPort;
        this.mapper = mapper;
    }

    @Override
    public Event createEvent(EventRequestDTO data) {
        String imgUrl = "";
        if (data.image() != null) {
            imgUrl = imageUploaderPort.uploadImage(data.image());
        }
        Event newEvent = mapper.dtoToEntity(data, imgUrl);
        repository.save(newEvent);
        if (Boolean.FALSE.equals(data.remote())) {
            this.addressService.createAddress(data, newEvent);
        }
        return newEvent;
    }

    @Override
    public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDate currentDate = LocalDate.now();
        Page<EventAddressProjection> eventsPage = this.repository.findUpcomingEvents(currentDate, pageable);
        return eventsPage.map(event -> new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getCity() != null ? event.getCity() : "",
                event.getUf() != null ? event.getUf() : "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImgUrl())).getContent();
    }

    @Override
    public EventDetailsDTO getEventDetails(UUID eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        Optional<Address> address = addressService.findByEventId(eventId);
        List<Coupon> coupons = couponService.consultCoupons(eventId, new Date());
        return mapper.domainToDetaisDto(event, address, coupons);
    }

    @Override
    public void deleteEvent(UUID eventId, String adminKey) {
        if (adminKey == null || !adminKey.equals(this.adminKey)) {
            throw new IllegalArgumentException("Invalid admin key");
        }
        this.repository.deleteById(eventId);
    }

    @Override
    public List<EventResponseDTO> searchEvents(String title) {
        title = (title != null) ? title : "";
        List<EventAddressProjection> eventsList = this.repository.findEventsByTitle(title);
        return eventsList.stream()
                .map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getCity() != null ? event.getCity() : "",
                        event.getUf() != null ? event.getUf() : "",
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())).toList();
    }

    @Override
    public List<EventResponseDTO> getFilteredEvents(int page, int size, String city, String uf, LocalDate startDate, LocalDate endDate) {
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        startDate = (startDate != null) ? startDate : LocalDate.ofEpochDay(0);
        endDate = (endDate != null) ? endDate : LocalDate.now();
        Pageable pageable = PageRequest.of(page, size);
        Page<EventAddressProjection> eventsPage = this.repository.findFilteredEvents(city, uf, startDate, endDate, pageable);
        return eventsPage.map(event -> new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getCity() != null ? event.getCity() : "",
                event.getUf() != null ? event.getUf() : "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImgUrl())).getContent();
    }
}
