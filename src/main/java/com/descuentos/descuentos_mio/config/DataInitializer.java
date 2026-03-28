package com.descuentos.descuentos_mio.config;

import com.descuentos.descuentos_mio.domain.CustomerDiscountDomain;
import com.descuentos.descuentos_mio.domain.DiscountDomain;
import com.descuentos.descuentos_mio.repository.CustomerDiscountRepository;
import com.descuentos.descuentos_mio.repository.DiscountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class DataInitializer {

    private static final UUID DISCOUNT_ID_1 = UUID.fromString("90000000-0000-0000-0000-000000000001");
    private static final UUID DISCOUNT_ID_2 = UUID.fromString("90000000-0000-0000-0000-000000000002");

    private static final UUID CATEGORY_ID_1 = UUID.fromString("10f11111-1111-1111-1111-111111111111");
    private static final UUID CATEGORY_ID_2 = UUID.fromString("20f22222-2222-2222-2222-222222222222");

    private static final UUID CUSTOMER_DISCOUNT_ID_1 = UUID.fromString("70000000-0000-0000-0000-000000000001");
    private static final UUID LOCATION_ID_1 = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private static final UUID CUSTOMER_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Bean
    CommandLineRunner seedData(DiscountRepository discountRepository, CustomerDiscountRepository customerDiscountRepository) {
        return args -> {
            if (!discountRepository.existsById(DISCOUNT_ID_1)) {
                DiscountDomain firstDiscount = new DiscountDomain(
                        DISCOUNT_ID_1,
                        CATEGORY_ID_1,
                        10,
                        "Descuento de bienvenida",
                        true
                );
                firstDiscount.setCreatedAt(LocalDateTime.now());
                discountRepository.save(firstDiscount);
            }

            if (!discountRepository.existsById(DISCOUNT_ID_2)) {
                DiscountDomain secondDiscount = new DiscountDomain(
                        DISCOUNT_ID_2,
                        CATEGORY_ID_2,
                        25,
                        "Descuento premium",
                        true
                );
                secondDiscount.setCreatedAt(LocalDateTime.now());
                discountRepository.save(secondDiscount);
            }

            if (!customerDiscountRepository.existsById(CUSTOMER_DISCOUNT_ID_1)) {
                CustomerDiscountDomain firstCustomerDiscount = new CustomerDiscountDomain(
                        CUSTOMER_DISCOUNT_ID_1,
                        LOCATION_ID_1,
                        CUSTOMER_ID_1,
                        DISCOUNT_ID_1,
                        LocalDate.now()
                );
                firstCustomerDiscount.setCreatedAt(LocalDateTime.now());
                customerDiscountRepository.save(firstCustomerDiscount);
            }
        };
    }
}