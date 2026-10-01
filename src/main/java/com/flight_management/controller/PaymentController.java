package com.flight_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flight_management.dto.ModeOfPayment;
import com.flight_management.dto.PaymentDTO;
import com.flight_management.dto.PaymentStatus;
import com.flight_management.entity.Payment;
import com.flight_management.dto.ResponseStructure;
import com.flight_management.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController
{

	@Autowired
	private PaymentService paymentService;

//	//====================== CREATE PAYMENT ======================
//
//	@PostMapping("/add")
//	public ResponseEntity<ResponseStructure<PaymentDTO>> createPayment(
//			@RequestBody Payment payment)
//	{
//		return new ResponseEntity<>(
//				paymentService.createPaymentInDatabase(payment),
//				HttpStatus.CREATED);
//	}

	//====================== GET ALL PAYMENTS ======================

	@GetMapping("/")
	public ResponseEntity<ResponseStructure<List<PaymentDTO>>> getAllPayments()
	{
		return new ResponseEntity<>(
				paymentService.getAllPaymentsFromDatabase(),
				HttpStatus.FOUND);
	}

	//====================== GET PAYMENT BY ID ======================

	@GetMapping("/id/{paymentId}")
	public ResponseEntity<ResponseStructure<PaymentDTO>> getPaymentById(
			@PathVariable int paymentId)
	{
		return new ResponseEntity<>(
				paymentService.getPaymentByIdFromDatabase(paymentId),
				HttpStatus.FOUND);
	}

	//====================== UPDATE PAYMENT STATUS ======================

	@PatchMapping("/updateStatus")
	public ResponseEntity<ResponseStructure<PaymentDTO>> updatePaymentStatus(
			@RequestBody Payment payment)
	{
		return new ResponseEntity<>(
				paymentService.updatePaymentStatusInDatabase(payment),
				HttpStatus.OK);
	}

	//====================== GET PAYMENT BY STATUS ======================

	@GetMapping("/status/{status}")
	public ResponseEntity<ResponseStructure<List<PaymentDTO>>> getPaymentByStatus(
			@PathVariable PaymentStatus status)
	{
		return new ResponseEntity<>(
				paymentService.getPaymentByStatusFromDatabase(status),
				HttpStatus.FOUND);
	}

	//====================== GET PAYMENT BY MODE OF PAYMENT ======================

	@GetMapping("/mode/{mode}")
	public ResponseEntity<ResponseStructure<List<PaymentDTO>>> getPaymentByMode(
			@PathVariable ModeOfPayment mode)
	{
		return new ResponseEntity<>(
				paymentService.getPaymentByModeOfPaymentFromDatabase(mode),
				HttpStatus.FOUND);
	}

	//====================== GET TOTAL AMOUNT PAID ON FLIGHT ======================

	@GetMapping("/totalAmount/{flightId}")
	public ResponseEntity<ResponseStructure<Double>> getTotalAmountPaidOnFlight(
			@PathVariable int flightId)
	{
		return new ResponseEntity<>(
				paymentService.getTotalAmountPaidOnFlight(flightId),
				HttpStatus.FOUND);
	}

}