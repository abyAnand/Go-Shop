package io.ecommerce.GoShop.service.walletHistory;

import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.model.Wallet;
import io.ecommerce.GoShop.model.WalletHistory;
import io.ecommerce.GoShop.repository.WalletHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletHistoryImpl implements WalletHistoryService{

    @Autowired
    WalletHistoryRepository walletHistoryRepository;
    @Override
    public List<WalletHistory> getAllWalletHistory() {
        return walletHistoryRepository.findAll();
    }

    @Override
    public List<WalletHistory> getWallet(Wallet wallet) {
        return walletHistoryRepository.findByWallet(wallet);
    }

}
