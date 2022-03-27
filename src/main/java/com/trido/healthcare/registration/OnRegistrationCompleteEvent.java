package com.trido.healthcare.registration;

import com.trido.healthcare.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private final String appUrl;
    private final User user;

    public OnRegistrationCompleteEvent(final User user, final String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }
}
