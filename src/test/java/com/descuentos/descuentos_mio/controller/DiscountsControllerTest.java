package com.descuentos.descuentos_mio.controller;

import com.descuentos.descuentos_mio.dto.DiscountsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DiscountsControllerTest {

    private static final UUID CATEGORY_ID_1 = UUID.fromString("10f11111-1111-1111-1111-111111111111");
    private static final UUID DISCOUNT_ID_1 = UUID.fromString("90000000-0000-0000-0000-000000000001");
    private static final UUID DISCOUNT_ID_2 = UUID.fromString("90000000-0000-0000-0000-000000000002");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetDiscountsList() throws Exception {
        mockMvc.perform(get("/comercial/api/v1/discounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty());
    }

    @Test
    void shouldCreateDiscount() throws Exception {
        DiscountsDto request = new DiscountsDto(
                null,
                CATEGORY_ID_1,
                18,
                "Descuento temporal",
                true
        );

        mockMvc.perform(post("/comercial/api/v1/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.percentage").value(18));
    }

    @Test
    void shouldUpdateDiscountStatus() throws Exception {
        mockMvc.perform(patch("/comercial/api/v1/discounts/{discountId}/status", DISCOUNT_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", false))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DISCOUNT_ID_1.toString()))
                .andExpect(jsonPath("$.status").value(false));
    }

    @Test
    void shouldDeleteDiscount() throws Exception {
        mockMvc.perform(delete("/comercial/api/v1/discounts/{discountId}", DISCOUNT_ID_2))
                .andExpect(status().isNoContent());
    }
}
