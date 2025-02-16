package com.eventostec.api.adapters.output.repositories;

import com.eventostec.api.adapters.output.entities.JpaEventEntity;
import com.eventostec.api.domain.event.EventAddressProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface JpaEventRepository extends JpaRepository<JpaEventEntity, UUID> {

    @Query("SELECT e.id AS id, e.title AS title, e.description AS description, e.date AS date, e.imgUrl AS imgUrl, e.eventUrl AS eventUrl, e.remote AS remote, a.city AS city, a.uf AS uf " +
            "FROM JpaEventEntity e LEFT JOIN Address a ON e.id = a.event.id " +
            "WHERE e.date >= :currentDate")
    Page<EventAddressProjection> findUpcomingEvents(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    @Query("SELECT e.id AS id, e.title AS title, e.description AS description, e.date AS date, e.imgUrl AS imgUrl, e.eventUrl AS eventUrl, e.remote AS remote, a.city AS city, a.uf AS uf " +
            "FROM Event e JOIN Address a ON e.id = a.event.id " +
            "WHERE (:city = '' OR a.city LIKE %:city%) " +
            "AND (:uf = '' OR a.uf LIKE %:uf%) " +
            "AND (e.date >= :startDate AND e.date <= :endDate)")
    Page<EventAddressProjection> findFilteredEvents(@Param("city") String city,
                                                    @Param("uf") String uf,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate,
                                                    Pageable pageable);


    @Query("SELECT e.id AS id, e.title AS title, e.description AS description, e.date AS date, " +
            "e.imgUrl AS imgUrl, e.eventUrl AS eventUrl, e.remote AS remote, " +
            "a.city AS city, a.uf AS uf " +
            "FROM JpaEventEntity e JOIN JpaAddressEntity a ON e.id = a.event.id " +
            "WHERE (:title IS NULL OR :title = '' OR e.title LIKE %:title%)")
    List<EventAddressProjection> findEventsByTitle(@Param("title") String title);
}

