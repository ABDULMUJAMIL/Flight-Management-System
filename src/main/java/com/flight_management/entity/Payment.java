package com.flight_management.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.flight_management.dto.ModeOfPayment;
import com.flight_management.dto.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Payment 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int paymentId;
	@CreationTimestamp
	private LocalDateTime paymentDateTime;
	private double amount;
	@Enumerated(EnumType.STRING)
	private ModeOfPayment modeOfPayment;
	
    @Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;

	    @OneToOne(mappedBy = "payment")
	    private Booking booking;

		public int getPaymentId() {
			return paymentId;
		}

		public void setPaymentId(int paymentId) {
			this.paymentId = paymentId;
		}

		public LocalDateTime getPaymentDateTime() {
			return paymentDateTime;
		}

		public void setPaymentDateTime(LocalDateTime paymentDateTime) {
			this.paymentDateTime = paymentDateTime;
		}

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}

		public ModeOfPayment getModeOfPayment() {
			return modeOfPayment;
		}

		public void setModeOfPayment(ModeOfPayment modeOfPayment) {
			this.modeOfPayment = modeOfPayment;
		}

		public PaymentStatus getPaymentStatus() {
			return paymentStatus;
		}

		public void setPaymentStatus(PaymentStatus paymentStatus) {
			this.paymentStatus = paymentStatus;
		}

		public Booking getBooking() {
			return booking;
		}

		public void setBooking(Booking booking) {
			this.booking = booking;
		}
	    
	    
	    

}
