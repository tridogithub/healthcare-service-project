package com.trido.healthcare.service;

import com.trido.healthcare.controller.dto.PractitionerDto;
import com.trido.healthcare.domain.PractitionerFilter;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PractitionerService {
    List<PractitionerDto> getAllPractitioners();

    PractitionerDto getPractitionerById(UUID PractitionerId);

    PractitionerDto createPractitioner(PractitionerDto practitionerDto);

    PractitionerDto updatePractitioner(UUID practitionerId, PractitionerDto practitionerDto);

    boolean disablePractitioner(UUID practitionerId);

    boolean saveAvatarImage(MultipartFile file, UUID practitionerId);

    Resource loadAvatarImage(UUID practitionerId);

    boolean existsById(UUID practitionerId);

    List<PractitionerDto> searchPractitioners(PractitionerFilter practitionerFilter, Pageable pageable, List<String> sortValues);
}
