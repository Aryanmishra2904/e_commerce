package com.aryan.e_commerce.address.service;

import com.aryan.e_commerce.address.Address;
import com.aryan.e_commerce.address.AddressRepository;
import com.aryan.e_commerce.address.dto.AddAddressRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepo;

    public Address addAddress(String userId, AddAddressRequest request) {

        Address address = Address.builder()
                .userId(userId)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .isDefault(request.getIsDefault())
                .build();

        return addressRepo.save(address);
    }

    public List<Address> getUserAddresses(String userId) {
        return addressRepo.findByUserId(userId);
    }

    public void deleteAddress(String userId, String addressId) {

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        addressRepo.delete(address);
    }
}

