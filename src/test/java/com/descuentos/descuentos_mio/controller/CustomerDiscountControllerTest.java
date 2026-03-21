package com.descuentos.descuentos_mio.controller;

import com.descuentos.descuentos_mio.dto.CustomerDiscountDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerDiscountControllerTest {

    private static final UUID CUSTOMER_DISCOUNT_ID_1 = UUID.fromString("70000000-0000-0000-0000-000000000001");
    private static final UUID LOCATION_ID_1 = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private static final UUID CUSTOMER_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID CUSTOMER_ID_2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID DISCOUNT_ID_1 = UUID.fromString("90000000-0000-0000-0000-000000000001");
    private static final UUID DISCOUNT_ID_2 = UUID.fromString("90000000-0000-0000-0000-000000000002");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetCustomerDiscounts() throws Exception {
        mockMvc.perform(get("/comercial/api/v1/customer-discounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty());
    }

    @Test
    void shouldGetDiscountsByCustomerId() throws Exception {
        mockMvc.perform(get("/comercial/api/v1/customers/{customerId}/discounts", CUSTOMER_ID_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(CUSTOMER_ID_1.toString()));
    }

    @Test
    void shouldCreateCustomerDiscount() throws Exception {
        CustomerDiscountDto request = new CustomerDiscountDto(
                null,
                LOCATION_ID_1,
                CUSTOMER_ID_2,
                DISCOUNT_ID_1,
                LocalDate.now()
        );

        mockMvc.perform(post("/comercial/api/v1/customer-discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID_2.toString()));
    }

    @Test
    void shouldUpdateCustomerDiscount() throws Exception {
        CustomerDiscountDto request = new CustomerDiscountDto(
                null,
                LOCATION_ID_1,
                CUSTOMER_ID_1,
                DISCOUNT_ID_2,
                LocalDate.now()
        );

        mockMvc.perform(put("/comercial/api/v1/customer-discounts/{id}", CUSTOMER_DISCOUNT_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CUSTOMER_DISCOUNT_ID_1.toString()))
                .andExpect(jsonPath("$.discountId").value(DISCOUNT_ID_2.toString()));
    }

    @Test
    void shouldDeleteCustomerDiscount() throws Exception {
        mockMvc.perform(delete("/comercial/api/v1/customer-discounts/{id}", CUSTOMER_DISCOUNT_ID_1))
                .andExpect(status().isNoContent());
    }
}
