package io.ecommerce.GoShop.model;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginOtp extends BaseEntity {

    private String username;

    private String otp;
}
