package com.sajid.reader.datareader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.LinkedHashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class FindAverageTest {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	protected TestRestTemplate restTemplate;

	@AfterEach
	void clearData() {
		jdbcTemplate.execute("Delete from IOT_DATA");
	}

	@Test
	@DisplayName("Find Avg SP_O2 >> 200")
	void test1() {
		jdbcTemplate.execute("Insert into IOT_DATA(ID_DEVICE, EVENT_TIME, PROCESSED_TIME, SP_O2, BLOOD_PRESSURE, PULSE_RATE) values ('device-A', {ts '2022-05-15 11:11:11'}, {ts '2022-05-15 11:11:11'}, 80, 100, 30)");
		jdbcTemplate.execute("Insert into IOT_DATA(ID_DEVICE, EVENT_TIME, PROCESSED_TIME, SP_O2, BLOOD_PRESSURE, PULSE_RATE) values ('device-A', {ts '2022-05-15 11:11:12'}, {ts '2022-05-15 11:11:11'}, 87, 100, 30)");
		ResponseEntity<Object> responseEntity = restTemplate.exchange("/v1/iotdata/average/device-A?from=2022-05-15 11:10:00&to=2022-05-15 11:16:00&healthAttribute=SPO2", HttpMethod.GET, null, Object.class);
		Double avgSpO2 = (Double) responseEntity.getBody();
		assertNotNull(responseEntity.getBody());
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertEquals(avgSpO2, 83.5);
	}

	@Test
	@DisplayName("Find Avg BP >> 200")
	void test2() {
		jdbcTemplate.execute("Insert into IOT_DATA(ID_DEVICE, EVENT_TIME, PROCESSED_TIME, SP_O2, BLOOD_PRESSURE, PULSE_RATE) values ('device-A', {ts '2022-05-15 11:11:11'}, {ts '2022-05-15 11:11:11'}, 80, 132, 78)");
		jdbcTemplate.execute("Insert into IOT_DATA(ID_DEVICE, EVENT_TIME, PROCESSED_TIME, SP_O2, BLOOD_PRESSURE, PULSE_RATE) values ('device-A', {ts '2022-05-15 11:11:12'}, {ts '2022-05-15 11:11:11'}, 87, 146, 80)");
		ResponseEntity<Object> responseEntity = restTemplate.exchange("/v1/iotdata/average/device-A?from=2022-05-15 11:10:00&to=2022-05-15 11:16:00&healthAttribute=BLOOD_PRESSURE", HttpMethod.GET, null, Object.class);
		Double avgSpO2 = (Double) responseEntity.getBody();
		assertNotNull(responseEntity.getBody());
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertEquals(avgSpO2, 139.0);
	}

	@Test
	@DisplayName("Find Avg PULSE RATE >> 200")
	void test3() {
		jdbcTemplate.execute("Insert into IOT_DATA(ID_DEVICE, EVENT_TIME, PROCESSED_TIME, SP_O2, BLOOD_PRESSURE, PULSE_RATE) values ('device-A', {ts '2022-05-15 11:11:11'}, {ts '2022-05-15 11:11:11'}, 80, 132, 78)");
		jdbcTemplate.execute("Insert into IOT_DATA(ID_DEVICE, EVENT_TIME, PROCESSED_TIME, SP_O2, BLOOD_PRESSURE, PULSE_RATE) values ('device-A', {ts '2022-05-15 11:11:12'}, {ts '2022-05-15 11:11:11'}, 87, 146, 80)");
		ResponseEntity<Object> responseEntity = restTemplate.exchange("/v1/iotdata/average/device-A?from=2022-05-15 11:10:00&to=2022-05-15 11:16:00&healthAttribute=PULSE", HttpMethod.GET, null, Object.class);
		Double avgPulse = (Double) responseEntity.getBody();
		assertNotNull(responseEntity.getBody());
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertEquals(avgPulse, 79.0);
	}

	@Test
	@DisplayName("Find Max PULSE RATE When No Data Defined >> 200")
	void test4() {
		ResponseEntity<Object> responseEntity = restTemplate.exchange("/v1/iotdata/average/device-A?from=2022-05-15 11:10:00&to=2022-05-15 11:16:00&healthAttribute=PULSE", HttpMethod.GET, null, Object.class);
		assertNull(responseEntity.getBody());
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("Find Max PULSE RATE For Duration > 60 mins >> 406")
	void test5() {
		ResponseEntity<Object> responseEntity = restTemplate.exchange("/v1/iotdata/average/device-A?from=2022-05-15 11:10:00&to=2022-05-15 13:16:00&healthAttribute=PULSE", HttpMethod.GET, null, Object.class);
		assertNotNull(responseEntity.getBody());
		LinkedHashMap errorInfo = (LinkedHashMap) responseEntity.getBody();
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
		assertThat(errorInfo.get("errorCode")).isEqualTo("DURATION_LIMITATION");
	}

	@Test
	@DisplayName("Find Max Value with From after To >> 400")
	void test6() {
		ResponseEntity<Object> responseEntity = restTemplate.exchange("/v1/iotdata/average/device-A?from=2022-05-16 11:10:00&to=2022-05-15 13:16:00&healthAttribute=PULSE", HttpMethod.GET, null, Object.class);
		assertNotNull(responseEntity.getBody());
		LinkedHashMap errorInfo = (LinkedHashMap) responseEntity.getBody();
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(errorInfo.get("errorCode")).isEqualTo("INVALID_DATES");
	}
}
