package com.example.deliverytracker.delivery.controller;

import com.example.deliverytracker.delivery.entity.LocationMessage;
import com.example.deliverytracker.delivery.service.ProximityService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LocationController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ProximityService proximityService;

    public LocationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/rider/location")
    public void updateRiderLocation(LocationMessage message) {

        messagingTemplate.convertAndSend("/topic/order/" + message.getOrderId(), message);

        proximityService.checkProximity(message);
    }
}
