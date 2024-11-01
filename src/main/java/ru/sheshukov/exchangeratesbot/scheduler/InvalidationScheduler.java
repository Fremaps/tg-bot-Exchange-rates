package ru.sheshukov.exchangeratesbot.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.sheshukov.exchangeratesbot.service.ExchangeRatesService;

@Component
public class InvalidationScheduler {

    @Autowired
    private ExchangeRatesService service;

    @Scheduled(cron = "* 0 0 * * ?")
    public void invalidateCache() {
        service.clearUSDCache();
        service.clearEURCache();
        service.clearCNYCache();
        service.clearGBPCache();
        service.clearAUDCache();
    }
}
