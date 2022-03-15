package com.trido.healthcare.entity.enumm;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
@Getter
public enum AppointmentStatus {

    Accept("Accept", "Accepted appointment"),
    Reject("Reject", "Rejected appointment"),
    Pending("Pending", "Waiting for response");

    private final String code;
    private final String description;

    public static AppointmentStatus getStatusFromCode(String code) {
        if ("Accept".equals(code)) {
            return AppointmentStatus.Accept;
        } else if ("Reject".equals(code)) {
            return AppointmentStatus.Reject;
        } else if ("Pending".equals(code)) {
            return AppointmentStatus.Pending;
        } else {
            return null;
        }
    }
}
