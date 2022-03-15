package com.trido.healthcare.controller;

import com.trido.healthcare.controller.dto.PatientDto;
import com.trido.healthcare.domain.PatientFilter;
import com.trido.healthcare.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("${api.prefix}")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping("/patients")
    public ResponseEntity<?> getAllPatients(PatientFilter patientFilter,
                                            Pageable pageable,
                                            @Valid @Pattern(regexp = "[\\+|\\-][a-zA-Z]+") @RequestParam(required = false) List<String> sort
    ) {
        List<PatientDto> patientDtoList = patientService.searchPatients(patientFilter, pageable, sort);
        return new ResponseEntity<>(patientDtoList, HttpStatus.OK);
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<?> getPatientById(@PathVariable("patientId") UUID patientId) {
        PatientDto patientDto = patientService.getPatientById(patientId);
        return new ResponseEntity<>(patientDto, HttpStatus.OK);
    }

    @PostMapping("/patients")
    public ResponseEntity<?> createPatient(@RequestBody PatientDto patientDto) {
        PatientDto savedPatientDto = patientService.createPatient(patientDto);
        return new ResponseEntity<>(savedPatientDto, HttpStatus.OK);
    }

    @PutMapping("/patients/{patientId}")
    public ResponseEntity<?> updatePatient(@PathVariable("patientId") UUID patientId, @RequestBody PatientDto patientDto) {
        PatientDto updatedPatientDto = patientService.updatePatient(patientId, patientDto);
        return new ResponseEntity<>(updatedPatientDto, HttpStatus.OK);
    }

    @DeleteMapping("/patients/{patientId}")
    public ResponseEntity<?> disablePatient(@PathVariable("patientId") UUID patientId) {
        boolean result = patientService.disablePatient(patientId);
        return new ResponseEntity<>(result ? "User has been deleted" : "Fail to delete user", HttpStatus.OK);
    }

    @PostMapping("/patients/{patientId}/avatar")
    public ResponseEntity<?> uploadAvatarImage(@PathVariable("patientId") UUID patientId,
                                               @RequestParam("avatar") MultipartFile file) {
        patientService.saveAvatarImage(file, patientId);
        return new ResponseEntity<>("Successfully", HttpStatus.OK);
    }

    @GetMapping("/patients/{patientId}/avatar")
    public ResponseEntity<?> getAvatarImage(@PathVariable("patientId") UUID patientId) {
        Resource file = patientService.loadAvatarImage(patientId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        "inline"
                        "attachment; filename=\"" + file.getFilename() + "\""
                ).body(file);
    }

}
