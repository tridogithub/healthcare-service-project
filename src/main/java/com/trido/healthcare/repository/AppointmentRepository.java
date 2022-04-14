package com.trido.healthcare.repository;

import com.trido.healthcare.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID>, JpaSpecificationExecutor<Appointment> {
    boolean existsByIdAndIsDeleted(UUID uuid, boolean isDeleted);

    Appointment findByIdAndIsDeleted(UUID appointmentId, boolean isDeleted);

    boolean existsByPatientId(UUID patientId);

    boolean existsByPractitionerId(UUID practitionerId);

    boolean existsByPatientIdAndId(UUID patientId, UUID id);

    boolean existsByPractitionerIdAndId(UUID practitionerId, UUID id);
}
