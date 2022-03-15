package com.trido.healthcare.util.search;

import com.trido.healthcare.entity.Appointment;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class AppointmentSearchSpecification implements Specification<Appointment> {
    private UUID patientId;
    private UUID practitionerId;
    private String status;
    private ZonedDateTime startTime;
    private Integer minutesDuration;
    private boolean isDeleted = false;

    @Override
    public Predicate toPredicate(Root<Appointment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (this.patientId != null) {
            predicates.add(criteriaBuilder.equal(root.get("patientId"), this.patientId));
        }
        if (this.practitionerId != null) {
            predicates.add(criteriaBuilder.equal(root.get("practitionerId"), this.practitionerId));
        }
        if (this.status != null) {
            predicates.add(criteriaBuilder.equal(root.get("status").as(String.class), this.status));
        }
        if (this.startTime != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), this.startTime));
        }
        if (this.minutesDuration != null) {
            predicates.add(criteriaBuilder.equal(root.get("minutesDuration"), this.minutesDuration));
        }
        predicates.add(criteriaBuilder.equal(root.get("isDeleted"), this.isDeleted));
        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
