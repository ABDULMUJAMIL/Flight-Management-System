package com.flight_management.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.flight_management.dto.FlightDTO;
import com.flight_management.entity.Booking;
import com.flight_management.entity.Flight;
import com.flight_management.exception.IdNotFoundException;
import com.flight_management.exception.InvalidInputException;
import com.flight_management.exception.NoRecordFoundException;
import com.flight_management.repository.BookingRepository;
import com.flight_management.repository.FlightRepository;
import com.flight_management.dto.ResponseStructure;

@Service
public class FlightService
{
    public static FlightDTO convertFlightToFlightDTO(Flight f)
    {
        FlightDTO fdto = new FlightDTO();

        fdto.setFlightId(f.getFlightId());
        fdto.setAirline(f.getAirline());
        fdto.setSource(f.getSource());
        fdto.setDestination(f.getDestination());
        fdto.setDepartureDateTime(f.getDepartureDateTime());
        fdto.setArrivalDateTime(f.getArrivalDateTime());
        fdto.setAvailableSeats(f.getAvailableSeats());
        fdto.setPrice(f.getPrice());

        return fdto;
    }

    @Autowired
    private FlightRepository flightRepo;
    
    @Autowired
    private BookingRepository bookingRepo;

    // ====================== ADD FLIGHT ======================

    public ResponseStructure<FlightDTO> createFlightInDatabase(Flight f)
    {

        if(f.getAirline()==null || f.getAirline().isBlank())
        {
            throw new InvalidInputException("Airline cannot be empty.");
        }

        if(f.getSource()==null || f.getSource().isBlank())
        {
            throw new InvalidInputException("Source cannot be empty.");
        }

        if(f.getDestination()==null || f.getDestination().isBlank())
        {
            throw new InvalidInputException("Destination cannot be empty.");
        }

        if(f.getDepartureDateTime()==null)
        {
            throw new InvalidInputException("Departure Date Time cannot be empty.");
        }

        if(f.getArrivalDateTime()==null)
        {
            throw new InvalidInputException("Arrival Date Time cannot be empty.");
        }

        if(f.getTotalSeats()<=0)
        {
            throw new InvalidInputException("Total Seats must be greater than zero.");
        }

        if(f.getAvailableSeats()>f.getTotalSeats())
        {
            throw new InvalidInputException("Available Seats cannot be greater than Total Seats.");
        }

        if(f.getPrice()<=0)
        {
            throw new InvalidInputException("Price should be greater than zero.");
        }

        Flight savedFlight = flightRepo.save(f);

        ResponseStructure<FlightDTO> res = new ResponseStructure<>();

        res.setData(convertFlightToFlightDTO(savedFlight));
        res.setMessage("Record added to database...");
        res.setStatusCode(HttpStatus.CREATED.value());

        return res;
    }

    // ====================== GET ALL ======================

    public ResponseStructure<List<FlightDTO>> getAllFlightsFromDatabase()
    {

        List<Flight> li = flightRepo.findAll();

        ArrayList<FlightDTO> convertedArray = new ArrayList<>();

        for(Flight f : li)
        {
            convertedArray.add(convertFlightToFlightDTO(f));
        }

        ResponseStructure<List<FlightDTO>> res =
                new ResponseStructure<>();

        res.setData(convertedArray);
        res.setMessage(li.size()+" records found in database...");
        res.setStatusCode(HttpStatus.FOUND.value());

        return res;
    }

    // ====================== GET BY ID ======================

    public ResponseStructure<FlightDTO> getFlightByIdFromDatabase(int id)
    {

        Optional<Flight> opt = flightRepo.findById(id);

        if(opt.isEmpty())
        {
            throw new IdNotFoundException(
                    "Record does not exist with flight id : "+id+" in database.");
        }

        ResponseStructure<FlightDTO> res =
                new ResponseStructure<>();

        res.setData(convertFlightToFlightDTO(opt.get()));
        res.setMessage("Record found in database...");
        res.setStatusCode(HttpStatus.FOUND.value());

        return res;
    }

    // ====================== GET BY AIRLINE ======================

    public ResponseStructure<List<FlightDTO>> getFlightByAirlineFromDatabase(String airline)
    {

        List<Flight> li = flightRepo.findFlightByAirline(airline);

        if(li.size()==0)
        {
            throw new NoRecordFoundException(
                    "No Flights exist for airline : "+airline+" in database.");
        }

        ArrayList<FlightDTO> convertedArray =
                new ArrayList<>();

        for(Flight f : li)
        {
            convertedArray.add(convertFlightToFlightDTO(f));
        }

        ResponseStructure<List<FlightDTO>> res =
                new ResponseStructure<>();

        res.setData(convertedArray);
        res.setMessage(li.size()+" records found in database...");
        res.setStatusCode(HttpStatus.FOUND.value());

        return res;
    }
    
 // ====================== GET BY SOURCE, DESTINATION AND DATE ======================

    public ResponseStructure<List<FlightDTO>> getFlightBySourceDestinationAndDateFromDatabase(
            String source,
            String destination,
            LocalDate departureDate)
    {

        List<Flight> li =
                flightRepo.findFlightBySourceDestinationAndDate(source,
                        destination,
                        departureDate);

        if(li.size()==0)
        {
            throw new NoRecordFoundException(
                    "No Flights exist from source : "
                    +source+" to "+destination+
                    " on "+departureDate+" in database.");
        }

        ArrayList<FlightDTO> convertedArray =
                new ArrayList<>();

        for(Flight f : li)
        {
            convertedArray.add(convertFlightToFlightDTO(f));
        }

        ResponseStructure<List<FlightDTO>> res =
                new ResponseStructure<>();

        res.setData(convertedArray);
        res.setMessage(li.size()+" records found in database...");
        res.setStatusCode(HttpStatus.FOUND.value());

        return res;
    }



    // ====================== UPDATE FLIGHT ======================

    public ResponseStructure<FlightDTO> updateFlightInDatabase(Flight f)
    {

        if(f.getFlightId()==0)
        {
            throw new IdNotFoundException(
                    "Flight Id must be passed to update details.");
        }

        Optional<Flight> opt =
                flightRepo.findById(f.getFlightId());

        if(opt.isEmpty())
        {
            throw new NoRecordFoundException(
                    "Record doesn't exist with flight id : "
                    +f.getFlightId()+" in database.");
        }

        Flight fetchedFlight = opt.get();

        if(f.getAirline()!=null)
        {
            fetchedFlight.setAirline(f.getAirline());
        }

        if(f.getSource()!=null)
        {
            fetchedFlight.setSource(f.getSource());
        }

        if(f.getDestination()!=null)
        {
            fetchedFlight.setDestination(f.getDestination());
        }

        if(f.getDepartureDateTime()!=null)
        {
            fetchedFlight.setDepartureDateTime(
                    f.getDepartureDateTime());
        }

        if(f.getArrivalDateTime()!=null)
        {
            fetchedFlight.setArrivalDateTime(
                    f.getArrivalDateTime());
        }

        if(f.getTotalSeats()>0)
        {
            fetchedFlight.setTotalSeats(
                    f.getTotalSeats());
        }

        if(f.getAvailableSeats()>=0)
        {
            fetchedFlight.setAvailableSeats(
                    f.getAvailableSeats());
        }

        if(f.getPrice()>0)
        {
            fetchedFlight.setPrice(
                    f.getPrice());
        }

        ResponseStructure<FlightDTO> res =
                new ResponseStructure<>();

        res.setData(
                convertFlightToFlightDTO(
                        flightRepo.save(fetchedFlight)));

        res.setMessage("Record updated in database...");
        res.setStatusCode(HttpStatus.OK.value());

        return res;
    }



    // ====================== DELETE FLIGHT ======================

    public ResponseStructure<FlightDTO> deleteFlightByIdFromDatabase(int id)
    {

        Optional<Flight> opt =
                flightRepo.findById(id);

        if(opt.isEmpty())
        {
            throw new NoRecordFoundException(
                    "Flight with id : "
                    +id+" doesn't exist in database.");
        }List<Booking> bookings = bookingRepo.findBookingByFlightId(id);

        if(!bookings.isEmpty())
        {
        	throw new InvalidInputException(
        			"Cannot delete flight because bookings exist.");
        }

        flightRepo.deleteById(id);

        ResponseStructure<FlightDTO> res =
                new ResponseStructure<>();

        res.setData(null);
        res.setMessage(
                "Record with flight id : "
                +id+" deleted from database...");
        res.setStatusCode(HttpStatus.OK.value());

        return res;
    }
    
 // ====================== GET FLIGHTS WITHIN PRICE RANGE ======================

    public ResponseStructure<List<FlightDTO>> getFlightsWithinPriceRangeFromDatabase(
            double minPrice,
            double maxPrice)
    {

        List<Flight> li =
                flightRepo.findFlightsWithinPriceRange(minPrice, maxPrice);

        if(li.size()==0)
        {
            throw new NoRecordFoundException(
                    "No Flights exist between price : "
                    +minPrice+" and "+maxPrice+" in database.");
        }

        ArrayList<FlightDTO> convertedArray =
                new ArrayList<>();

        for(Flight f : li)
        {
            convertedArray.add(convertFlightToFlightDTO(f));
        }

        ResponseStructure<List<FlightDTO>> res =
                new ResponseStructure<>();

        res.setData(convertedArray);
        res.setMessage(li.size()+" records found in database...");
        res.setStatusCode(HttpStatus.FOUND.value());

        return res;
    }



    // ====================== GET CHEAPEST FLIGHT ======================

    public ResponseStructure<FlightDTO> getCheapestFlightBetweenCitiesFromDatabase(
            String source,
            String destination)
    {

        List<Flight> li =
                flightRepo.findCheapestFlightBetweenCities(source, destination);

        if(li.size()==0)
        {
            throw new NoRecordFoundException(
                    "No Flights exist from "
                    +source+" to "+destination+" in database.");
        }

        ResponseStructure<FlightDTO> res =
                new ResponseStructure<>();

        res.setData(convertFlightToFlightDTO(li.get(0)));
        res.setMessage("Cheapest Flight found...");
        res.setStatusCode(HttpStatus.FOUND.value());

        return res;
    }



    // ====================== GET FLIGHTS HAVING MORE THAN X AVAILABLE SEATS ======================

    public ResponseStructure<List<FlightDTO>> getFlightHavingMoreThanAvailableSeatsFromDatabase(
            int seats)
    {

        List<Flight> li =
                flightRepo.findFlightHavingMoreThanAvailableSeats(seats);

        if(li.size()==0)
        {
            throw new NoRecordFoundException(
                    "No Flights found having more than "
                    +seats+" available seats.");
        }

        ArrayList<FlightDTO> convertedArray =
                new ArrayList<>();

        for(Flight f : li)
        {
            convertedArray.add(convertFlightToFlightDTO(f));
        }

        ResponseStructure<List<FlightDTO>> res =
                new ResponseStructure<>();

        res.setData(convertedArray);
        res.setMessage(li.size()+" records found in database...");
        res.setStatusCode(HttpStatus.FOUND.value());

        return res;
    }



    // ====================== PAGINATION & SORTING ======================

    public ResponseStructure<Page<FlightDTO>> getFlightByPaginationAndSorting(
            int pn,
            int ps,
            String field)
    {

        Page<Flight> pages =
                flightRepo.findAll(
                        PageRequest.of(
                                pn,
                                ps,
                                Sort.by(field).ascending()));

        Page<FlightDTO> dtoPage =
                pages.map(f ->{

                    FlightDTO dto =
                            new FlightDTO();

                    dto.setFlightId(f.getFlightId());
                    dto.setAirline(f.getAirline());
                    dto.setSource(f.getSource());
                    dto.setDestination(f.getDestination());
                    dto.setDepartureDateTime(f.getDepartureDateTime());
                    dto.setArrivalDateTime(f.getArrivalDateTime());
                    dto.setAvailableSeats(f.getAvailableSeats());
                    dto.setPrice(f.getPrice());

                    return dto;

                });

        ResponseStructure<Page<FlightDTO>> res =
                new ResponseStructure<>();

        res.setData(dtoPage);
        res.setMessage(dtoPage.getNumberOfElements()+" records found");
        res.setStatusCode(HttpStatus.FOUND.value());

        return res;
    }
}