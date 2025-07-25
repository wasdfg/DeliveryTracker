package com.example.deliverytracker.rider.controller;

import com.example.deliverytracker.global.jwt.JwtProvider;
import com.example.deliverytracker.rider.dto.RiderProfileResponseDto;
import com.example.deliverytracker.rider.dto.RiderStatusRequestDto;
import com.example.deliverytracker.rider.service.RiderService;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/rider")
@RestController
public class RiderController {

    private final RiderService riderService;

    private final JwtProvider jwtProvider;

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        RiderProfileResponseDto response = riderService.getMyInfo(userDetails.getUser());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/status")
    public ResponseEntity<?> changeStatus(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody RiderStatusRequestDto status) {
        riderService.changeStatus(userDetails.getUser(),status);
        return ResponseEntity.ok().build();
    }

}
