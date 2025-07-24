package com.example.deliverytracker.rider.controller;

import com.example.deliverytracker.global.jwt.JwtProvider;
import com.example.deliverytracker.rider.dto.RiderLoginRequestDto;
import com.example.deliverytracker.rider.dto.RiderLoginResponseDto;
import com.example.deliverytracker.rider.dto.RiderProfileResponseDto;
import com.example.deliverytracker.rider.dto.RiderSignupRequestDto;
import com.example.deliverytracker.rider.service.RiderService;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        RiderProfileResponseDto response = riderService.getMyInfo(userDetails.getUser());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                    HttpServletRequest request) {
        String accessToken = jwtProvider.resolveToken(request);
        riderService.logout(userDetails.getUser(), accessToken);
        return ResponseEntity.ok().build();
    }
}
