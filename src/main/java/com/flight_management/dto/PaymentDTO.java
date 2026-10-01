package com.flight_management.dto;

import java.time.LocalDateTime;

public class PaymentDTO
{
	private int paymentId;
	private LocalDateTime paymentDateTime;
	private double amount;
	private ModeOfPayment modeOfPayment;
	private PaymentStatus paymentStatus;

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
}