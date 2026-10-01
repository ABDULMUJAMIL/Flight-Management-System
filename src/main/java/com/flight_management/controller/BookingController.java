package com.flight_management.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flight_management.dto.BookingDTO;
import com.flight_management.dto.BookingStatus;
import com.flight_management.dto.PassengerDTO;
import com.flight_management.dto.PaymentDTO;
import com.flight_management.entity.Booking;
import com.flight_management.dto.ResponseStructure;
import com.flight_management.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController
{

	@Autowired
	private BookingService bookingService;

	//====================== ADD BOOKING ======================

	@PostMapping("/add")
	public ResponseEntity<ResponseStructure<BookingDTO>> createBooking(
			@RequestBody Booking booking)
	{
		return new ResponseEntity<>(
				bookingService.createBookingInDatabase(booking),
				HttpStatus.CREATED);
	}

	//====================== GET ALL BOOKINGS ======================

	@GetMapping("/")
	public ResponseEntity<ResponseStructure<List<BookingDTO>>> getAllBookings()
	{
		return new ResponseEntity<>(
				bookingService.getAllBookingsFromDatabase(),
				HttpStatus.FOUND);
	}

	//====================== GET BOOKING BY ID ======================

	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<BookingDTO>> getBookingById(
			@PathVariable("id") int bookingId)
	{
		return new ResponseEntity<>(
				bookingService.getBookingByIdFromDatabase(bookingId),
				HttpStatus.FOUND);
	}

	//====================== GET BOOKING BY FLIGHT ======================

	@GetMapping("/flight/{flightId}")
	public ResponseEntity<ResponseStructure<List<BookingDTO>>> getBookingByFlight(
			@PathVariable int flightId)
	{
		return new ResponseEntity<>(
				bookingService.getBookingByFlightFromDatabase(flightId),
				HttpStatus.FOUND);
	}

	//====================== GET BOOKING BY DATE ======================

	@GetMapping("/date/{bookingDate}")
	public ResponseEntity<ResponseStructure<List<BookingDTO>>> getBookingByDate(
			@PathVariable LocalDate bookingDate)
	{
		return new ResponseEntity<>(
				bookingService.getBookingByDateFromDatabase(bookingDate),
				HttpStatus.FOUND);
	}

	//====================== GET BOOKING BY STATUS ======================

	@GetMapping("/status/{status}")
	public ResponseEntity<ResponseStructure<List<BookingDTO>>> getBookingByStatus(
			@PathVariable BookingStatus status)
	{
		return new ResponseEntity<>(
				bookingService.getBookingByStatusFromDatabase(status),
				HttpStatus.FOUND);
	}

	//====================== GET PASSENGERS OF BOOKING ======================

	@GetMapping("/passengers/{bookingId}")
	public ResponseEntity<ResponseStructure<List<PassengerDTO>>> getPassengersOfBooking(
			@PathVariable int bookingId)
	{
		return new ResponseEntity<>(
				bookingService.getPassengersOfBookingFromDatabase(bookingId),
				HttpStatus.FOUND);
	}

	//====================== GET PAYMENT OF BOOKING ======================

	@GetMapping("/payment/{bookingId}")
	public ResponseEntity<ResponseStructure<PaymentDTO>> getPaymentOfBooking(
			@PathVariable int bookingId)
	{
		return new ResponseEntity<>(
				bookingService.getPaymentOfBookingFromDatabase(bookingId),
				HttpStatus.FOUND);
	}

	//====================== UPDATE BOOKING STATUS ======================

	@PatchMapping("/updateStatus")
	public ResponseEntity<ResponseStructure<BookingDTO>> updateBookingStatus(
			@RequestBody Booking booking)
	{
		return new ResponseEntity<>(
				bookingService.updateBookingStatusInDatabase(booking),
				HttpStatus.OK);
	}

	//====================== DELETE (CANCEL) BOOKING ======================

	@DeleteMapping("/delete/{bookingId}")
	public ResponseEntity<ResponseStructure<BookingDTO>> deleteBooking(
			@PathVariable int bookingId)
	{
		return new ResponseEntity<>(
				bookingService.deleteBookingFromDatabase(bookingId),
				HttpStatus.OK);
	}

	//====================== BOOKING HISTORY OF PASSENGER ======================

	@GetMapping("/history/{passengerId}")
	public ResponseEntity<ResponseStructure<List<BookingDTO>>> getBookingHistory(
			@PathVariable int passengerId)
	{
		return new ResponseEntity<>(
				bookingService.getBookingHistoryOfPassengerFromDatabase(passengerId),
				HttpStatus.FOUND);
	}

}