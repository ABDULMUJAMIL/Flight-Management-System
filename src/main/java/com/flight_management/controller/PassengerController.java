package com.flight_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flight_management.dto.Gender;
import com.flight_management.dto.PassengerDTO;
import com.flight_management.entity.Passenger;
import com.flight_management.repository.FlightRepository;
import com.flight_management.service.PassengerService;
import com.flight_management.dto.ResponseStructure;

@RestController
@RequestMapping("/passenger")
public class PassengerController
{

	@Autowired
	private PassengerService passengerService;
	
	@Autowired
	private FlightRepository flightRepo;

//	@PostMapping("/add")
//	public ResponseEntity<ResponseStructure<PassengerDTO>> createPassenger(
//			@RequestBody Passenger passenger)
//	{
//		return new ResponseEntity<>(
//				passengerService.createPassengerInDatabase(passenger),
//				HttpStatus.CREATED);
//	}

	@GetMapping("/")
	public ResponseEntity<ResponseStructure<List<PassengerDTO>>> getAllPassengers()
	{
		return new ResponseEntity<>(
				passengerService.getAllPassengersFromDatabase(),
				HttpStatus.FOUND);
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<PassengerDTO>> getPassengerById(
			@PathVariable("id") int passengerId)
	{
		return new ResponseEntity<>(
				passengerService.getPassengerByIdFromDatabase(passengerId),
				HttpStatus.FOUND);
	}

	@GetMapping("/contact/{contact}")
	public ResponseEntity<ResponseStructure<PassengerDTO>> getPassengerByContact(
			@PathVariable long contact)
	{
		return new ResponseEntity<>(
				passengerService.getPassengerByContactFromDatabase(contact),
				HttpStatus.FOUND);
	}

	@GetMapping("/booking/{bookingId}")
	public ResponseEntity<ResponseStructure<List<PassengerDTO>>> getPassengerByBooking(
			@PathVariable int bookingId)
	{
		return new ResponseEntity<>(
				passengerService.getPassengerByBookingIdFromDatabase(bookingId),
				HttpStatus.FOUND);
	}

	@GetMapping("/gender/{gender}")
	public ResponseEntity<ResponseStructure<List<PassengerDTO>>> getPassengerByGender(
			@PathVariable Gender gender)
	{
		return new ResponseEntity<>(
				passengerService.getPassengerByGenderFromDatabase(gender),
				HttpStatus.FOUND);
	}

	@GetMapping("/age/{minAge}/{maxAge}")
	public ResponseEntity<ResponseStructure<List<PassengerDTO>>> getPassengerByAgeRange(
			@PathVariable int minAge,
			@PathVariable int maxAge)
	{
		return new ResponseEntity<>(
				passengerService.getPassengerByAgeRangeFromDatabase(minAge, maxAge),
				HttpStatus.FOUND);
	}

	@GetMapping("/flight/{flightId}")
	public ResponseEntity<ResponseStructure<List<PassengerDTO>>> getPassengerByFlight(
			@PathVariable int flightId)
	{
		return new ResponseEntity<>(
				passengerService.getPassengerByFlightFromDatabase(flightId),
				HttpStatus.FOUND);
	}

	@PutMapping("/update")
	public ResponseEntity<ResponseStructure<PassengerDTO>> updatePassenger(
			@RequestBody Passenger passenger)
	{
		return new ResponseEntity<>(
				passengerService.updatePassengerInDatabase(passenger),
				HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ResponseStructure<PassengerDTO>> deletePassenger(
			@PathVariable int id)
	{
		return new ResponseEntity<>(
				passengerService.deletePassengerByIdFromDatabase(id),
				HttpStatus.OK);
	}

	@PatchMapping("/removeFromBooking/{id}")
	public ResponseEntity<ResponseStructure<PassengerDTO>> removePassengerFromBooking(
			@PathVariable int id)
	{
		return new ResponseEntity<>(
				passengerService.removePassengerFromBookingInDatabase(id),
				HttpStatus.OK);
	}

	@GetMapping("/pagination/{pn}/{ps}/{field}")
	public ResponseEntity<ResponseStructure<Page<PassengerDTO>>> getPassengerByPagination(
			@PathVariable Integer pn,
			@PathVariable Integer ps,
			@PathVariable String field)
	{
		return ResponseEntity.ok(
				passengerService.getPassengerByPaginationAndSorting(
						pn,
						ps,
						field));
	}

}