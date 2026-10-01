package com.flight_management.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flight_management.entity.Flight;

public interface FlightRepository extends JpaRepository<Flight, Integer>
{
    @Query("select f from Flight f where f.airline=?1")
    List<Flight> findFlightByAirline(@Param("airline") String airline);

    @Query("select f from Flight f where f.source=?1 and f.destination=?2")
    List<Flight> findFlightBySourceAndDestination(String source, String destination);

    @Query("select f from Flight f where DATE(f.departureDateTime)=?1")
    List<Flight> findFlightByDepartureDate(LocalDate departureDate);

    @Query("select f from Flight f where f.source=?1 and f.destination=?2 and DATE(f.departureDateTime)=?3")
    List<Flight> findFlightBySourceDestinationAndDate(String source,
                                                      String destination,
                                                      LocalDate departureDate);

    @Query("select f from Flight f where f.price between ?1 and ?2")
    List<Flight> findFlightsWithinPriceRange(double minPrice,
                                             double maxPrice);

    @Query("select f from Flight f where f.source=?1 and f.destination=?2 order by f.price asc")
    List<Flight> findCheapestFlightBetweenCities(String source,
                                                 String destination);

    @Query("select f from Flight f where f.availableSeats>=?1")
    List<Flight> findFlightHavingMoreThanAvailableSeats(int seats);

    Page<Flight> findAll(Pageable pageable);

}