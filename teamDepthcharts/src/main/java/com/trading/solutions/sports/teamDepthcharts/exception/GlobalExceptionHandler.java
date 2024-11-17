package com.trading.solutions.sports.teamDepthcharts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.trading.solutions.sports.teamDepthcharts.pojo.CustomErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Handle HttpMessageNotReadableException (invalid JSON)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<CustomErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
		// Create a custom error response
		CustomErrorResponse errorResponse = new CustomErrorResponse("JSON input is not Valid",
				"The JSON body of the request is malformed or invalid.");

		// 400 Bad Request
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); 
	}

}
