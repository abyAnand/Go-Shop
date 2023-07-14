package io.ecommerce.GoShop.service.bannerImage;

import io.ecommerce.GoShop.model.Banner;
import io.ecommerce.GoShop.model.BannerImage;
import io.ecommerce.GoShop.repository.BannerImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BannerImageServiceImpl implements BannerImageService{

    @Autowired
    BannerImageRepository bannerImageRepository;
    @Override
    public void deleteBannerImage(BannerImage image) {
        bannerImageRepository.delete(image);
    }

    @Override
    public BannerImage findByBannerId(UUID id) {
        return bannerImageRepository.findByBannerId(id);
    }

    @Override
    public void save(BannerImage existingImage) {
        bannerImageRepository.save(existingImage);
    }
}
