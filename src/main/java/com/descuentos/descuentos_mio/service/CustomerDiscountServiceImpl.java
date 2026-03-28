package com.descuentos.descuentos_mio.service;

import com.descuentos.descuentos_mio.domain.CustomerDiscountDomain;
import com.descuentos.descuentos_mio.dto.CustomerDiscountDTO;
import com.descuentos.descuentos_mio.repository.CustomerDiscountRepository;
import com.descuentos.descuentos_mio.utils.customerdiscount.mapper.CustomerDiscountMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerDiscountServiceImpl implements CustomerDiscountService {

    private final CustomerDiscountRepository customerDiscountRepository;
    private final CustomerDiscountMapper customerDiscountMapper;

    public CustomerDiscountServiceImpl(
            CustomerDiscountRepository customerDiscountRepository,
            CustomerDiscountMapper customerDiscountMapper
    ) {
        this.customerDiscountRepository = customerDiscountRepository;
        this.customerDiscountMapper = customerDiscountMapper;
    }

    @Override
    public List<CustomerDiscountDTO> getAllCustomerDiscounts() {
        return customerDiscountRepository.findAll().stream().map(customerDiscountMapper::toDto).toList();
    }

    @Override
    public List<CustomerDiscountDTO> getDiscountsByCustomerId(UUID customerId) {
        return customerDiscountRepository.findByCustomerId(customerId).stream().map(customerDiscountMapper::toDto).toList();
    }

    @Override
    public CustomerDiscountDTO createCustomerDiscount(CustomerDiscountDTO customerDiscount) {
        CustomerDiscountDTO validated = validate(customerDiscount);
        CustomerDiscountDomain domain = customerDiscountMapper.toDomain(validated);
        domain.setCreatedAt(LocalDateTime.now());
        CustomerDiscountDomain saved = customerDiscountRepository.save(domain);
        return customerDiscountMapper.toDto(saved);
    }

    @Override
    public Optional<CustomerDiscountDTO> updateCustomerDiscount(UUID id, CustomerDiscountDTO customerDiscount) {
        CustomerDiscountDTO validated = validate(customerDiscount);
        return customerDiscountRepository.findById(id)
                .map(existing -> {
                    customerDiscountMapper.updateDomain(existing, validated);
                    existing.setModifiedAt(LocalDateTime.now());
                    return customerDiscountRepository.save(existing);
                })
                .map(customerDiscountMapper::toDto);
    }

    @Override
    public boolean deleteCustomerDiscount(UUID id) {
        if (!customerDiscountRepository.existsById(id)) {
            return false;
        }
        customerDiscountRepository.deleteById(id);
        return true;
    }

    private CustomerDiscountDTO validate(CustomerDiscountDTO customerDiscount) {
        if (customerDiscount.getLocationId() == null) {
            throw new IllegalArgumentException("locationId es obligatorio");
        }
        if (customerDiscount.getCustomerId() == null) {
            throw new IllegalArgumentException("customerId es obligatorio");
        }
        if (customerDiscount.getDiscountId() == null) {
            throw new IllegalArgumentException("discountId es obligatorio");
        }
        if (customerDiscount.getAssignedAt() == null) {
            customerDiscount.setAssignedAt(LocalDate.now());
        }
        return customerDiscount;
    }
}
