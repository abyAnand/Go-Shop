package io.ecommerce.GoShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GoShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoShopApplication.class, args);
	}

}
