package com.example.deliverytracker.delivery.service;


import com.example.deliverytracker.delivery.dto.DeliveryAddressCreateRequest;
import com.example.deliverytracker.delivery.dto.DeliveryAddressResponse;
import com.example.deliverytracker.delivery.dto.DeliveryAddressUpdateRequest;
import com.example.deliverytracker.delivery.entity.DeliveryAddress;
import com.example.deliverytracker.delivery.repository.DeliveryAddressRepository;
import com.example.deliverytracker.user.entity.User;
import com.example.deliverytracker.util.geocoding.Coordinates;
import com.example.deliverytracker.util.geocoding.GeocodingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final DeliveryAddressRepository deliveryAddressRepository;

    private final GeocodingService geocodingService;

    @Transactional
    public void createAddress(DeliveryAddressCreateRequest request, User user) {

        String baseAddress = request.getAddress().trim();

        Coordinates coordinates = geocodingService.getCoordinates(request.getAddress());

        if (request.isDefaultAddress()) {
            setExistingDefaultAddressFalse(user.getId());
        }

        DeliveryAddress address = new DeliveryAddress(
                user,
                request.getName(),
                request.getReceiverName(),
                request.getReceiverPhone(),
                baseAddress,
                request.getDetailAddress(),
                coordinates.getLatitude(),
                coordinates.getLongitude(),
                request.isDefaultAddress()
        );

        deliveryAddressRepository.save(address);
    }

    public List<DeliveryAddressResponse> getAddresses(User user) {

        return deliveryAddressRepository
                .findAllByUserIdOrderByDefaultAddressDescIdDesc(user.getId())
                .stream()
                .map(DeliveryAddressResponse::from)
                .toList();
    }

    public DeliveryAddressResponse getAddress(Long addressId, User user) {

        DeliveryAddress address = deliveryAddressRepository.findByIdAndUserId(addressId, user.getId()).orElseThrow(() -> new EntityNotFoundException("배송지를 찾을 수 없습니다."));

        return DeliveryAddressResponse.from(address);
    }

    @Transactional
    public void updateAddress(Long addressId, DeliveryAddressUpdateRequest request, User user) {

        DeliveryAddress address = deliveryAddressRepository.findByIdAndUserId(addressId, user.getId()).orElseThrow(() -> new EntityNotFoundException("배송지를 찾을 수 없습니다."));

        String newBaseAddress = request.getAddress().trim();

        boolean addressChanged = !Objects.equals(address.getAddress(), newBaseAddress);

        boolean coordinatesMissing = address.getLatitude() == null || address.getLongitude() == null;

        Coordinates coordinates = null;

        if (addressChanged || coordinatesMissing) {
            coordinates = geocodingService.getCoordinates(newBaseAddress);
        }

        if (request.isDefaultAddress()) {
            setExistingDefaultAddressFalse(user.getId());
        }

        address.update(request.getName(), request.getReceiverName(), request.getReceiverPhone(), newBaseAddress, request.getDetailAddress(), request.isDefaultAddress());

        if (coordinates != null) {
            address.updateCoordinates(
                    coordinates.getLatitude(),
                    coordinates.getLongitude()
            );
        }
    }

    @Transactional
    public void deleteAddress(Long addressId, User user) {

        DeliveryAddress address = deliveryAddressRepository.findByIdAndUserId(addressId, user.getId()).orElseThrow(() -> new EntityNotFoundException("배송지를 찾을 수 없습니다."));

        deliveryAddressRepository.delete(address);
    }

    @Transactional
    public void setDefaultAddress(Long addressId, User user) {

        DeliveryAddress address = deliveryAddressRepository.findByIdAndUserId(addressId, user.getId()).orElseThrow(() -> new EntityNotFoundException("배송지를 찾을 수 없습니다."));

        setExistingDefaultAddressFalse(user.getId());

        address.setDefaultAddress(true);
    }

    private void setExistingDefaultAddressFalse(Long userId) {

        deliveryAddressRepository.findByUserIdAndDefaultAddressTrue(userId).ifPresent(address -> address.setDefaultAddress(false));
    }
}
