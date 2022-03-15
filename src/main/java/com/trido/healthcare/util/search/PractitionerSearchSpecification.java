package com.trido.healthcare.util.search;

import com.trido.healthcare.entity.Practitioner;
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

@Getter
@AllArgsConstructor
public class PractitionerSearchSpecification implements Specification<Practitioner> {
    private String gender;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Integer experience;
    private String email;
    private boolean active = true;

    @Override
    public Predicate toPredicate(Root<Practitioner> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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
        if (this.birthDate != null) {
            predicates.add(criteriaBuilder.equal(root.get("birthDate"), this.birthDate));
        }
        if (this.experience != null) {
            predicates.add(criteriaBuilder.equal(root.get("experience"), this.experience));
        }
        if (this.email != null) {
            predicates.add(criteriaBuilder.like(root.get("email"), this.email + "%"));
        }
        predicates.add(criteriaBuilder.equal(root.get("active"), this.active));
        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
