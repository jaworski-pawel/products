package com.edge1s.product;

import com.edge1s.product.entity.Type;
import com.edge1s.product.repository.TypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

    @Bean
    CommandLineRunner init(TypeRepository typeRepository) {
        return args -> {
            Type male = new Type();
            male.setName("MALE");
            male.setDiscountInPercent(5);
            typeRepository.save(male);

            Type female = new Type();
            female.setName("FEMALE");
            female.setDiscountInPercent(5);
            typeRepository.save(female);

            Type kid = new Type();
            kid.setName("KID");
            kid.setDiscountInPercent(10);
            typeRepository.save(kid);
        };
    }


}
