package io.ecommerce.GoShop.repository;

import io.ecommerce.GoShop.model.Role;
import io.ecommerce.GoShop.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {


    Role findByRoleName(String roleUser);

    List<Role> findAll();
}
