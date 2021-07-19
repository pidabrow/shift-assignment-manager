package io.pidabrow.shiftmanager.service;

import io.pidabrow.shiftmanager.config.Environment;
import io.pidabrow.shiftmanager.config.RabbitConfiguration;
import io.pidabrow.shiftmanager.converter.SmsNotificationConverter;
import io.pidabrow.shiftmanager.dto.SmsDto;
import io.pidabrow.shiftmanager.dto.rabbit.QueueableMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService {

    @Value("${io.pidabrow.shiftmanager.environment}")
    private Environment environment;

    private final RabbitTemplate rabbitTemplate;
    private final RabbitConfiguration rabbitConfiguration;
    private final SmsNotificationConverter smsNotificationConverter;

    public void sendSmsNotification(SmsDto smsDto) {
        String exchange = rabbitConfiguration.getExchange();
        String routingKey = rabbitConfiguration.getRoutingKey();
        QueueableMessage payload = smsNotificationConverter.convert(smsDto);

        if(environment == Environment.PROD) {
            sendSmsNotificationIntern(exchange, routingKey, payload);
        }
    }

    private void sendSmsNotificationIntern(String exchange, String routingKey, QueueableMessage payload) {
        rabbitTemplate.convertAndSend(exchange, routingKey, payload);
    }
}
