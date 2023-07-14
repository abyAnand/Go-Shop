package io.ecommerce.GoShop.service.Wallet;

import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.model.Wallet;

public interface WalletService {

    Wallet saveWallet(Wallet wallet);

    Wallet findByUser(User user);

}
