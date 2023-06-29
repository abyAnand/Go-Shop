package io.ecommerce.GoShop.service.banner;

import io.ecommerce.GoShop.model.Banner;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BannerService {

    Banner save(Banner banner);

    void deletebyId(UUID id);

    Optional<Banner> findById(UUID id);

    List<Banner> findAll();
}
