package io.pidabrow.shiftmanager.dto.rabbit;

import lombok.Builder;

@Builder
public class SmsNotification extends QueueableMessage {

    private String phoneNumber;
    private String messageBody;
}
