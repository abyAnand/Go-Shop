package io.ecommerce.GoShop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class BannerImage extends BaseEntity {

    private String imagePath;


    @OneToOne
    @JoinColumn(name = "banner_id")
    private Banner banner;
}