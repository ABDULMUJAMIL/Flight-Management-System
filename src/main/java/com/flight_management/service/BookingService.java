package com.flight_management.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flight_management.dto.BookingDTO;
import com.flight_management.dto.BookingStatus;
import com.flight_management.dto.PassengerDTO;
import com.flight_management.dto.PaymentDTO;
import com.flight_management.dto.PaymentStatus;
import com.flight_management.dto.ResponseStructure;
import com.flight_management.entity.Booking;
import com.flight_management.entity.Flight;
import com.flight_management.entity.Passenger;
import com.flight_management.entity.Payment;
import com.flight_management.exception.IdNotFoundException;
import com.flight_management.exception.InvalidInputException;
import com.flight_management.exception.NoRecordFoundException;
import com.flight_management.repository.BookingRepository;
import com.flight_management.repository.FlightRepository;
import com.flight_management.repository.PassengerRepository;
import com.flight_management.repository.PaymentRepository;

@Service
public class BookingService 
{
	@Autowired
	private BookingRepository bookingRepo;

	@Autowired
	private FlightRepository flightRepo;

	@Autowired
	private PassengerRepository passengerRepo;

	@Autowired
	private PaymentRepository paymentRepo;
	
	
	public static BookingDTO convertBookingToBookingDTO(Booking b)
	{
		BookingDTO dto = new BookingDTO();

		dto.setBookingId(b.getBookingId());
		dto.setBookingDateTime(b.getBookingDateTime());
		dto.setStatus(b.getStatus());

		dto.setFlightId(b.getFlight().getFlightId());
		dto.setAirline(b.getFlight().getAirline());
		dto.setSource(b.getFlight().getSource());
		dto.setDestination(b.getFlight().getDestination());

		dto.setPaymentId(b.getPayment().getPaymentId());
		dto.setAmount(b.getPayment().getAmount());
		dto.setPaymentStatus(b.getPayment().getPaymentStatus());

		dto.setTotalPassengers(b.getPassengers().size());

		return dto;
	}
	
	
	@Transactional
	public ResponseStructure<BookingDTO> createBookingInDatabase(Booking b)
	{

		// Booking Status Validation
		if(b.getStatus()==null)
		{
			throw new InvalidInputException(
					"Booking Status cannot be empty.");
		}

		// Flight Validation
		if(b.getFlight()==null)
		{
			throw new InvalidInputException(
					"Flight cannot be empty.");
		}

		Optional<Flight> flightOpt =
				flightRepo.findById(
						b.getFlight().getFlightId());

		if(flightOpt.isEmpty())
		{
			throw new InvalidInputException(
					"Invalid Flight Id.");
		}

		Flight flight = flightOpt.get();

		// Payment Validation
		if(b.getPayment()==null)
		{
			throw new InvalidInputException(
					"Payment cannot be empty.");
		}


		// Calculate Amount
		
				Payment payment = b.getPayment();
				
				if(payment.getModeOfPayment() == null)
				{
				    throw new InvalidInputException(
				            "Payment mode cannot be empty.");
				}
				
				if(b.getPassengers()==null || b.getPassengers().isEmpty())
				{
				    throw new InvalidInputException(
				            "Booking must contain at least one passenger.");
				}
				
				List<Passenger> passengers = b.getPassengers();
				
				
				if(payment.getPaymentStatus()==null)
				{
				    payment.setPaymentStatus(PaymentStatus.PENDING);
				}
				
				Set<Integer> seatNumbers = new HashSet<>();
				Set<Long> contacts = new HashSet<>();
				for(Passenger p : passengers)
				{
				    if(p.getName()==null || p.getName().isBlank())
				    {
				        throw new InvalidInputException("Passenger name cannot be empty.");
				    }

				    if(p.getAge()<1 || p.getAge()>120)
				    {
				        throw new InvalidInputException("Invalid passenger age.");
				    }

				    if(p.getGender()==null)
				    {
				        throw new InvalidInputException("Passenger gender is required.");
				    }

				    if(p.getSeatNumber()<=0)
				    {
				        throw new InvalidInputException("Invalid seat number.");
				    }
				    
				    if(!seatNumbers.add(p.getSeatNumber()))
				    {
				        throw new InvalidInputException(
				                "Duplicate seat number in booking request.");
				    }

				    String contact = String.valueOf(p.getContact());

				    if(contact.length()!=10 || !contact.matches("[6-9]\\d{9}"))
				    {
				        throw new InvalidInputException("Invalid contact number.");
				    }
				    
				    if(!contacts.add(p.getContact()))
				    {
				        throw new InvalidInputException(
				                "Duplicate contact number in booking request.");
				    }

				    if(passengerRepo.findPassengerByContact(p.getContact()).isPresent())
				    {
				        throw new InvalidInputException(
				                "Passenger already exists with contact : "
				                +p.getContact());
				    }

				    if(passengerRepo.findPassengerByFlightAndSeatNumber(
				            flight.getFlightId(),
				            p.getSeatNumber()).isPresent())
				    {
				        throw new InvalidInputException(
				                "Seat number "+p.getSeatNumber()+" already booked.");
				    }

				    p.setBooking(b);
				}
			// price validation
				if(flight.getPrice() <= 0)
				{
				    throw new InvalidInputException(
				            "Invalid flight price.");
				}
				
				double totalAmount =
						flight.getPrice()*passengers.size();

				payment.setAmount(totalAmount);
				
				if(flight.getAvailableSeats()<passengers.size())
				{
				    throw new InvalidInputException(
				            "Not enough seats available.");
				}

				
		
		payment.setBooking(b);

		b.setFlight(flight);

		b.setPayment(payment);

		b.setPassengers(passengers);
		
		
		Booking savedBooking =
				bookingRepo.save(b);
		
		
		flight.setAvailableSeats(
		        flight.getAvailableSeats()-passengers.size());

		flightRepo.save(flight);

		ResponseStructure<BookingDTO> res =
				new ResponseStructure<>();

		res.setData(
				convertBookingToBookingDTO(savedBooking));

		res.setMessage(
				"Booking created successfully.");

		res.setStatusCode(
				HttpStatus.CREATED.value());

		return res;

	}
	
	public ResponseStructure<List<BookingDTO>> getAllBookingsFromDatabase()
	{
		List<Booking> li = bookingRepo.findAll();

		ArrayList<BookingDTO> convertedArray = new ArrayList<>();

		for(Booking b : li)
		{
			convertedArray.add(convertBookingToBookingDTO(b));
		}

		ResponseStructure<List<BookingDTO>> res =
				new ResponseStructure<>();

		res.setData(convertedArray);
		res.setMessage(li.size()+" records found in database.");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;
	}
	
	public ResponseStructure<BookingDTO> getBookingByIdFromDatabase(int bookingId)
	{
		Optional<Booking> opt =
				bookingRepo.findById(bookingId);

		if(opt.isEmpty())
		{
			throw new NoRecordFoundException(
					"No Booking exists with Id : "
					+bookingId);
		}

		ResponseStructure<BookingDTO> res =
				new ResponseStructure<>();

		res.setData(convertBookingToBookingDTO(opt.get()));
		res.setMessage("Record found in database.");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;
	}
	
	public ResponseStructure<List<BookingDTO>> getBookingByFlightFromDatabase(int flightId)
	{

		List<Booking> li =
				bookingRepo.findBookingByFlightId(flightId);

		if(li.isEmpty())
		{
			throw new NoRecordFoundException(
					"No Bookings found for Flight Id : "
					+flightId);
		}

		ArrayList<BookingDTO> dto =
				new ArrayList<>();

		for(Booking b : li)
		{
			dto.add(convertBookingToBookingDTO(b));
		}

		ResponseStructure<List<BookingDTO>> res =
				new ResponseStructure<>();

		res.setData(dto);
		res.setMessage(li.size()+" records found.");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;

	}
	
	public ResponseStructure<List<BookingDTO>> getBookingByStatusFromDatabase(BookingStatus status)
	{

		List<Booking> li =
				bookingRepo.findBookingByStatus(status);

		if(li.isEmpty())
		{
			throw new NoRecordFoundException(
					"No Bookings found with status : "
					+status);
		}

		ArrayList<BookingDTO> dto =
				new ArrayList<>();

		for(Booking b : li)
		{
			dto.add(convertBookingToBookingDTO(b));
		}

		ResponseStructure<List<BookingDTO>> res =
				new ResponseStructure<>();

		res.setData(dto);
		res.setMessage(li.size()+" records found.");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;

	}
	
	
	public ResponseStructure<List<PassengerDTO>> getPassengersOfBookingFromDatabase(int bookingId)
	{

		Optional<Booking> opt =
				bookingRepo.findById(bookingId);

		if(opt.isEmpty())
		{
			throw new NoRecordFoundException(
					"Booking doesn't exist.");
		}

		ArrayList<PassengerDTO> dto =
				new ArrayList<>();

		for(Passenger p : opt.get().getPassengers())
		{
			dto.add(
				PassengerService.convertPassengerToPassengerDTO(p));
		}

		ResponseStructure<List<PassengerDTO>> res =
				new ResponseStructure<>();

		res.setData(dto);
		res.setMessage(dto.size()+" passengers found.");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;

	}
	
	
	public ResponseStructure<PaymentDTO> getPaymentOfBookingFromDatabase(int bookingId)
	{

		Optional<Booking> opt =
				bookingRepo.findById(bookingId);

		if(opt.isEmpty())
		{
			throw new NoRecordFoundException(
					"Booking doesn't exist.");
		}

		ResponseStructure<PaymentDTO> res =
				new ResponseStructure<>();

		res.setData(
				PaymentService.convertPaymentToPaymentDTO(
						opt.get().getPayment()));

		res.setMessage("Payment found.");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;

	}
	
	
	public ResponseStructure<List<BookingDTO>> getBookingByDateFromDatabase(LocalDate bookingDate)
	{

		LocalDateTime start = bookingDate.atStartOfDay();
		LocalDateTime end = bookingDate.atTime(23,59,59);

		List<Booking> li =
				bookingRepo.findBookingByDate(start, end);

		if(li.isEmpty())
		{
			throw new NoRecordFoundException(
					"No Bookings found on : " + bookingDate);
		}

		ArrayList<BookingDTO> dto = new ArrayList<>();

		for(Booking b : li)
		{
			dto.add(convertBookingToBookingDTO(b));
		}

		ResponseStructure<List<BookingDTO>> res =
				new ResponseStructure<>();

		res.setData(dto);
		res.setMessage(li.size()+" records found.");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;

	}
	
	
	@Transactional
	public ResponseStructure<BookingDTO> updateBookingStatusInDatabase(Booking booking)
	{

		if(booking.getBookingId()==0)
		{
			throw new IdNotFoundException(
					"Booking Id must be provided.");
		}

		if(booking.getStatus()==null)
		{
			throw new InvalidInputException(
					"Booking Status cannot be empty.");
		}

		Optional<Booking> opt =
				bookingRepo.findById(
						booking.getBookingId());

		if(opt.isEmpty())
		{
			throw new NoRecordFoundException(
					"Booking not found.");
		}

		Booking fetchedBooking = opt.get();

		fetchedBooking.setStatus(booking.getStatus());

		Booking updatedBooking =
				bookingRepo.save(fetchedBooking);

		ResponseStructure<BookingDTO> res =
				new ResponseStructure<>();

		res.setData(
				convertBookingToBookingDTO(updatedBooking));

		res.setMessage("Booking Status updated successfully.");
		res.setStatusCode(HttpStatus.OK.value());

		return res;

	}
	
	
	
	@Transactional
	public ResponseStructure<BookingDTO> deleteBookingFromDatabase(int bookingId)
	{

		Optional<Booking> opt =
				bookingRepo.findById(bookingId);

		if(opt.isEmpty())
		{
			throw new NoRecordFoundException(
					"Booking not found.");
		}

		Booking booking = opt.get();

		if(booking.getStatus()==BookingStatus.CANCELLED)
		{
			throw new InvalidInputException(
					"Booking is already cancelled.");
		}
		
		Flight flight = booking.getFlight();

		flight.setAvailableSeats(
				flight.getAvailableSeats()
				+ booking.getPassengers().size());

		flightRepo.save(flight);

		booking.setStatus(BookingStatus.CANCELLED);

		Payment payment = booking.getPayment();

		payment.setPaymentStatus(
				PaymentStatus.REFUNDED);

		paymentRepo.save(payment);

		Booking updatedBooking =
				bookingRepo.save(booking);

		ResponseStructure<BookingDTO> res =
				new ResponseStructure<>();

		res.setData(
				convertBookingToBookingDTO(updatedBooking));

		res.setMessage(
				"Booking cancelled successfully.");

		res.setStatusCode(HttpStatus.OK.value());

		return res;

	}
	
	
	public ResponseStructure<List<BookingDTO>> getBookingHistoryOfPassengerFromDatabase(int passengerId)
	{

		List<Booking> li =
				bookingRepo.findBookingHistoryOfPassenger(passengerId);

		if(li.isEmpty())
		{
			throw new NoRecordFoundException(
					"No Booking History found.");
		}

		ArrayList<BookingDTO> dto =
				new ArrayList<>();

		for(Booking b : li)
		{
			dto.add(convertBookingToBookingDTO(b));
		}

		ResponseStructure<List<BookingDTO>> res =
				new ResponseStructure<>();

		res.setData(dto);
		res.setMessage(li.size()+" records found.");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;

	}
	
	
	
	
	

}





