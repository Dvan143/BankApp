package org.example.listeners;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitProducer {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendVerificationCode(MetaInfoDto metainfoDto, String secret) {
        // Sending request to EmailVerifier
        metainfoDto.setSecretCode(secret);
        rabbitTemplate.convertAndSend("email-exchange","to.emailService.v2", metainfoDto);
    }
}
