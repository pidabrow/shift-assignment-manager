package io.pidabrow.shiftmanager.converter;

import io.pidabrow.shiftmanager.dto.SmsDto;
import io.pidabrow.shiftmanager.dto.rabbit.SmsNotification;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SmsNotificationConverter implements Converter<SmsDto, SmsNotification> {

    @Override
    public SmsNotification convert(SmsDto smsDto) {
        return SmsNotification.builder()
                .phoneNumber(smsDto.getPhoneNumber())
                .messageBody(smsDto.getMessageBody())
                .build();
    }
}
