package com.trido.healthcare.controller;

import com.trido.healthcare.controller.dto.PatientDto;
import com.trido.healthcare.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

@Controller
@RequestMapping("${api.prefix}")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping("/patients")
    public ResponseEntity getAllPatients() {
        List<PatientDto> patientDtoList = patientService.getAllPatients();
        return new ResponseEntity(patientDtoList, HttpStatus.OK);
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity getPatientById(@PathVariable("patientId") String patientId) {
        PatientDto patientDto = patientService.getPatientById(patientId);
        return new ResponseEntity(patientDto, HttpStatus.OK);
    }

    @PostMapping("/patients")
    public ResponseEntity createPatient(@RequestBody PatientDto patientDto) {
        PatientDto savedPatientDto = patientService.createPatient(patientDto);
        return new ResponseEntity(savedPatientDto, HttpStatus.OK);
    }

    @PutMapping("/patients/{patientId}")
    public ResponseEntity updatePatient(@PathVariable("patientId") String patientId, @RequestBody PatientDto patientDto) {
        PatientDto updatedPatientDto = patientService.updatePatient(patientId, patientDto);
        return new ResponseEntity(updatedPatientDto, HttpStatus.OK);
    }

    @DeleteMapping("/patients/{patientId}")
    public ResponseEntity disablePatient(@PathVariable("patientId") String patientId) {
        boolean result = patientService.disablePatient(patientId);
        return new ResponseEntity(result ? "User has been deleted" : "Fail to delete user", HttpStatus.OK);
    }

}
