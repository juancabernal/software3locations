package com.co.eatupapi.services.commercial.customerDiscount;

import com.co.eatupapi.domain.commercial.customerDiscount.CustomerDiscountDomain;
import com.co.eatupapi.dto.commercial.customerDiscount.CustomerDiscountDTO;
import com.co.eatupapi.repositories.commercial.customerDiscount.CustomerDiscountRepository;
import com.co.eatupapi.utils.commercial.customerDiscount.mapper.CustomerDiscountMapper;
import com.co.eatupapi.repositories.commercial.discount.DiscountRepository;
import com.co.eatupapi.domain.commercial.discount.DiscountDomain;
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
    private final DiscountRepository discountRepository;

    public CustomerDiscountServiceImpl(
            CustomerDiscountRepository customerDiscountRepository,
            CustomerDiscountMapper customerDiscountMapper,
            DiscountRepository discountRepository
    ) {
        this.customerDiscountRepository = customerDiscountRepository;
        this.customerDiscountMapper = customerDiscountMapper;
        this.discountRepository = discountRepository;
    }

    @Override
    public List<CustomerDiscountDTO> getAllCustomerDiscounts() {
        return customerDiscountRepository.findAll().stream().map(customerDiscountMapper::toDto).toList();
    }
    @Override
    public CustomerDiscountDTO getCustomerDiscountById(UUID customerDiscountId) {
        CustomerDiscountDomain domain = customerDiscountRepository.findById(customerDiscountId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe un CustomerDiscount con id: " + customerDiscountId));
        return customerDiscountMapper.toDto(domain);
    }

    @Override
    public List<CustomerDiscountDTO> getDiscountsByCustomerId(UUID customerId) {
        return customerDiscountRepository.findByCustomerId(customerId).stream().map(customerDiscountMapper::toDto).toList();
    }

    @Override
    public List<CustomerDiscountDTO> getCustomersByDiscountId(UUID discountId) {
        return customerDiscountRepository.findByDiscountId(discountId)
                .stream().map(customerDiscountMapper::toDto).toList();
    }

    @Override
    public CustomerDiscountDTO getApplicableCustomerDiscount(
            UUID customerDiscountId,
            UUID customerId,
            UUID locationId) {

        // 1. Busca el CustomerDiscount — lanza si no existe
        CustomerDiscountDomain domain = customerDiscountRepository.findById(customerDiscountId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe un CustomerDiscount con id: " + customerDiscountId));

        // 2. Valida que pertenezca al cliente
        if (!domain.getCustomerId().equals(customerId)) {
            throw new IllegalArgumentException(
                    "El descuento no pertenece al cliente indicado");
        }

        // 3. Valida que pertenezca a la sede
        if (!domain.getLocationId().equals(locationId)) {
            throw new IllegalArgumentException(
                    "El descuento no pertenece a la sede indicada");
        }

        // 4. Valida que el Discount relacionado esté activo
        // (mismo proyecto → usamos DiscountRepository directamente)
        DiscountDomain discount = discountRepository.findById(domain.getDiscountId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "El descuento asociado no existe"));

        if (!Boolean.TRUE.equals(discount.getStatus())) {
            throw new IllegalArgumentException(
                    "El descuento asociado no está activo");
        }
        // 5. Valida vigencia si tiene fechas definidas
        LocalDate hoy = LocalDate.now();

        if (domain.getStartDate() != null && hoy.isBefore(domain.getStartDate())) {
            throw new IllegalArgumentException(
                    "El descuento aún no está vigente");
        }

        if (domain.getEndDate() != null && hoy.isAfter(domain.getEndDate())) {
            throw new IllegalArgumentException(
                    "El descuento ya venció");
        }

        return customerDiscountMapper.toDto(domain);
    }

    @Override
    public CustomerDiscountDTO createCustomerDiscount(CustomerDiscountDTO customerDiscount) {
        CustomerDiscountDTO validated = validate(customerDiscount);
        CustomerDiscountDomain domain = customerDiscountMapper.toDomain(validated);
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
        if (customerDiscount.getAssignedAt() != null
                && customerDiscount.getAssignedAt().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("assignedAt no puede ser una fecha futura");
        }
        if (customerDiscount.getAssignedAt() == null) {
            customerDiscount.setAssignedAt(LocalDate.now());
        }
        if (customerDiscount.getStartDate() != null && customerDiscount.getEndDate() != null
                && customerDiscount.getEndDate().isBefore(customerDiscount.getStartDate())) {
            throw new IllegalArgumentException(
                    "endDate no puede ser anterior a startDate");
        }
        return customerDiscount;
    }
}
