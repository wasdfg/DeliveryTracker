package com.example.deliverytracker.component;

import com.example.deliverytracker.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderService orderService;

    @Scheduled(cron = "0 * * * * *")
    public void checkAndCancelOrders() {
        orderService.autoCancelUnacceptedOrders();
    }

    @Scheduled(cron = "0 0/10 * * * *")
    public void checkAndCompleteOrders() {
        orderService.autoCompleteDeliveringOrders();
    }
}