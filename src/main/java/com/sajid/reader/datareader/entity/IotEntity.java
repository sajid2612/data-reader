package com.sajid.reader.datareader.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Entity
@Table(name = "IOT_DATA")
public class IotEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private String id;

	@Column(name = "ID_DEVICE")
	private String deviceId;

	@Column(name = "EVENT_TIME")
	private LocalDateTime eventTime;

	@Column(name = "PROCESSED_TIME")
	private LocalDateTime processedTime;

	@Column(name = "SP_O2")
	private Integer spO2;

	@Column(name = "BLOOD_PRESSURE")
	private Integer bp;

	@Column(name = "PULSE_RATE")
	private Integer pulseRate;

}
