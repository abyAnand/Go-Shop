package io.ecommerce.GoShop.service.banner;

import io.ecommerce.GoShop.model.Banner;
import io.ecommerce.GoShop.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BannerServiceImpl implements BannerService{


    @Autowired
    BannerRepository bannerRepository;
    @Override
    public Banner save(Banner banner) {
        return bannerRepository.save(banner);
    }

    @Override
    public void deletebyId(UUID id) {
        bannerRepository.deleteById(id);
    }

    @Override
    public Optional<Banner> findById(UUID id) {
        return bannerRepository.findById(id);
    }

    @Override
    public List<Banner> findAll() {
        return bannerRepository.findAll();
    }
}
