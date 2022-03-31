package com.trido.healthcare.controller;

import com.trido.healthcare.controller.dto.PractitionerDto;
import com.trido.healthcare.domain.PractitionerFilter;
import com.trido.healthcare.service.PractitionerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("${api.prefix}")
@AllArgsConstructor
public class PractitionerController {
    private final PractitionerService practitionerService;

    @GetMapping("/practitioners")
    public ResponseEntity<?> getAllPractitioners(PractitionerFilter practitionerFilter,
                                                 Pageable pageable,
                                                 @Valid @Pattern(regexp = "[+|\\-][a-zA-Z]+") @RequestParam(required = false) List<String> sort) {
        List<PractitionerDto> practitionerDtoList = practitionerService.searchPractitioners(practitionerFilter, pageable, sort);
        return new ResponseEntity<>(practitionerDtoList, HttpStatus.OK);
    }

    @GetMapping("/practitioners/{practitionerId}")
    public ResponseEntity<?> getPractitionerById(@PathVariable("practitionerId") UUID practitionerId) {
        PractitionerDto practitionerDto = practitionerService.getPractitionerById(practitionerId);
        return new ResponseEntity<>(practitionerDto, HttpStatus.OK);
    }

    @PostMapping("/practitioners")
    public ResponseEntity<?> createPractitioners(@RequestBody PractitionerDto practitionerDto) {
        PractitionerDto savedPractitionerDto = practitionerService.createPractitioner(practitionerDto);
        return new ResponseEntity<>(savedPractitionerDto, HttpStatus.OK);
    }

    @PutMapping("/practitioners/{practitionerId}")
    public ResponseEntity<?> updatePractitioners(@PathVariable("practitionerId") UUID practitionerId, @RequestBody PractitionerDto practitionerDto) {
        PractitionerDto updatedPractitionerDto = practitionerService.updatePractitioner(practitionerId, practitionerDto);
        return new ResponseEntity<>(updatedPractitionerDto, HttpStatus.OK);
    }

    @DeleteMapping("/practitioners/{practitionerId}")
    public ResponseEntity<?> disablePractitioners(@PathVariable("practitionerId") UUID practitionerId) {
        boolean result = practitionerService.disablePractitioner(practitionerId);
        return new ResponseEntity<>(result ? "User has been deleted" : "Fail to delete user", HttpStatus.OK);
    }
}
