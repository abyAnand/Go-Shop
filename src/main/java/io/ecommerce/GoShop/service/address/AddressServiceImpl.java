package io.ecommerce.GoShop.service.address;

import io.ecommerce.GoShop.model.Address;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    AddressRepository addressRepository;
    @Override
    public void save(Address address) {
        addressRepository.save(address);
    }

    @Override
    public Optional<Address> findById(UUID id) {
        return addressRepository.findById(id);
    }

    @Override
    public List<Address> findByUser(User user) {
        return addressRepository.findByUser(user);
    }

    @Override
    public void deleteAddress(Address address) {

        address.setDeleted(true);
        addressRepository.save(address);
    }
}
