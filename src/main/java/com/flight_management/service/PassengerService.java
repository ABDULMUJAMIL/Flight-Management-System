package com.flight_management.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.flight_management.dto.PassengerDTO;
import com.flight_management.dto.Gender;
import com.flight_management.entity.Booking;
import com.flight_management.entity.Flight;
import com.flight_management.entity.Passenger;
import com.flight_management.exception.IdNotFoundException;
import com.flight_management.exception.InvalidInputException;
import com.flight_management.exception.NoRecordFoundException;
import com.flight_management.repository.BookingRepository;
import com.flight_management.repository.FlightRepository;
import com.flight_management.repository.PassengerRepository;
import com.flight_management.dto.ResponseStructure;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PassengerService
{

	public static PassengerDTO convertPassengerToPassengerDTO(Passenger p)
	{
		PassengerDTO dto = new PassengerDTO();

		dto.setPassengerId(p.getPassengerId());
		dto.setName(p.getName());
		dto.setAge(p.getAge());
		dto.setGender(p.getGender());
		dto.setSeatNumber(p.getSeatNumber());
		dto.setContact(p.getContact());

		return dto;
	}

	@Autowired
	private PassengerRepository passengerRepo;

	@Autowired
	private BookingRepository bookingRepo;
	
	@Autowired
	private FlightRepository flightRepo;

//	//====================== ADD PASSENGER ======================
//
//	@Transactional
//	public ResponseStructure<PassengerDTO> createPassengerInDatabase(Passenger p)
//	{
//
//		// Passenger Name Validation
//		if(p.getName()==null || p.getName().isBlank())
//		{
//			throw new InvalidInputException("Passenger name cannot be empty.");
//		}
//
//		// Age Validation
//		if(p.getAge()<1 || p.getAge()>120)
//		{
//		    throw new InvalidInputException("Invalid passenger age.");
//		}
//
//		// Gender Validation
//		if(p.getGender()==null)
//		{
//			throw new InvalidInputException("Passenger gender cannot be empty.");
//		}
//
//		// Seat Number Validation
//		if(p.getSeatNumber()<=0)
//		{
//			throw new InvalidInputException("Invalid seat number.");
//		}
//
//		// Contact Validation
//		String contact = String.valueOf(p.getContact());
//
//		if(contact.length()!=10 || !contact.matches("[6-9]\\d{9}"))
//		{
//		    throw new InvalidInputException("Invalid contact number.");
//		}
//		// Duplicate Contact Check
//		Optional<Passenger> contactOpt =
//				passengerRepo.findPassengerByContact(p.getContact());
//
//		if(contactOpt.isPresent())
//		{
//			throw new InvalidInputException(
//					"Passenger already exists with contact number : "
//					+p.getContact());
//		}
//
//		// Booking Validation
//		if(p.getBooking()==null)
//		{
//			throw new InvalidInputException("Booking cannot be empty.");
//		}
//
//		Optional<Booking> bookingOpt =
//				bookingRepo.findById(
//						p.getBooking().getBookingId());
//
//		if(bookingOpt.isEmpty())
//		{
//			throw new InvalidInputException(
//					"Invalid Booking Id.");
//		}
//
//		Booking booking = bookingOpt.get();
//
//		if(booking.getFlight()==null)
//		{
//			throw new InvalidInputException(
//					"No Flight is assigned to this Booking.");
//		}
//
//		Flight flight = booking.getFlight();
//		
//		if(p.getSeatNumber() > flight.getTotalSeats())
//		{
//			throw new InvalidInputException(
//					"Seat Number exceeds total seats of the flight.");
//		}
//
//		// Seat Availability
//		if(flight.getAvailableSeats()<=0)
//		{
//			throw new InvalidInputException(
//					"No seats available for this flight.");
//		}
//		
//		
//		
//
//		// Seat Already Booked
//		Optional<Passenger> seatOpt =
//				passengerRepo.findPassengerByFlightAndSeatNumber(
//						flight.getFlightId(),
//						p.getSeatNumber());
//
//		if(seatOpt.isPresent())
//		{
//			throw new InvalidInputException(
//					"Seat Number "
//					+p.getSeatNumber()
//					+" is already booked.");
//		}
//
//		// Reduce Available Seats
//		flight.setAvailableSeats(
//				flight.getAvailableSeats()-1);
//
//		flightRepo.save(flight);
//
//		// Assign Booking
//		p.setBooking(booking);
//
//		// Save Passenger
//		Passenger savedPassenger =
//				passengerRepo.save(p);
//
//		ResponseStructure<PassengerDTO> res =
//				new ResponseStructure<>();
//
//		res.setData(
//				convertPassengerToPassengerDTO(savedPassenger));
//
//		res.setMessage("Passenger added successfully.");
//		res.setStatusCode(HttpStatus.CREATED.value());
//
//		return res;
//
//	}

	//====================== GET ALL ======================

	public ResponseStructure<List<PassengerDTO>> getAllPassengersFromDatabase()
	{

		List<Passenger> li =
				passengerRepo.findAll();

		ArrayList<PassengerDTO> convertedArray =
				new ArrayList<>();

		for(Passenger p : li)
		{
			convertedArray.add(
					convertPassengerToPassengerDTO(p));
		}

		ResponseStructure<List<PassengerDTO>> res =
				new ResponseStructure<>();

		res.setData(convertedArray);
		res.setMessage(li.size()+" records found in database...");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;
	}

	//====================== GET BY ID ======================

	public ResponseStructure<PassengerDTO> getPassengerByIdFromDatabase(int id)
	{

		Optional<Passenger> opt =
				passengerRepo.findById(id);

		if(opt.isEmpty())
		{
			throw new IdNotFoundException(
					"Passenger with id : "+id+
					" doesn't exist in database.");
		}

		ResponseStructure<PassengerDTO> res =
				new ResponseStructure<>();

		res.setData(
				convertPassengerToPassengerDTO(opt.get()));

		res.setMessage("Record found in database...");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;
	}

	//====================== GET BY CONTACT ======================

	public ResponseStructure<PassengerDTO> getPassengerByContactFromDatabase(long contact)
	{

		Optional<Passenger> opt =
				passengerRepo.findPassengerByContact(contact);

		if(opt.isEmpty())
		{
			throw new NoRecordFoundException(
					"No Passenger exists with contact : "
					+contact);
		}

		ResponseStructure<PassengerDTO> res =
				new ResponseStructure<>();

		res.setData(
				convertPassengerToPassengerDTO(opt.get()));

		res.setMessage("Record found in database...");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;
	}
	
	//====================== GET BY BOOKING ID ======================

	public ResponseStructure<List<PassengerDTO>> getPassengerByBookingIdFromDatabase(int bookingId)
	{

		Optional<Booking> booking = bookingRepo.findById(bookingId);

		if(booking.isEmpty())
		{
			throw new NoRecordFoundException(
					"Booking with id : "+bookingId+" doesn't exist in database.");
		}

		List<Passenger> li =
				passengerRepo.findPassengerByBookingId(bookingId);

		if(li.size()==0)
		{
			throw new NoRecordFoundException(
					"No Passengers exist for booking id : "
					+bookingId+" in database.");
		}

		ArrayList<PassengerDTO> convertedArray =
				new ArrayList<>();

		for(Passenger p : li)
		{
			convertedArray.add(
					convertPassengerToPassengerDTO(p));
		}

		ResponseStructure<List<PassengerDTO>> res =
				new ResponseStructure<>();

		res.setData(convertedArray);
		res.setMessage(li.size()+" records found in database...");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;
	}



	//====================== GET BY GENDER ======================

	public ResponseStructure<List<PassengerDTO>> getPassengerByGenderFromDatabase(Gender gender)
	{

		List<Passenger> li =
				passengerRepo.findPassengerByGender(gender);

		if(li.size()==0)
		{
			throw new NoRecordFoundException(
					"No Passenger exists with gender : "+gender);
		}

		ArrayList<PassengerDTO> convertedArray =
				new ArrayList<>();

		for(Passenger p : li)
		{
			convertedArray.add(
					convertPassengerToPassengerDTO(p));
		}

		ResponseStructure<List<PassengerDTO>> res =
				new ResponseStructure<>();

		res.setData(convertedArray);
		res.setMessage(li.size()+" records found in database...");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;
	}



	//====================== GET BY AGE RANGE ======================

	public ResponseStructure<List<PassengerDTO>> getPassengerByAgeRangeFromDatabase(
			int minAge,
			int maxAge)
	{

		List<Passenger> li =
				passengerRepo.findPassengerByAgeRange(minAge, maxAge);

		if(li.size()==0)
		{
			throw new NoRecordFoundException(
					"No Passenger exists between age "
					+minAge+" and "+maxAge);
		}

		ArrayList<PassengerDTO> convertedArray =
				new ArrayList<>();

		for(Passenger p : li)
		{
			convertedArray.add(
					convertPassengerToPassengerDTO(p));
		}

		ResponseStructure<List<PassengerDTO>> res =
				new ResponseStructure<>();

		res.setData(convertedArray);
		res.setMessage(li.size()+" records found in database...");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;
	}



	//====================== GET PASSENGERS BY FLIGHT ======================

	public ResponseStructure<List<PassengerDTO>> getPassengerByFlightFromDatabase(
			int flightId)
	{

		List<Passenger> li =
				passengerRepo.findPassengerByFlight(flightId);

		if(li.size()==0)
		{
			throw new NoRecordFoundException(
					"No Passengers exist for flight id : "
					+flightId+" in database.");
		}

		ArrayList<PassengerDTO> convertedArray =
				new ArrayList<>();

		for(Passenger p : li)
		{
			convertedArray.add(
					convertPassengerToPassengerDTO(p));
		}

		ResponseStructure<List<PassengerDTO>> res =
				new ResponseStructure<>();

		res.setData(convertedArray);
		res.setMessage(li.size()+" records found in database...");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;
	}



	//====================== UPDATE PASSENGER ======================

	public ResponseStructure<PassengerDTO> updatePassengerInDatabase(
			Passenger p)
	{

		if(p.getPassengerId()==0)
		{
			throw new IdNotFoundException(
					"Passenger Id must be passed to update.");
		}

		Optional<Passenger> opt =
				passengerRepo.findById(
						p.getPassengerId());

		if(opt.isEmpty())
		{
			throw new NoRecordFoundException(
					"Passenger with id : "
					+p.getPassengerId()+
					" doesn't exist in database.");
		}
		
		

		Passenger fetchedPassenger =
				opt.get();
		
		if(p.getSeatNumber()>0)
		{
			Optional<Passenger> seatOpt =
					passengerRepo.findPassengerByFlightAndSeatNumber(
							fetchedPassenger.getBooking().getFlight().getFlightId(),
							p.getSeatNumber());

			if(seatOpt.isPresent()
					&& seatOpt.get().getPassengerId()!=fetchedPassenger.getPassengerId())
			{
				throw new InvalidInputException(
						"Seat Number "
						+p.getSeatNumber()
						+" is already booked.");
			}

			fetchedPassenger.setSeatNumber(p.getSeatNumber());
		}

		if(p.getName()!=null)
		{
			fetchedPassenger.setName(
					p.getName());
		}

		if(p.getAge()>0)
		{
			fetchedPassenger.setAge(
					p.getAge());
		}

		if(p.getGender()!=null)
		{
			fetchedPassenger.setGender(
					p.getGender());
		}

		if(p.getSeatNumber()>0)
		{
			fetchedPassenger.setSeatNumber(
					p.getSeatNumber());
		}

		if(p.getContact()!=0)
		{
			fetchedPassenger.setContact(
					p.getContact());
		}

		ResponseStructure<PassengerDTO> res =
				new ResponseStructure<>();

		res.setData(
				convertPassengerToPassengerDTO(
						passengerRepo.save(
								fetchedPassenger)));

		res.setMessage("Record updated in database...");
		res.setStatusCode(HttpStatus.OK.value());

		return res;
	}
	
	//====================== DELETE PASSENGER ======================

	@Transactional
	public ResponseStructure<PassengerDTO> deletePassengerByIdFromDatabase(int id)
	{

		Optional<Passenger> opt =
				passengerRepo.findById(id);

		if(opt.isEmpty())
		{
			throw new NoRecordFoundException(
					"Passenger with id : "
					+id
					+" doesn't exist in database.");
		}

		Passenger passenger = opt.get();

		if(passenger.getBooking()!=null)
		{
			Flight flight =
					passenger.getBooking().getFlight();

			flight.setAvailableSeats(
					flight.getAvailableSeats()+1);

			flightRepo.save(flight);
		}

		passengerRepo.delete(passenger);

		ResponseStructure<PassengerDTO> res =
				new ResponseStructure<>();

		res.setData(null);
		res.setMessage("Passenger deleted successfully.");
		res.setStatusCode(HttpStatus.OK.value());

		return res;

	}



	//====================== REMOVE PASSENGER FROM BOOKING ======================

	@Transactional
	public ResponseStructure<PassengerDTO> removePassengerFromBookingInDatabase(int passengerId)
	{

		Optional<Passenger> opt =
				passengerRepo.findById(passengerId);

		if(opt.isEmpty())
		{
			throw new NoRecordFoundException(
					"Passenger with id : "
					+passengerId
					+" doesn't exist in database.");
		}

		Passenger passenger = opt.get();

		if(passenger.getBooking()==null)
		{
			throw new InvalidInputException(
					"Passenger is not assigned to any booking.");
		}

		Flight flight =
				passenger.getBooking().getFlight();

		flight.setAvailableSeats(
				flight.getAvailableSeats()+1);

		flightRepo.save(flight);

		passenger.setBooking(null);

		Passenger updatedPassenger =
				passengerRepo.save(passenger);

		ResponseStructure<PassengerDTO> res =
				new ResponseStructure<>();

		res.setData(
				convertPassengerToPassengerDTO(updatedPassenger));

		res.setMessage("Passenger removed from booking successfully.");
		res.setStatusCode(HttpStatus.OK.value());

		return res;

	}



	//====================== PAGINATION & SORTING ======================

	public ResponseStructure<Page<PassengerDTO>> getPassengerByPaginationAndSorting(
			int pn,
			int ps,
			String field)
	{

		Page<Passenger> pages =
				passengerRepo.findAll(
						PageRequest.of(
								pn,
								ps,
								Sort.by(field).ascending()));

		Page<PassengerDTO> dtoPage =
				pages.map(p ->{

					PassengerDTO dto =
							new PassengerDTO();

					dto.setPassengerId(
							p.getPassengerId());

					dto.setName(
							p.getName());

					dto.setAge(
							p.getAge());

					dto.setGender(
							p.getGender());

					dto.setSeatNumber(
							p.getSeatNumber());

					dto.setContact(
							p.getContact());

					return dto;

				});

		ResponseStructure<Page<PassengerDTO>> res =
				new ResponseStructure<>();

		res.setData(dtoPage);
		res.setMessage(dtoPage.getNumberOfElements()+" records found");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;
	}
	
	
}