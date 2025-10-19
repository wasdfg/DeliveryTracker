package com.example.deliverytracker.address.controller;

import com.example.deliverytracker.address.dto.AddressRequestDto;
import com.example.deliverytracker.address.dto.AddressResponseDto;
import com.example.deliverytracker.address.service.AddressService;
import com.example.deliverytracker.user.entitiy.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<String> addAddress(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AddressRequestDto requestDto) {

        addressService.addAddress(userDetails.getUser(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("주소가 성공적으로 추가되었습니다.");
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDto>> getMyAddresses(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<AddressResponseDto> addresses = addressService.getMyAddresses(userDetails.getUser());
        return ResponseEntity.ok(addresses);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<String> updateAddress(
            @PathVariable Long addressId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AddressRequestDto requestDto) throws AccessDeniedException {

        addressService.updateAddress(addressId, userDetails.getUser(), requestDto);
        return ResponseEntity.ok("주소가 성공적으로 수정되었습니다.");
    }


    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long addressId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws AccessDeniedException {

        addressService.deleteAddress(addressId, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}