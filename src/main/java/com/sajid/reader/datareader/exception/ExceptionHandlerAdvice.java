package com.sajid.reader.datareader.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerAdvice {

	@ExceptionHandler(ConversionFailedException.class)
	public ResponseEntity<ErrorInfo> conversionFailed(ConversionFailedException ex) {
		return new ResponseEntity<>(
				new ErrorInfo("INVALID_DATA_TYPE", ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorInfo> methodArgumentInvalid(MethodArgumentNotValidException ex) {
		return new ResponseEntity<>(
				new ErrorInfo("INVALID_REQUEST", ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).findAny().orElse(null)),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorInfo> constraintViolation(ConstraintViolationException ex) {
		return new ResponseEntity<>(
				new ErrorInfo("CONSTRAINT_VIOLATION", ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).distinct().findAny().orElse(null)),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidDateException.class)
	public ResponseEntity<ErrorInfo> invalidDates(InvalidDateException ex) {
		return new ResponseEntity<>(
				new ErrorInfo("INVALID_DATES", ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotAllowedDateException.class)
	public ResponseEntity<ErrorInfo> datesNotAllowed(NotAllowedDateException ex) {
		return new ResponseEntity<>(
				new ErrorInfo("DURATION_LIMITATION", ex.getMessage()),
				HttpStatus.NOT_ACCEPTABLE);
	}
}
