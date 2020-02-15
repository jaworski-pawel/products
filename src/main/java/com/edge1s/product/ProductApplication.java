package com.edge1s.product;

import com.edge1s.product.entity.Discount;
import com.edge1s.product.entity.Type;
import com.edge1s.product.repository.DiscountRepository;
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
    CommandLineRunner init(TypeRepository typeRepository, DiscountRepository discountRepository) {
        return args -> {
            Type male = new Type();
            male.setName("MALE");
            typeRepository.save(male);

            Type female = new Type();
            female.setName("FEMALE");
            typeRepository.save(female);

            Type kid = new Type();
            kid.setName("KID");
            typeRepository.save(kid);

            Discount small = Discount.builder()
                    .name("small")
                    .discountInPercent(3)
                    .build();
            discountRepository.save(small);
            Discount big = Discount.builder()
                    .name("big")
                    .discountInPercent(5)
                    .build();
            discountRepository.save(big);
        };
    }


}
