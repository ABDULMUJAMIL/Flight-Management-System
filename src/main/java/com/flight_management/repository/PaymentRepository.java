package com.flight_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flight_management.dto.ModeOfPayment;
import com.flight_management.dto.PaymentStatus;
import com.flight_management.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer>
{

	//====================== GET PAYMENT BY STATUS ======================

	@Query("select p from Payment p where p.paymentStatus=?1")
	List<Payment> findPaymentByStatus(PaymentStatus paymentStatus);

	//====================== GET PAYMENT BY MODE ======================

	@Query("select p from Payment p where p.modeOfPayment=?1")
	List<Payment> findPaymentByModeOfPayment(ModeOfPayment modeOfPayment);

	//====================== TOTAL AMOUNT PAID ON A FLIGHT ======================

	@Query("select sum(b.payment.amount) from Booking b where b.flight.flightId=?1 and b.payment.paymentStatus=com.flight_management.dto.PaymentStatus.SUCCESS")
	Double getTotalAmountPaidOnFlight(int flightId);

}