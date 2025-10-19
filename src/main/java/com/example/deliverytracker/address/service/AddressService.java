package com.example.deliverytracker.address.service;

import com.example.deliverytracker.address.dto.AddressRequestDto;
import com.example.deliverytracker.address.dto.AddressResponseDto;
import com.example.deliverytracker.address.enitity.Address;
import com.example.deliverytracker.address.repository.AddressRepository;
import com.example.deliverytracker.user.entitiy.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    @Transactional
    public void addAddress(User user, AddressRequestDto requestDto) {
        Address address = new Address(
                user,
                requestDto.getAlias(),
                requestDto.getStreetAddress(),
                requestDto.getDetailAddress()
        );
        addressRepository.save(address);
    }

    public List<AddressResponseDto> getMyAddresses(User user) {
        return addressRepository.findAllByUser(user).stream()
                .map(AddressResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateAddress(Long addressId, User user, AddressRequestDto requestDto) throws AccessDeniedException {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("주소를 찾을 수 없습니다."));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("주소를 수정할 권한이 없습니다.");
        }

        address.update(requestDto.getAlias(), requestDto.getStreetAddress(), requestDto.getDetailAddress());
    }

    @Transactional
    public void deleteAddress(Long addressId, User user) throws AccessDeniedException {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("주소를 찾을 수 없습니다."));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("주소를 삭제할 권한이 없습니다.");
        }

        addressRepository.delete(address);
    }
}
