package com.flight_management.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flight_management.dto.ModeOfPayment;
import com.flight_management.dto.PaymentDTO;
import com.flight_management.dto.PaymentStatus;
import com.flight_management.dto.ResponseStructure;
import com.flight_management.entity.Payment;
import com.flight_management.exception.IdNotFoundException;
import com.flight_management.exception.InvalidInputException;
import com.flight_management.exception.NoRecordFoundException;
import com.flight_management.repository.FlightRepository;
import com.flight_management.repository.PaymentRepository;

@Service
public class PaymentService 
{
	@Autowired
	private PaymentRepository paymentRepo;

	@Autowired
	private FlightRepository flightRepo;
	
	public static PaymentDTO convertPaymentToPaymentDTO(Payment p)
	{
		PaymentDTO dto = new PaymentDTO();

		dto.setPaymentId(p.getPaymentId());
		dto.setPaymentDateTime(p.getPaymentDateTime());
		dto.setAmount(p.getAmount());
		dto.setModeOfPayment(p.getModeOfPayment());
		dto.setPaymentStatus(p.getPaymentStatus());

		return dto;
	}
	
	@Transactional
	public ResponseStructure<PaymentDTO> createPaymentInDatabase(Payment payment)
	{

	    if(payment.getModeOfPayment() == null)
	    {
	        throw new InvalidInputException("Please select payment mode.");
	    }

	    if(payment.getPaymentStatus() == null)
	    {
	        payment.setPaymentStatus(PaymentStatus.PENDING);
	    }

	    // Booking will calculate the amount
	    payment.setAmount(0);

	    Payment savedPayment = paymentRepo.save(payment);

	    ResponseStructure<PaymentDTO> res = new ResponseStructure<>();

	    res.setData(convertPaymentToPaymentDTO(savedPayment));
	    res.setMessage("Payment created successfully.");
	    res.setStatusCode(HttpStatus.CREATED.value());

	    return res;
	}
	
	public ResponseStructure<List<PaymentDTO>> getAllPaymentsFromDatabase()
	{

		List<Payment> li =
				paymentRepo.findAll();

		ArrayList<PaymentDTO> dto =
				new ArrayList<>();

		for(Payment p : li)
		{
			dto.add(convertPaymentToPaymentDTO(p));
		}

		ResponseStructure<List<PaymentDTO>> res =
				new ResponseStructure<>();

		res.setData(dto);
		res.setMessage(li.size()+" records found.");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;

	}
	
	
	public ResponseStructure<PaymentDTO> getPaymentByIdFromDatabase(int paymentId)
	{

		Optional<Payment> opt =
				paymentRepo.findById(paymentId);

		if(opt.isEmpty())
		{
			throw new NoRecordFoundException(
					"Payment doesn't exist with Id : "
					+paymentId);
		}

		ResponseStructure<PaymentDTO> res =
				new ResponseStructure<>();

		res.setData(
				convertPaymentToPaymentDTO(opt.get()));

		res.setMessage("Record found.");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;

	}
	
	
	@Transactional
	public ResponseStructure<PaymentDTO> updatePaymentStatusInDatabase(Payment payment)
	{

		if(payment.getPaymentId()==0)
		{
			throw new IdNotFoundException(
					"Payment Id must be provided.");
		}

		if(payment.getPaymentStatus()==null)
		{
			throw new InvalidInputException(
					"Payment Status cannot be empty.");
		}

		Optional<Payment> opt =
				paymentRepo.findById(
						payment.getPaymentId());

		if(opt.isEmpty())
		{
			throw new NoRecordFoundException(
					"Payment not found.");
		}

		Payment fetchedPayment = opt.get();

		fetchedPayment.setPaymentStatus(
				payment.getPaymentStatus());

		Payment updatedPayment =
				paymentRepo.save(fetchedPayment);

		ResponseStructure<PaymentDTO> res =
				new ResponseStructure<>();

		res.setData(
				convertPaymentToPaymentDTO(updatedPayment));

		res.setMessage("Payment Status updated successfully.");
		res.setStatusCode(HttpStatus.OK.value());

		return res;

	}
	
	
	public ResponseStructure<List<PaymentDTO>> getPaymentByStatusFromDatabase(PaymentStatus status)
	{

		List<Payment> li =
				paymentRepo.findPaymentByStatus(status);

		if(li.isEmpty())
		{
			throw new NoRecordFoundException(
					"No Payments found with status : "
					+status);
		}

		ArrayList<PaymentDTO> dto =
				new ArrayList<>();

		for(Payment p : li)
		{
			dto.add(convertPaymentToPaymentDTO(p));
		}

		ResponseStructure<List<PaymentDTO>> res =
				new ResponseStructure<>();

		res.setData(dto);
		res.setMessage(li.size()+" records found.");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;

	}
	
	
	public ResponseStructure<List<PaymentDTO>> getPaymentByModeOfPaymentFromDatabase(ModeOfPayment mode)
	{

		List<Payment> li =
				paymentRepo.findPaymentByModeOfPayment(mode);

		if(li.isEmpty())
		{
			throw new NoRecordFoundException(
					"No Payments found with Mode : "
					+mode);
		}

		ArrayList<PaymentDTO> dto =
				new ArrayList<>();

		for(Payment p : li)
		{
			dto.add(convertPaymentToPaymentDTO(p));
		}

		ResponseStructure<List<PaymentDTO>> res =
				new ResponseStructure<>();

		res.setData(dto);
		res.setMessage(li.size()+" records found.");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;

	}
	
	
	public ResponseStructure<Double> getTotalAmountPaidOnFlight(int flightId)
	{

		if(flightRepo.findById(flightId).isEmpty())
		{
			throw new NoRecordFoundException(
					"Flight doesn't exist.");
		}

		Double total =
				paymentRepo.getTotalAmountPaidOnFlight(flightId);

		if(total==null)
		{
			total=0.0;
		}

		ResponseStructure<Double> res =
				new ResponseStructure<>();

		res.setData(total);
		res.setMessage("Total Amount Paid.");
		res.setStatusCode(HttpStatus.FOUND.value());

		return res;

	}
	
	

}
