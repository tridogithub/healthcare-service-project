package com.trido.healthcare.repository;

import com.trido.healthcare.entity.Practitioner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PractitionerRepository extends JpaRepository<Practitioner, UUID> {
    boolean existsByEmail(String email);

    List<Practitioner> findAllByActive(boolean isActive);

    Optional<Practitioner> findByIdAndActive(UUID id, boolean isActive);
}
