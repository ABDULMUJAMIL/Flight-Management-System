package com.flight_management.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.flight_management.dto.BookingStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Booking 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int bookingId;
	@CreationTimestamp
	private LocalDateTime bookingDateTime;
	@Enumerated(EnumType.STRING)
	private BookingStatus status;
	
	 @ManyToOne
	 @JoinColumn(name = "flight_id")
	 private Flight flight;

	 @OneToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name = "payment_id")
	 private Payment payment;

	 @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
	 private List<Passenger> passengers;

		public int getBookingId() {
			return bookingId;
		}

		public void setBookingId(int bookingId) {
			this.bookingId = bookingId;
		}

		public LocalDateTime getBookingDateTime() {
			return bookingDateTime;
		}

		public void setBookingDateTime(LocalDateTime bookingDateTime) {
			this.bookingDateTime = bookingDateTime;
		}

		public BookingStatus getStatus() {
			return status;
		}

		public void setStatus(BookingStatus status) {
			this.status = status;
		}

		public Flight getFlight() {
			return flight;
		}

		public void setFlight(Flight flight) {
			this.flight = flight;
		}

		public Payment getPayment() {
			return payment;
		}

		public void setPayment(Payment payment) {
			this.payment = payment;
		}

		public List<Passenger> getPassengers() {
			return passengers;
		}

		public void setPassengers(List<Passenger> passengers) {
			this.passengers = passengers;
		}
	    
	    

}
