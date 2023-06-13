package io.ecommerce.GoShop.repository;


import io.ecommerce.GoShop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {


    Optional<User> findByUsername(String username);

    Page<User> findByUsername(String username, Pageable pageable);

    @Override
    <S extends User> S save(S user);


    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(long phoneNumber);

    Optional<User> findPhoneNumberByUsername(String username);
}
