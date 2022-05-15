package com.sajid.reader.datareader.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class IotData {
	private String deviceId;

	@Schema(name = "Event date time", example = "2022-05-14 23:11:10")
	private LocalDateTime timestamp;

	private int spO2;
	private int pulseRate;
	private int bloodPressure;
}