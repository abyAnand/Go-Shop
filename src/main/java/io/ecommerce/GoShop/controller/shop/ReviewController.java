package io.ecommerce.GoShop.controller.shop;

import io.ecommerce.GoShop.DTO.ReviewResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("reviews")
public class ReviewController {


    public String saveReview(@ModelAttribute ReviewResponse reviewResponse){


        return "index";
    }


}
