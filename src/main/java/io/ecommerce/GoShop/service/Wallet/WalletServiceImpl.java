package io.ecommerce.GoShop.service.Wallet;

import io.ecommerce.GoShop.model.Wallet;
import io.ecommerce.GoShop.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class WalletServiceImpl implements WalletService{


    @Autowired
    WalletRepository walletRepository;

    @Override
    public Wallet saveWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }
}
