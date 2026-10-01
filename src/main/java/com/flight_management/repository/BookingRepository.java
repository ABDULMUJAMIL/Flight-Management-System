
package com.flight_management.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flight_management.dto.BookingStatus;
import com.flight_management.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer>
{

	// Get all bookings of a Flight
	@Query("select b from Booking b where b.flight.flightId=?1")
	List<Booking> findBookingByFlightId(int flightId);

	// Get bookings by Booking Date
	@Query("select b from Booking b where b.bookingDateTime between ?1 and ?2")
	List<Booking> findBookingByDate(LocalDateTime start, LocalDateTime end);

	// Get bookings by Status
	@Query("select b from Booking b where b.status=?1")
	List<Booking> findBookingByStatus(BookingStatus status);

	// Get booking by Payment Id
	@Query("select b from Booking b where b.payment.paymentId=?1")
	Optional<Booking> findBookingByPaymentId(int paymentId);

	// Get booking history of a Passenger
	@Query("select p.booking from Passenger p where p.passengerId=?1")
	List<Booking> findBookingHistoryOfPassenger(int passengerId);

}