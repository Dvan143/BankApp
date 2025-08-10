package org.example.listeners;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    Exchange exchange() {
        return new DirectExchange("email-exchange");
    }
}
