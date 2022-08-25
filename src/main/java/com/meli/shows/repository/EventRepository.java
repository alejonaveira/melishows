package com.meli.shows.repository;

import com.meli.shows.model.Event;
import com.meli.shows.model.EventCategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT DISTINCT e.*" +
            "FROM " +
            "events e " +
            "INNER JOIN shows s ON " +
            "s.event_id = e.id " +
            "INNER JOIN sections s2 ON " +
            "s2.show_id = s.id " +
            "WHERE " +
            "(:name is null or e.name LIKE :name) " +
            "AND (:category is null or e.category = :category) " +
            "AND ((:fromDate is null OR :toDate is null) or s.`DATE` BETWEEN :fromDate AND :toDate) " +
            "AND ((:min is null OR :max is null) or s2.price BETWEEN :min AND :max)",
            nativeQuery = true)
    List<Event> findByNameCategoryDatePriceNative(String name, String category, Date fromDate, Date toDate, Double min, Double max);




    @Query("select distinct e from Event e " +
            "where (:name is null or upper(e.name) like upper(:name)) " +
            "and (e.category in :categories) "
    )
    List<Event> findByNameCategories(@Param("name") String name, @Param("categories") Collection<EventCategory> categories, Sort sort);

    @Query("select distinct e from Event e " +
            "inner join e.shows s " +
            "where (:name is null or upper(e.name) like upper(:name)) " +
            "and (:category is null or e.category = :category) " +
            "and (:dateStart is null or s.date >= :dateStart) " +
            "and (:dateEnd is null or s.date <= :dateEnd) "
    )
    List<Event> findByNameCategoryDate(@Param("name") String name, @Param("category") EventCategory category, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd, Sort sort);

    @Query("select distinct e from Event e inner join e.shows shows where shows.date >= ?1")
    List<Event> findByShows_DateGreaterThanEqual(Date date);


    @Query("select distinct e from Event e " +
            "join e.shows s " +
            "join s.sections sec " +
            "where (:name is null or upper(e.name) like upper(:name)) " +
            "and (e.category in :categories) " +
            "and (:dateStart is null or s.date >= :dateStart) " +
            "and (:dateEnd is null or s.date <= :dateEnd) " +
            "and (:priceStart is null or sec.price >= :priceStart) " +
            "and (:priceEnd is null or sec.price <= :priceEnd) "
    )
    List<Event> findByNameCategoryDatePrice(@Param("name") String name, @Param("categories") Collection<EventCategory> categories, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd, @Param("priceStart") Double priceStart, @Param("priceEnd") Double priceEnd, Sort sort);

}