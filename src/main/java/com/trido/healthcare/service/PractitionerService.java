package com.trido.healthcare.service;

import com.trido.healthcare.controller.dto.PractitionerDto;

import java.util.List;

public interface PractitionerService {
    List<PractitionerDto> getAllPractitioners();

    PractitionerDto getPractitionerById(String PractitionerId);

    PractitionerDto createPractitioner(PractitionerDto practitionerDto);

    PractitionerDto updatePractitioner(String practitionerId, PractitionerDto practitionerDto);

    boolean disablePractitioner(String PractitionerId);
}
