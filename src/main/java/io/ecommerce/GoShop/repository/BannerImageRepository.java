package io.ecommerce.GoShop.repository;

import io.ecommerce.GoShop.model.Banner;
import io.ecommerce.GoShop.model.BannerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BannerImageRepository extends JpaRepository<BannerImage, UUID> {

    BannerImage findByBannerId(UUID id);
}
