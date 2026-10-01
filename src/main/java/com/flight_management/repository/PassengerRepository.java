package com.flight_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flight_management.dto.Gender;
import com.flight_management.entity.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Integer>
{
	@Query("select p from Passenger p where p.contact=?1")
	Optional<Passenger> findPassengerByContact(long contact);

	@Query("select p from Passenger p where p.booking.bookingId=?1")
	List<Passenger> findPassengerByBookingId(int bookingId);

	@Query("select p from Passenger p where p.gender=?1")
	List<Passenger> findPassengerByGender(Gender gender);

	@Query("select p from Passenger p where p.age between ?1 and ?2")
	List<Passenger> findPassengerByAgeRange(int minAge, int maxAge);

	@Query("select p from Passenger p where p.booking.flight.flightId=?1")
	List<Passenger> findPassengerByFlight(int flightId);

	Page<Passenger> findAll(Pageable pageable);
	
	@Query("select p from Passenger p where p.booking.flight.flightId=?1 and p.seatNumber=?2")
	Optional<Passenger> findPassengerByFlightAndSeatNumber(int flightId,int seatNumber);
}