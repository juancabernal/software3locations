package com.descuentos.descuentos_mio.controller;

import com.descuentos.descuentos_mio.dto.CustomerDiscountDTO;
import com.descuentos.descuentos_mio.service.CustomerDiscountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/comercial/api/v1")
public class CustomerDiscountController {

    private final CustomerDiscountService customerDiscountService;

    public CustomerDiscountController(CustomerDiscountService customerDiscountService) {
        this.customerDiscountService = customerDiscountService;
    }

    @GetMapping("/customer-discounts")
    public List<CustomerDiscountDTO> getAllCustomerDiscounts() {
        return customerDiscountService.getAllCustomerDiscounts();
    }

    @GetMapping("/customers/{customerId}/discounts")
    public ResponseEntity<List<CustomerDiscountDTO>> getDiscountsByCustomerId(@PathVariable UUID customerId) {
        List<CustomerDiscountDTO> discounts = customerDiscountService.getDiscountsByCustomerId(customerId);
        return ResponseEntity.ok(discounts);
    }

    @PostMapping("/customer-discounts")
    public ResponseEntity<CustomerDiscountDTO> createCustomerDiscount(@RequestBody CustomerDiscountDTO customerDiscountDto) {
        CustomerDiscountDTO created = customerDiscountService.createCustomerDiscount(customerDiscountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/customer-discounts/{id}")
    public ResponseEntity<CustomerDiscountDTO> updateCustomerDiscount(
            @PathVariable UUID id,
            @RequestBody CustomerDiscountDTO customerDiscountDto
    ) {
        return customerDiscountService.updateCustomerDiscount(id, customerDiscountDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/customer-discounts/{id}")
    public ResponseEntity<Void> deleteCustomerDiscount(@PathVariable UUID id) {
        if (customerDiscountService.deleteCustomerDiscount(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}
