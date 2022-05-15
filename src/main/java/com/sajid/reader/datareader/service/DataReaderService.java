package com.sajid.reader.datareader.service;


import com.sajid.reader.datareader.constt.HealthAttribute;
import com.sajid.reader.datareader.repository.IotDataRepository;
import com.sajid.reader.datareader.repository.SpecificationGenerator;
import com.sajid.reader.datareader.entity.IotEntity;
import com.sajid.reader.datareader.model.IotData;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DataReaderService {
	private final IotDataRepository iotDataRepository;
	private final SpecificationGenerator specificationGenerator;
	private final ValidationService validationService;

	@Transactional(readOnly = true)
	public Map<String, List<IotData>> findAllDeviceInfoDuring(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
		validationService.validateRequestParams(fromDateTime, toDateTime);
		return iotDataRepository.findByEventTimeAfterAndEventTimeBefore(fromDateTime, toDateTime)
				.stream()
				.map(this::of)
				.sorted(Comparator.comparing(IotData::getTimestamp))
				.collect(Collectors.groupingBy(IotData::getDeviceId));
	}

	@Transactional(readOnly = true)
	public List<IotData> findForDevice(LocalDateTime fromDateTime, LocalDateTime toDateTime, String deviceId) {
		validationService.validateRequestParams(fromDateTime, toDateTime);
		return iotDataRepository.findByDeviceIdAndEventTimeAfterAndEventTimeBefore(deviceId, fromDateTime, toDateTime)
				.stream()
				.map(this::of)
				.sorted(Comparator.comparing(IotData::getTimestamp))
				.collect(Collectors.toList());
	}

	public Float findAverage(LocalDateTime fromDateTime, LocalDateTime toDateTime, String deviceId, HealthAttribute healthAttribute) {
		validationService.validateRequestParams(fromDateTime, toDateTime);
		switch (healthAttribute) {
			case BLOOD_PRESSURE:
				return iotDataRepository.findAverageBp(deviceId, fromDateTime, toDateTime);
			case PULSE:
				return iotDataRepository.findAveragePulse(deviceId, fromDateTime, toDateTime);
			case SPO2:
				return iotDataRepository.findAverageSpO2(deviceId, fromDateTime, toDateTime);
			default:
				return null;
		}
	}

	public Integer findMin(LocalDateTime fromDateTime, LocalDateTime toDateTime, String deviceId, HealthAttribute healthAttribute) {
		validationService.validateRequestParams(fromDateTime, toDateTime);
		switch (healthAttribute) {
			case BLOOD_PRESSURE:
				return iotDataRepository.findMinBp(deviceId, fromDateTime, toDateTime);
			case PULSE:
				return iotDataRepository.findMinPulse(deviceId, fromDateTime, toDateTime);
			case SPO2:
				return iotDataRepository.findMinSpO2(deviceId, fromDateTime, toDateTime);
			default:
				return null;
		}
	}

	public Integer findMax(LocalDateTime fromDateTime, LocalDateTime toDateTime, String deviceId, HealthAttribute healthAttribute) {
		validationService.validateRequestParams(fromDateTime, toDateTime);
		switch (healthAttribute) {
			case BLOOD_PRESSURE:
				return iotDataRepository.findMaxBp(deviceId, fromDateTime, toDateTime);
			case PULSE:
				return iotDataRepository.findMaxPulse(deviceId, fromDateTime, toDateTime);
			case SPO2:
				return iotDataRepository.findMaxSpO2(deviceId, fromDateTime, toDateTime);
			default:
				return null;
		}
	}

	public List<IotData> searchByDynamicCondition(LocalDateTime fromDateTime, LocalDateTime toDateTime, String deviceId, Integer spO2Condition, Integer pulseCondition, Integer bpCondition) {
		validationService.validateRequestParams(fromDateTime, toDateTime);
		Specification<IotEntity> iotEntitySpecification = specificationGenerator.generate(fromDateTime, toDateTime, deviceId, spO2Condition, pulseCondition, bpCondition);
		return iotDataRepository.findAll(iotEntitySpecification)
				.stream()
				.map(this::of)
				.sorted(Comparator.comparing(IotData::getTimestamp))
				.collect(Collectors.toList());
	}

	private IotData of(IotEntity iotEntity) {
		IotData iotData = new IotData();
		iotData.setDeviceId(iotEntity.getDeviceId());
		iotData.setTimestamp(iotEntity.getEventTime());
		iotData.setBloodPressure(iotEntity.getBp());
		iotData.setPulseRate(iotEntity.getPulseRate());
		iotData.setSpO2(iotEntity.getSpO2());
		return iotData;
	}
}
