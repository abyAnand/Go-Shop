package io.ecommerce.GoShop.service.address;

import io.ecommerce.GoShop.model.Address;
import io.ecommerce.GoShop.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressService {

    void save(Address address);

    Optional<Address> findById(UUID id);

    List<Address> findByUser(User user);

    void deleteAddress(Address address);
}
