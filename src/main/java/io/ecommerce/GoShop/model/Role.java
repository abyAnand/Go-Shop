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
public class Role extends BaseEntity{

    private String roleName;







}
