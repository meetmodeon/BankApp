package com.bank.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
public class ExchangeRateScheduleTaskRunnerComponent {

    private final ExchangeRateService rateService;


    public ExchangeRateScheduleTaskRunnerComponent(ExchangeRateService rateService){
        this.rateService = rateService;

    }

    @Scheduled(fixedDelay = 1000*60*60*12)
    public void fetchExchangeRate() {
        log.info("Calling The Currency API endpoint for exchange rate");
       rateService.getExchangeRate();
    }
}
