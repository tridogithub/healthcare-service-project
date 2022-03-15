package com.trido.healthcare.util.search;

import com.trido.healthcare.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class PatientSearchSpecification implements Specification<Patient> {
    private String gender;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private LocalDate birthDate;
    private boolean active = true;

    @Override
    public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (this.gender != null) {
            predicates.add(criteriaBuilder.equal(root.get("gender").as(String.class), gender));
        }
        if (this.firstName != null) {
            predicates.add(criteriaBuilder.like(root.get("firstName"), this.firstName + "%"));
        }
        if (this.lastName != null) {
            predicates.add(criteriaBuilder.like(root.get("lastName"), this.lastName + "%"));
        }
        if (this.phoneNumber != null) {
            predicates.add(criteriaBuilder.equal(root.get("phoneNumber"), this.phoneNumber));
        }
        if (this.birthDate != null) {
            predicates.add(criteriaBuilder.equal(root.get("birthDate"), this.birthDate));
        }
        if (this.email != null) {
            predicates.add(criteriaBuilder.like(root.get("email"), this.email + "%"));
        }
        predicates.add(criteriaBuilder.equal(root.get("active"), this.active));
        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
