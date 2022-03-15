package com.trido.healthcare.repository;

import com.trido.healthcare.entity.Practitioner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PractitionerRepository extends JpaRepository<Practitioner, UUID>, JpaSpecificationExecutor<Practitioner> {
    boolean existsByEmail(String email);

    List<Practitioner> findAllByActive(boolean isActive);

    Optional<Practitioner> findByIdAndActive(UUID id, boolean isActive);

    boolean existsByIdAndActive(UUID id, boolean isActive);
}
