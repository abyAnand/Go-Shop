package io.ecommerce.GoShop.Auth.Otp;

import io.ecommerce.GoShop.model.LoginOtp;
import io.ecommerce.GoShop.model.OtpDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class LoginServiceImpl implements ILoginService{

    @Autowired
    OtpLoginRepository otpLoginRepository;


    @Override
    public void saveOtpWithusername(String username, String otp) {
        Optional<LoginOtp> exist = otpLoginRepository.findByUsername(username);


        exist.ifPresent(otpLogin -> otpLoginRepository.deleteByUsername(otpLogin.getUsername()));

        LoginOtp loginOtp   = new LoginOtp();
        loginOtp.setOtp(otp);
        loginOtp.setUsername(username);
        otpLoginRepository.save(loginOtp);
    }

    @Override
    public Optional<LoginOtp> findByusername(String username) {
        return otpLoginRepository.findByUsername(username);
    }
}
