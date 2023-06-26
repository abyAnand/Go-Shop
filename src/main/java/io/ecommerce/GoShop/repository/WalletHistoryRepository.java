package io.ecommerce.GoShop.repository;

import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.model.Wallet;
import io.ecommerce.GoShop.model.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WalletHistoryRepository extends JpaRepository<WalletHistory, UUID> {

    List<WalletHistory> findByWallet(Wallet wallet);
}
