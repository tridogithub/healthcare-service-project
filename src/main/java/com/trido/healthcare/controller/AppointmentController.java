package com.trido.healthcare.controller;

import com.trido.healthcare.controller.dto.AppointmentDto;
import com.trido.healthcare.controller.dto.AppointmentUpdateDto;
import com.trido.healthcare.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("${api.prefix}")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<?> getAppointmentById(@PathVariable("appointmentId") UUID appointmentId) {
        AppointmentDto appointmentDto = appointmentService.findAppointmentById(appointmentId);
        return new ResponseEntity<>(appointmentDto, HttpStatus.OK);
    }

    @PostMapping("/appointments")
    public ResponseEntity<?> addNewAppointment(@RequestBody AppointmentDto appointmentDto) {
        AppointmentDto savedAppointment = appointmentService.addAppointment(appointmentDto);
        return new ResponseEntity<>(savedAppointment, HttpStatus.OK);
    }

    @PutMapping("/appointments/{appointmentId}")
    public ResponseEntity<?> disableAppointment(@PathVariable("appointmentId") UUID appointmentId,
                                                @RequestBody AppointmentUpdateDto appointmentUpdateDto) {
        AppointmentDto appointmentDto = appointmentService.updateAppointment(appointmentId, appointmentUpdateDto);
        return new ResponseEntity<>(appointmentDto, HttpStatus.OK);
    }

    @DeleteMapping("/appointments/{appointmentId}")
    public ResponseEntity<?> disableAppointment(@PathVariable("appointmentId") UUID appointmentId) {
        appointmentService.disableAppointment(appointmentId);
        return new ResponseEntity<>("Successfully", HttpStatus.OK);
    }

    @GetMapping("/appointments")
    public ResponseEntity<?> searchAppointments(@RequestParam(required = false) UUID patientId,
                                                @RequestParam(required = false) UUID practitionerId,
                                                @RequestParam(required = false) String status,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startTime,
                                                @RequestParam(required = false) Integer minutesDuration,
                                                Pageable pageable,
                                                @Valid @Pattern(regexp = "[+|\\-][a-zA-Z]+") @RequestParam(required = false) List<String> sort
    ) {
        List<AppointmentDto> appointmentDtoList =
                appointmentService.searchAppointments(patientId, practitionerId, status, startTime, minutesDuration, pageable, sort);
        return new ResponseEntity<>(appointmentDtoList, HttpStatus.OK);
    }
}
