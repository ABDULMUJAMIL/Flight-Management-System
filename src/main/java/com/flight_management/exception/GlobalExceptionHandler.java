package com.flight_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.flight_management.dto.ResponseStructure;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler
{
	@ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ResponseStructure<String>> handleIIE(InvalidInputException exception)
    {
        ResponseStructure<String> res = new ResponseStructure<>();

        res.setStatusCode(HttpStatus.CONFLICT.value());
        res.setMessage(exception.getMessage());
        res.setData(null);

        return new ResponseEntity<>(res, HttpStatus.CONFLICT);
    }
	
	@ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> handleINFE(IdNotFoundException exception)
    {
        ResponseStructure<String> res = new ResponseStructure<>();

        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setMessage(exception.getMessage());
        res.setData("Failure");

        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(NoRecordFoundException.class)
    public ResponseEntity<ResponseStructure<String>> handleNRAE(NoRecordFoundException exception)
    {
        ResponseStructure<String> res = new ResponseStructure<>();

        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setMessage(exception.getMessage());
        res.setData("Failure");

        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }
	
}
