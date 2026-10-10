package com.example.deliverytracker.delivery.controller;

import com.example.deliverytracker.delivery.dto.DeliveryAddressCreateRequest;
import com.example.deliverytracker.delivery.dto.DeliveryAddressResponse;
import com.example.deliverytracker.delivery.dto.DeliveryAddressUpdateRequest;
import com.example.deliverytracker.delivery.service.DeliveryAddressService;
import com.example.deliverytracker.user.entity.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery-addresses")
public class DeliveryAddressController {

    private final DeliveryAddressService deliveryAddressService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<Void> createAddress(@Valid @RequestBody DeliveryAddressCreateRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        deliveryAddressService.createAddress(request, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<DeliveryAddressResponse>> getAddresses(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(deliveryAddressService.getAddresses(userDetails.getUser()));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{addressId}")
    public ResponseEntity<DeliveryAddressResponse> getAddress(@PathVariable Long addressId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(deliveryAddressService.getAddress(addressId, userDetails.getUser()));
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{addressId}")
    public ResponseEntity<Void> updateAddress(@PathVariable Long addressId, @Valid @RequestBody DeliveryAddressUpdateRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        deliveryAddressService.updateAddress(addressId, request, userDetails.getUser());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        deliveryAddressService.deleteAddress(addressId, userDetails.getUser());

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{addressId}/default")
    public ResponseEntity<Void> setDefaultAddress(@PathVariable Long addressId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        deliveryAddressService.setDefaultAddress(addressId, userDetails.getUser());

        return ResponseEntity.ok().build();
    }
}
