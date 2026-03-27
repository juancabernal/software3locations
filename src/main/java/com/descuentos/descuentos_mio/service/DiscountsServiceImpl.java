package com.descuentos.descuentos_mio.service;

import com.descuentos.descuentos_mio.domain.DiscountsDomain;
import com.descuentos.descuentos_mio.dto.DiscountsDto;
import com.descuentos.descuentos_mio.repository.DiscountsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DiscountsServiceImpl implements DiscountsService {

    private final DiscountsRepository discountsRepository;

    public DiscountsServiceImpl(DiscountsRepository discountsRepository) {
        this.discountsRepository = discountsRepository;
    }

    @Override
    public List<DiscountsDto> getAllDiscounts() {
        return discountsRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public Optional<DiscountsDto> getDiscountById(UUID id) {
        return discountsRepository.findById(id).map(this::toDto);
    }

    @Override
    public DiscountsDto createDiscount(DiscountsDto discount) {
        DiscountsDto validated = validate(discount);
        DiscountsDomain domain = toDomain(validated);
        domain.setCreatedAt(LocalDateTime.now());
        DiscountsDomain saved = discountsRepository.save(domain);
        return toDto(saved);
    }

    @Override
    public Optional<DiscountsDto> updateDiscount(UUID id, DiscountsDto discount) {
        DiscountsDto validated = validate(discount);
        return discountsRepository.findById(id)
                .map(existing -> {
                    existing.setCategoryId(validated.getCategoryId());
                    existing.setPercentage(validated.getPercentage());
                    existing.setDescription(validated.getDescription());
                    existing.setStatus(validated.getStatus());
                    existing.setModifiedAt(LocalDateTime.now());
                    return discountsRepository.save(existing);
                })
                .map(this::toDto);
    }

    @Override
    public Optional<DiscountsDto> updateDiscountStatus(UUID id, Boolean status) {
        if (status == null) {
            throw new IllegalArgumentException("status es obligatorio");
        }
        return discountsRepository.findById(id)
                .map(existing -> {
                    existing.setStatus(status);
                    existing.setModifiedAt(LocalDateTime.now());
                    return discountsRepository.save(existing);
                })
                .map(this::toDto);
    }

    @Override
    public boolean deleteDiscount(UUID id) {
        if (!discountsRepository.existsById(id)) {
            return false;
        }
        discountsRepository.deleteById(id);
        return true;
    }

    private DiscountsDto validate(DiscountsDto discount) {
        if (discount.getPercentage() == null || discount.getPercentage() < 0 || discount.getPercentage() > 100) {
            throw new IllegalArgumentException("percentage debe estar entre 0 y 100");
        }
        if (discount.getCategoryId() == null) {
            throw new IllegalArgumentException("categoryId es obligatorio");
        }
        if (discount.getDescription() == null || discount.getDescription().isBlank()) {
            throw new IllegalArgumentException("description es obligatoria");
        }
        if (discount.getStatus() == null) {
            discount.setStatus(Boolean.TRUE);
        }
        return discount;
    }

    private DiscountsDto toDto(DiscountsDomain domain) {
        return new DiscountsDto(
                domain.getId(),
                domain.getCategoryId(),
                domain.getPercentage(),
                domain.getDescription(),
                domain.getStatus()
        );
    }

    private DiscountsDomain toDomain(DiscountsDto dto) {
        return new DiscountsDomain(
                dto.getId(),
                dto.getCategoryId(),
                dto.getPercentage(),
                dto.getDescription(),
                dto.getStatus()
        );
    }
}
