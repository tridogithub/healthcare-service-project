package com.trido.healthcare.config.utils;

import com.trido.healthcare.config.auth.BearerContextHolder;
import com.trido.healthcare.constants.Constants;
import com.trido.healthcare.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebAuthorization {
    private String currentUserId = null;

    @Autowired
    private AppointmentService appointmentService;

    public boolean checkUserWithIdAuthorization(HttpServletRequest request, String userId) {
        //antMatchers: /user/{userId}
        currentUserId = BearerContextHolder.getContext().getUserId();
        if (!Constants.ADMIN_ROLE.equalsIgnoreCase(BearerContextHolder.getContext().getRoleName())) {
            return request.getMethod().equals(HttpMethod.GET.name()) && currentUserId.equals(userId);
        }
        return true;
    }

    public boolean checkPractitionerWithIdAuthorization(HttpServletRequest request, String practitionerId) {
        currentUserId = BearerContextHolder.getContext().getUserId();
        if (!Constants.PRACTITIONER_ROLE.equalsIgnoreCase(BearerContextHolder.getContext().getRoleName())) {
            return (request.getMethod().equals(HttpMethod.GET.name()) || request.getMethod().equals(HttpMethod.PUT.name()))
                    && currentUserId.equals(practitionerId);
        }
        return true;
    }

    public boolean checkPatientWithIdAuthorization(HttpServletRequest request, String patientId) {
        currentUserId = BearerContextHolder.getContext().getUserId();
        if (!Constants.PATIENT_ROLE.equalsIgnoreCase(BearerContextHolder.getContext().getRoleName())) {
            return (request.getMethod().equals(HttpMethod.GET.name()) || request.getMethod().equals(HttpMethod.PUT.name()))
                    && currentUserId.equals(patientId);
        }
        return true;
    }

    public boolean checkAppointmentWithIdAuthorization(HttpServletRequest request, String appointmentId) {
        currentUserId = BearerContextHolder.getContext().getUserId();
        if (request.getMethod().equals(HttpMethod.GET.name()) || request.getMethod().equals(HttpMethod.PUT.name())) {
            if (!(Constants.PATIENT_ROLE.equalsIgnoreCase(BearerContextHolder.getContext().getRoleName())
                    && appointmentService.existsByPatientId(currentUserId)
            )) {
                return false;
            }
            return Constants.PRACTITIONER_ROLE.equalsIgnoreCase(BearerContextHolder.getContext().getRoleName())
                    && appointmentService.existsByPractitionerId(currentUserId);
        }
        return true;
    }
}
