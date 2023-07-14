package io.ecommerce.GoShop.controller.shop;


import io.ecommerce.GoShop.DTO.UserDTO;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.model.Wallet;
import io.ecommerce.GoShop.model.WalletHistory;
import io.ecommerce.GoShop.service.Wallet.WalletService;
import io.ecommerce.GoShop.service.user.UserService;
import io.ecommerce.GoShop.service.walletHistory.WalletHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    WalletHistoryService walletHistoryService;

    @Autowired
    UserService userService;

    @Autowired
    WalletService walletService;

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }



    @GetMapping("/history")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getAllWallets(Model model) {

        List<WalletHistory> walletHistoryList = walletHistoryService.getAllWalletHistory();

        model.addAttribute("walletHistories", walletHistoryList);

        return "admin/wallet-history";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    public String getUserWalletHistory(@PathVariable UUID id,
                                       Model model){

        User user = userService.findByUsername(getCurrentUsername()).orElse(null);
        model.addAttribute("user",user);


        assert user != null;

        if(user.getWallet() != null){
            double balance = user.getWallet().getBalance();

            List<WalletHistory> walletHistoryList = walletHistoryService.getWallet(user.getWallet());
            model.addAttribute("walletHistories", walletHistoryList);
            model.addAttribute("balance", balance);
        }else{
            Wallet wallet = walletService.findByUser(user);
            if(wallet == null){
                Wallet newWallet = new Wallet();
                newWallet.setBalance(0.0);
                newWallet.setUser(user);
                newWallet = walletService.saveWallet(newWallet);
                user.setWallet(newWallet);
                userService.save(user);
            }
            List<WalletHistory> walletHistoryList = walletHistoryService.getWallet(wallet);
            model.addAttribute("walletHistories", walletHistoryList);
            model.addAttribute("balance", wallet.getBalance());
        }



        return "user/wallet-dashboard";
    }

}
