package io.ecommerce.GoShop.model;


import lombok.*;
import javax.persistence.*;
import java.util.Collection;


@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_account")
public class User extends BaseEntity{


    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @Column(name = "phone_number")
    private long phoneNumber;

    private String username;

    private String password;

    private boolean enabled;


    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    private Role role;



}
