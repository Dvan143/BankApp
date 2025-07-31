package org.example.listeners;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RabbitConsumer {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendVerificationCode(MetaInfoDto metainfoDto) {
        Random random = new Random();
        String secretCode = String.valueOf(random.nextInt(100000,999999));
        metainfoDto.setSecretCode(secretCode);
        rabbitTemplate.convertAndSend("email-exchange","to.emailService.v2", metainfoDto);
    }
}
