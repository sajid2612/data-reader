package com.sajid.reader.datareader.repository;

import com.sajid.reader.datareader.entity.IotEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SpecificationGenerator {

	public Specification<IotEntity> generate(LocalDateTime from, LocalDateTime to, String deviceId, Integer spO2Condition, Integer pulseCondition, Integer bpCondition){
		return (root, criteriaQuery, criteriaBuilder) -> {
			List<Predicate> predicates = generatePredicates(from, to, deviceId, spO2Condition, pulseCondition, bpCondition, root, criteriaBuilder);
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

	private List<Predicate> generatePredicates(LocalDateTime from, LocalDateTime to, String deviceId, Integer spO2Condition, Integer pulseCondition, Integer bpCondition, Root<IotEntity> root, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventTime"), from));
		predicates.add(criteriaBuilder.lessThan(root.get("eventTime"), to));
		if (Objects.nonNull(deviceId)) {
			Predicate p = criteriaBuilder.equal(root.get("deviceId"), deviceId);
			predicates.add(p);
		}
		if (Objects.nonNull(spO2Condition)) {
			Predicate p = criteriaBuilder.greaterThanOrEqualTo(root.get("spO2"), spO2Condition);
			predicates.add(p);
		}

		if (Objects.nonNull(pulseCondition)) {
			Predicate p = criteriaBuilder.greaterThanOrEqualTo(root.get("pulseRate"), pulseCondition);
			predicates.add(p);
		}

		if (Objects.nonNull(bpCondition)) {
			Predicate p = criteriaBuilder.greaterThanOrEqualTo(root.get("bp"), bpCondition);
			predicates.add(p);
		}
		return predicates;
	}
}
