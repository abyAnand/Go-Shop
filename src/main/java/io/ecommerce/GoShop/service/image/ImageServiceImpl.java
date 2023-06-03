package io.ecommerce.GoShop.service.image;

import io.ecommerce.GoShop.model.Image;
import io.ecommerce.GoShop.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    ImageRepository imageRepository;

    @Override
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public void deleteImage(Image image) {
        imageRepository.delete(image);
    }

    @Override
    public void deleteImageById(UUID id) {
        imageRepository.deleteById(id);
    }


}
