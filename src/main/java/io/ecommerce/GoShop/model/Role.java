package io.ecommerce.GoShop.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Entity
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity{

    private String name;


    @ManyToMany(mappedBy = "roles")
    @ToString.Exclude
    private Collection<User> users;

    @ManyToMany
    @JoinTable(name = "role_privileges",
    joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    @ToString.Exclude
    private Collection<Privilege> privileges;


}
