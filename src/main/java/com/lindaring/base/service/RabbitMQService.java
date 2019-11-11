package com.lindaring.base.service;

import com.lindaring.base.dto.VisitorDto;
import com.lindaring.base.helper.RabbitMQHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQService {

    @RabbitListener(queues="${spring.rabbitmq.queueName}")
    public void listen(VisitorDto msg) {
        log.info(msg + " message received.");
        if (msg != null) {
            RabbitMQHelper.visitorsList.add(msg);
        }
    }

}
