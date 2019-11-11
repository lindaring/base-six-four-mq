package com.lindaring.base.config;

import com.lindaring.base.dto.VisitorDto;
import com.lindaring.base.entity.Visitor;
import com.lindaring.base.helper.RabbitMQHelper;
import com.lindaring.base.repo.VisitorsRepo;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private VisitorsRepo visitorsRepo;

    @Async
    @Scheduled(cron = "${spring.rabbitmq.cron.visitor}")
    public void writeVisitorQueueToDatabase() {
        if (RabbitMQHelper.visitorsList.isEmpty()) {
            log.info("No visitors to write.");
            return;
        }
        log.info("Writing visitor queue to database..." + RabbitMQHelper.visitorsList.size());

        DozerBeanMapper mapper = new DozerBeanMapper();
        List<Visitor> entityList = new ArrayList<>();
        for (VisitorDto dto : RabbitMQHelper.visitorsList) {
            Visitor entity = mapper.map(dto, Visitor.class);
            entityList.add(entity);
        }

        visitorsRepo.saveAll(entityList);
        RabbitMQHelper.visitorsList.clear();
    }

}
