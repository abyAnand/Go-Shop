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
public class OtpDto extends BaseEntity{

    private String sessionId;

    private String otp;
}
