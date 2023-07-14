package io.ecommerce.GoShop.repository;

import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Wallet findByUser(User user);
}
