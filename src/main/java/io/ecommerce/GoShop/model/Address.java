package io.ecommerce.GoShop.model;


import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Address extends BaseEntity {

    private String flat;

    private String area;

    private String town;

    private String city;

    private String  state;

    private String pin;

    private String landmark;

    private boolean defaultAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
