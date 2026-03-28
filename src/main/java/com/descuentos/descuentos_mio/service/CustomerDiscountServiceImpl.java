package com.descuentos.descuentos_mio.service;

import com.descuentos.descuentos_mio.client.CustomerServiceClient;
import com.descuentos.descuentos_mio.client.LocationServiceClient;
import com.descuentos.descuentos_mio.domain.CustomerDiscountDomain;
import com.descuentos.descuentos_mio.dto.CustomerDiscountDTO;
import com.descuentos.descuentos_mio.repository.CustomerDiscountRepository;
import com.descuentos.descuentos_mio.repository.DiscountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerDiscountServiceImpl implements CustomerDiscountService {

    private final CustomerDiscountRepository customerDiscountRepository;
    private final CustomerServiceClient customerServiceClient;
    private final LocationServiceClient locationServiceClient;
    private final DiscountRepository discountRepository;

    public CustomerDiscountServiceImpl(
            CustomerDiscountRepository customerDiscountRepository,
            CustomerServiceClient customerServiceClient,
            LocationServiceClient locationServiceClient,
            DiscountRepository discountRepository
    ) {
        this.customerDiscountRepository = customerDiscountRepository;
        this.customerServiceClient = customerServiceClient;
        this.locationServiceClient = locationServiceClient;
        this.discountRepository = discountRepository;
    }

    @Override
    public List<CustomerDiscountDTO> getAllCustomerDiscounts() {
        return customerDiscountRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public List<CustomerDiscountDTO> getDiscountsByCustomerId(UUID customerId) {
        if (customerServiceClient.findCustomerById(customerId).isEmpty()) {
            throw new IllegalArgumentException("customerId no existe en el servicio externo");
        }
        return customerDiscountRepository.findByCustomerId(customerId).stream().map(this::toDto).toList();
    }

    @Override
    public CustomerDiscountDTO createCustomerDiscount(CustomerDiscountDTO customerDiscount) {
        CustomerDiscountDTO validated = validate(customerDiscount);
        CustomerDiscountDomain domain = toDomain(validated);
        domain.setCreatedAt(LocalDateTime.now());
        CustomerDiscountDomain saved = customerDiscountRepository.save(domain);
        return toDto(saved);
    }

    @Override
    public Optional<CustomerDiscountDTO> updateCustomerDiscount(UUID id, CustomerDiscountDTO customerDiscount) {
        CustomerDiscountDTO validated = validate(customerDiscount);
        return customerDiscountRepository.findById(id)
                .map(existing -> {
                    existing.setLocationId(validated.getLocationId());
                    existing.setCustomerId(validated.getCustomerId());
                    existing.setDiscountId(validated.getDiscountId());
                    existing.setAssignedAt(validated.getAssignedAt());
                    existing.setModifiedAt(LocalDateTime.now());
                    return customerDiscountRepository.save(existing);
                })
                .map(this::toDto);
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
        if (locationServiceClient.findLocationById(customerDiscount.getLocationId()).isEmpty()) {
            throw new IllegalArgumentException("locationId no existe en el servicio externo");
        }
        if (customerServiceClient.findCustomerById(customerDiscount.getCustomerId()).isEmpty()) {
            throw new IllegalArgumentException("customerId no existe en el servicio externo");
        }
        if (discountRepository.findById(customerDiscount.getDiscountId()).isEmpty()) {
            throw new IllegalArgumentException("discountId no existe en discounts");
        }
        if (customerDiscount.getAssignedAt() == null) {
            customerDiscount.setAssignedAt(LocalDate.now());
        }
        return customerDiscount;
    }

    private CustomerDiscountDTO toDto(CustomerDiscountDomain domain) {
        return new CustomerDiscountDTO(
                domain.getId(),
                domain.getLocationId(),
                domain.getCustomerId(),
                domain.getDiscountId(),
                domain.getAssignedAt()
        );
    }

    private CustomerDiscountDomain toDomain(CustomerDiscountDTO dto) {
        return new CustomerDiscountDomain(
                dto.getId(),
                dto.getLocationId(),
                dto.getCustomerId(),
                dto.getDiscountId(),
                dto.getAssignedAt()
        );
    }
}