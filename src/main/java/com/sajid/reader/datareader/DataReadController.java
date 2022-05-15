package com.sajid.reader.datareader;

import com.sajid.reader.datareader.constt.HealthAttribute;
import com.sajid.reader.datareader.model.IotData;
import com.sajid.reader.datareader.service.DataReaderService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/iotdata")
@RequiredArgsConstructor
public class DataReadController {

	private final DataReaderService dataReaderService;

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, List<IotData>> findAllDeviceInfo(
			@RequestParam(value = "from")  LocalDateTime fromDateTime,
			@RequestParam(value = "to")  LocalDateTime toDateTime
	) {
		return dataReaderService.findAllDeviceInfoDuring(fromDateTime, toDateTime);
	}

	@GetMapping(value = "/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IotData> findInfoForDevice(@PathVariable("deviceId") String deviceId,
			@RequestParam(value = "from") LocalDateTime fromDateTime,
			@RequestParam(value = "to") LocalDateTime toDateTime
	) {
		return dataReaderService.findForDevice(fromDateTime, toDateTime, deviceId);
	}

	@GetMapping(value = "/average/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Float> findAverageConditionForDevice(@PathVariable("deviceId") String deviceId,
			@RequestParam(value = "from") LocalDateTime fromDateTime,
			@RequestParam(value = "to") LocalDateTime toDateTime,
			@RequestParam(value = "healthAttribute") HealthAttribute healthAttribute
	) {
		return ResponseEntity.ok(dataReaderService.findAverage(fromDateTime, toDateTime, deviceId, healthAttribute));
	}

	@GetMapping(value = "/min/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> findMinConditionForDevice(@PathVariable("deviceId") String deviceId,
			@RequestParam(value = "from") LocalDateTime fromDateTime,
			@RequestParam(value = "to") LocalDateTime toDateTime,
			@RequestParam(value = "healthAttribute") HealthAttribute healthAttribute
	) {
		return ResponseEntity.ok(dataReaderService.findMin(fromDateTime, toDateTime, deviceId, healthAttribute));
	}

	@GetMapping(value = "/max/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findMaxConditionForDevice(@PathVariable("deviceId") String deviceId,
			@RequestParam(value = "from") LocalDateTime fromDateTime,
			@RequestParam(value = "to") LocalDateTime toDateTime,
			@RequestParam(value = "healthAttribute") HealthAttribute healthAttribute
	) {
		return ResponseEntity.ok(dataReaderService.findMax(fromDateTime, toDateTime, deviceId, healthAttribute));
	}

	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IotData> searchStatus(
			@RequestParam(value = "from") LocalDateTime fromDateTime,
			@RequestParam(value = "to") LocalDateTime toDateTime,
			@RequestParam(value = "filter.device", required = false) String deviceId ,
			@RequestParam(value = "filter.spo2", required = false) Integer spO2Condition,
			@RequestParam(value = "filter.bp", required = false) Integer bpCondition,
			@RequestParam(value = "filter.pulse", required = false) Integer pulseCondition
	) {
		return dataReaderService.searchByDynamicCondition(fromDateTime, toDateTime, deviceId, spO2Condition, pulseCondition, bpCondition);
	}
}
