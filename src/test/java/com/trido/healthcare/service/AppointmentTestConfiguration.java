package com.trido.healthcare.service;

import com.trido.healthcare.controller.mapper.AppointmentMapper;
import com.trido.healthcare.repository.AppointmentRepository;
import com.trido.healthcare.service.impl.AppointmentServiceImpl;
import org.springframework.context.annotation.Bean;

@org.springframework.boot.test.context.TestConfiguration
public class AppointmentTestConfiguration {
//    @Bean
//    public AppointmentService appointmentService() {
//        return new AppointmentServiceImpl();
//    }

    @Bean
    public AppointmentMapper appointmentMapper() {
        return new AppointmentMapper();
    }
}
