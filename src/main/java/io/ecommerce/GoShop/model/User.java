package io.ecommerce.GoShop.model;


import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


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

    @Column(name = "username")
    private String username;

    private String password;

    private boolean enabled;


    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    private Role role;



    @OneToOne
    @JoinColumn(name = "cart_id")
    @ToString.Exclude
    private Cart cart;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy ="user")
    @ToString.Exclude
    private List<Address> addresses = new ArrayList<Address>();

    @OneToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    private Wallet wallet;

    @OneToMany(mappedBy ="user")
    @ToString.Exclude
    private List<Review> reviews;


}
