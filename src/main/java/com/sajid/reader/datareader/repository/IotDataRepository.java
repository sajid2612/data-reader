package com.sajid.reader.datareader.repository;

import com.sajid.reader.datareader.entity.IotEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface IotDataRepository extends JpaRepository<IotEntity, String> , JpaSpecificationExecutor<IotEntity> {
	List<IotEntity> findByEventTimeAfterAndEventTimeBefore(LocalDateTime from, LocalDateTime to);

	List<IotEntity> findByDeviceIdAndEventTimeAfterAndEventTimeBefore(String deviceId, LocalDateTime from, LocalDateTime to);

	@Query("SELECT max(bp) FROM IotEntity WHERE deviceId = ?1 and eventTime > ?2 and eventTime <= ?3")
	Integer findMaxBp(@Param("deviceId") String deviceId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT min(bp) FROM IotEntity WHERE deviceId = ?1 and eventTime > ?2 and eventTime <= ?3")
	Integer findMinBp(@Param("deviceId") String deviceId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT avg(bp) FROM IotEntity WHERE deviceId = ?1 and eventTime > ?2 and eventTime <= ?3")
	Float findAverageBp(@Param("deviceId") String deviceId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


	@Query("SELECT max(pulseRate) FROM IotEntity WHERE deviceId = ?1 and eventTime > ?2 and eventTime <= ?3")
	Integer findMaxPulse(@Param("deviceId") String deviceId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT min(pulseRate) FROM IotEntity WHERE deviceId = ?1 and eventTime > ?2 and eventTime <= ?3")
	Integer findMinPulse(@Param("deviceId") String deviceId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT avg(pulseRate) FROM IotEntity WHERE deviceId = ?1 and eventTime > ?2 and eventTime <= ?3")
	Float findAveragePulse(@Param("deviceId") String deviceId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT max(spO2) FROM IotEntity WHERE deviceId = ?1 and eventTime > ?2 and eventTime <= ?3")
	Integer findMaxSpO2(@Param("deviceId") String deviceId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT min(spO2) FROM IotEntity WHERE deviceId = ?1 and eventTime > ?2 and eventTime <= ?3")
	Integer findMinSpO2(@Param("deviceId") String deviceId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT avg(spO2) FROM IotEntity WHERE deviceId = ?1 and eventTime > ?2 and eventTime <= ?3")
	Float findAverageSpO2(@Param("deviceId") String deviceId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	List<IotEntity> findAll(Specification<IotEntity> spec);
}
