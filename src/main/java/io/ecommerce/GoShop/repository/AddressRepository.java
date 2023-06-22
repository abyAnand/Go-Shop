package io.ecommerce.GoShop.repository;

import io.ecommerce.GoShop.model.Address;
import io.ecommerce.GoShop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

    List<Address> findByUser(User user);
}
