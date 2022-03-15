package com.trido.healthcare.repository;

import com.trido.healthcare.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID>, JpaSpecificationExecutor<Patient> {
    List<Patient> findAllByActive(boolean isDelete);

    Patient findByIdAndActive(UUID id, boolean isDeleted);

    boolean existsByEmail(String email);

    boolean existsByIdAndActive(UUID patientId, boolean isActive);
}
