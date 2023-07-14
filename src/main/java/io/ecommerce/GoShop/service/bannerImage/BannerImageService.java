package io.ecommerce.GoShop.service.bannerImage;

import io.ecommerce.GoShop.model.BannerImage;

import java.util.UUID;

public interface BannerImageService {

    void deleteBannerImage(BannerImage image);

    BannerImage findByBannerId(UUID id);

    void save(BannerImage existingImage);
}
