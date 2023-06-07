package io.ecommerce.GoShop.model;

import lombok.*;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Otp extends BaseEntity{

    private UUID userId;

    private String otp;
}
