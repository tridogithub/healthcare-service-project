package com.trido.healthcare.controller.mapper;

import com.trido.healthcare.controller.dto.PractitionerDto;
import com.trido.healthcare.entity.Practitioner;
import org.springframework.stereotype.Component;

@Component
public class PractitionerMapper {
    public Practitioner toEntity(PractitionerDto practitionerDto) {
        Practitioner practitioner = new Practitioner();
        practitioner.setGender(practitionerDto.getGender());
        practitioner.setFirstName(practitionerDto.getFirstName());
        practitioner.setLastName(practitionerDto.getLastName());
        practitioner.setBirthDate(practitionerDto.getBirthDate());
        practitioner.setExperience(practitionerDto.getExperience());
        practitioner.setEmail(practitionerDto.getEmail());
        return practitioner;
    }

    public PractitionerDto toDto(Practitioner practitioner) {
        PractitionerDto practitionerDto = new PractitionerDto();
        practitionerDto.setId(practitioner.getId());
        practitionerDto.setGender(practitioner.getGender());
        practitionerDto.setFirstName(practitioner.getFirstName());
        practitionerDto.setLastName(practitioner.getLastName());
        practitionerDto.setBirthDate(practitioner.getBirthDate());
        practitionerDto.setExperience(practitioner.getExperience());
        practitionerDto.setEmail(practitioner.getEmail());
        return practitionerDto;
    }
}
