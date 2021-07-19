package io.pidabrow.shiftmanager.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Primary
public class RabbitConfiguration extends RabbitProperties {

    @Value("${rabbit.sms.exchange}")
    @Getter
    private String exchange;

    @Value("${rabbit.sms.routingKey}")
    @Getter
    private String routingKey;
}
