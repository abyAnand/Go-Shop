package io.ecommerce.GoShop.service.image;

import io.ecommerce.GoShop.model.Image;

import java.util.UUID;

public interface ImageService {


    Image saveImage(Image image);

    void deleteImage(Image deletedImage);

    void deleteImageById(UUID id);
}
