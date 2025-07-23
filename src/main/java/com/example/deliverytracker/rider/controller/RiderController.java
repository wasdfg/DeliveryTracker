package com.example.deliverytracker.rider.controller;

import com.example.deliverytracker.rider.dto.RiderLoginRequestDto;
import com.example.deliverytracker.rider.dto.RiderLoginResponseDto;
import com.example.deliverytracker.rider.dto.RiderSignupRequestDto;
import com.example.deliverytracker.rider.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/rider")
@RestController
public class RiderController {

    private final RiderService riderService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RiderSignupRequestDto request) {
        this.riderService.signup(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<RiderLoginResponseDto> signup(@RequestBody RiderLoginRequestDto request) {
        RiderLoginResponseDto response = this.riderService.login(request);
        return ResponseEntity.ok(response);
    }
}
