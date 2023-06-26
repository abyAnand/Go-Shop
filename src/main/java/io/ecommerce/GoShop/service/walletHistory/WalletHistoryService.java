package io.ecommerce.GoShop.service.walletHistory;

import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.model.Wallet;
import io.ecommerce.GoShop.model.WalletHistory;

import java.util.List;

public interface WalletHistoryService {

    List<WalletHistory> getAllWalletHistory();

    List<WalletHistory> getWallet(Wallet wallet);
}
