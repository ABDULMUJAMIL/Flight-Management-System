package com.flight_management.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flight_management.dto.FlightDTO;
import com.flight_management.entity.Flight;
import com.flight_management.service.FlightService;
import com.flight_management.dto.ResponseStructure;

@RestController
@RequestMapping("/flight")
public class FlightController
{
    @Autowired
    private FlightService flightService;

    @PostMapping("/add")
    public ResponseEntity<ResponseStructure<FlightDTO>> createFlight(@RequestBody Flight flight)
    {
        return new ResponseEntity<ResponseStructure<FlightDTO>>(
                flightService.createFlightInDatabase(flight),
                HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<ResponseStructure<List<FlightDTO>>> getAllFlights()
    {
        return new ResponseEntity<ResponseStructure<List<FlightDTO>>>(
                flightService.getAllFlightsFromDatabase(),
                HttpStatus.FOUND);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ResponseStructure<FlightDTO>> getFlightById(
            @PathVariable("id") int flightId)
    {
        return new ResponseEntity<ResponseStructure<FlightDTO>>(
                flightService.getFlightByIdFromDatabase(flightId),
                HttpStatus.FOUND);
    }

    @GetMapping("/airline/{airline}")
    public ResponseEntity<ResponseStructure<List<FlightDTO>>> getFlightByAirline(
            @PathVariable("airline") String airline)
    {
        return new ResponseEntity<ResponseStructure<List<FlightDTO>>>(
                flightService.getFlightByAirlineFromDatabase(airline),
                HttpStatus.FOUND);
    }

    @GetMapping("/sourceDestination/{source}/{destination}/{date}")
    public ResponseEntity<ResponseStructure<List<FlightDTO>>> getFlightBySourceDestinationAndDate(
            @PathVariable String source,
            @PathVariable String destination,
            @PathVariable LocalDate date)
    {
        return new ResponseEntity<ResponseStructure<List<FlightDTO>>>(
                flightService.getFlightBySourceDestinationAndDateFromDatabase(
                        source,
                        destination,
                        date),
                HttpStatus.FOUND);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseStructure<FlightDTO>> updateFlight(
            @RequestBody Flight flight)
    {
        return new ResponseEntity<ResponseStructure<FlightDTO>>(
                flightService.updateFlightInDatabase(flight),
                HttpStatus.OK);
    }

    @DeleteMapping("/deleteFlight/{id}")
    public ResponseEntity<ResponseStructure<FlightDTO>> deleteFlight(
            @PathVariable("id") int flightId)
    {
        return new ResponseEntity<ResponseStructure<FlightDTO>>(
                flightService.deleteFlightByIdFromDatabase(flightId),
                HttpStatus.OK);
    }

    @GetMapping("/priceRange/{minPrice}/{maxPrice}")
    public ResponseEntity<ResponseStructure<List<FlightDTO>>> getFlightsWithinPriceRange(
            @PathVariable double minPrice,
            @PathVariable double maxPrice)
    {
        return new ResponseEntity<ResponseStructure<List<FlightDTO>>>(
                flightService.getFlightsWithinPriceRangeFromDatabase(
                        minPrice,
                        maxPrice),
                HttpStatus.FOUND);
    }

    @GetMapping("/cheapest/{source}/{destination}")
    public ResponseEntity<ResponseStructure<FlightDTO>> getCheapestFlight(
            @PathVariable String source,
            @PathVariable String destination)
    {
        return new ResponseEntity<ResponseStructure<FlightDTO>>(
                flightService.getCheapestFlightBetweenCitiesFromDatabase(
                        source,
                        destination),
                HttpStatus.FOUND);
    }

    @GetMapping("/availableSeats/{seats}")
    public ResponseEntity<ResponseStructure<List<FlightDTO>>> getFlightsHavingMoreThanAvailableSeats(
            @PathVariable int seats)
    {
        return new ResponseEntity<ResponseStructure<List<FlightDTO>>>(
                flightService.getFlightHavingMoreThanAvailableSeatsFromDatabase(
                        seats),
                HttpStatus.FOUND);
    }

    @GetMapping("/pagination/{pn}/{ps}/{field}")
    public ResponseEntity<ResponseStructure<Page<FlightDTO>>> getFlightsByPagination(
            @PathVariable Integer pn,
            @PathVariable Integer ps,
            @PathVariable String field)
    {
        return ResponseEntity.ok(
                flightService.getFlightByPaginationAndSorting(
                        pn,
                        ps,
                        field));
    }

}