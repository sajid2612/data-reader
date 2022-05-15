package com.sajid.reader.datareader.service;

import com.sajid.reader.datareader.exception.InvalidDateException;
import com.sajid.reader.datareader.exception.NotAllowedDateException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

	void validateRequestParams(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
		validateDates(fromDateTime, toDateTime);
		allowedDurationPerRequest(fromDateTime, toDateTime);
	}

	private void validateDates(LocalDateTime from, LocalDateTime to) {
		if (from.isAfter(to)) {
			throw new InvalidDateException("Incorrect duration, from should be before to");
		}
	}

	private void allowedDurationPerRequest(LocalDateTime from, LocalDateTime to) {
		if (ChronoUnit.MINUTES.between(from, to) > 60 ) {
			throw new NotAllowedDateException("Currently only duration of 60 minutes or less is supported");
		}
	}


}
