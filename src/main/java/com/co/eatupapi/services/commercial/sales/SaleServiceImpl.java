package com.co.eatupapi.services.commercial.sales;

import com.co.eatupapi.domain.commercial.sales.SaleDetailDomain;
import com.co.eatupapi.domain.commercial.sales.SaleDomain;
import com.co.eatupapi.domain.commercial.sales.SaleStatus;
import com.co.eatupapi.dto.commercial.sales.SaleDetailDTO;
import com.co.eatupapi.dto.commercial.sales.SalePatchDTO;
import com.co.eatupapi.dto.commercial.sales.SaleRequestDTO;
import com.co.eatupapi.dto.commercial.sales.SaleResponseDTO;
import com.co.eatupapi.repositories.commercial.sales.SaleRepository;
import com.co.eatupapi.utils.commercial.sales.exceptions.SaleBusinessException;
import com.co.eatupapi.utils.commercial.sales.exceptions.SaleNotFoundException;
import com.co.eatupapi.utils.commercial.sales.exceptions.SaleValidationException;
import com.co.eatupapi.utils.commercial.sales.mapper.SaleMapper;
import com.co.eatupapi.utils.commercial.sales.validation.ValidationUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleServiceImpl implements SaleService {

    private static final String VENTA_NO_ENCONTRADA = "No existe una venta con el id: ";

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;

    public SaleServiceImpl(SaleRepository saleRepository, SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.saleMapper = saleMapper;
    }

    @Override
    @Transactional
    public SaleResponseDTO createSale(SaleRequestDTO request) {
        validateRequiredSalePayload(request);
        validateSaleLineItems(request.getDetails());

        SaleDomain sale = new SaleDomain();
        sale.setSellerId(request.getSellerId().trim());
        sale.setLocationId(request.getLocationId());
        sale.setTableId(request.getTableId().trim());
        sale.setStatus(SaleStatus.CREATED);

        BigDecimal totalAmount = processSaleDetails(sale, request.getDetails());
        sale.setTotalAmount(totalAmount);

        return saleMapper.toDto(saleRepository.save(sale));
    }

    @Override
    public SaleResponseDTO getSaleById(UUID id) {
        SaleDomain sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(VENTA_NO_ENCONTRADA + id));
        return saleMapper.toDto(sale);
    }

    @Override
    public List<SaleResponseDTO> getAllSales() {
        return saleRepository.findAll().stream().map(saleMapper::toDto).toList();
    }

    @Override
    @Transactional
    public SaleResponseDTO updateSale(UUID id, SaleRequestDTO request) {
        SaleDomain existingSale = findSaleOrThrow(id);
        ensureSaleCanBeModified(existingSale);

        validateRequiredSalePayload(request);
        validateSaleLineItems(request.getDetails());

        existingSale.setSellerId(request.getSellerId().trim());
        existingSale.setLocationId(request.getLocationId());
        existingSale.setTableId(request.getTableId().trim());
        existingSale.getDetails().clear();
        existingSale.setTotalAmount(processSaleDetails(existingSale, request.getDetails()));

        return saleMapper.toDto(saleRepository.save(existingSale));
    }

    @Override
    @Transactional
    public SaleResponseDTO patchSale(UUID id, SalePatchDTO request) {
        SaleDomain existingSale = findSaleOrThrow(id);
        ensureSaleCanBeModified(existingSale);

        updateSaleBasicInfo(existingSale, request);
        updateSaleDetails(existingSale, request.details());

        validatePersistableSale(existingSale);
        return saleMapper.toDto(saleRepository.save(existingSale));
    }

    @Override
    @Transactional
    public void deleteSale(UUID id) {
        if (!saleRepository.existsById(id)) {
            throw new SaleNotFoundException(VENTA_NO_ENCONTRADA + id);
        }
        saleRepository.deleteById(id);
    }

    private SaleDomain findSaleOrThrow(UUID id) {
        return saleRepository.findById(id).orElseThrow(() -> new SaleNotFoundException(VENTA_NO_ENCONTRADA + id));
    }

    private void ensureSaleCanBeModified(SaleDomain sale) {
        if (sale.getStatus() == SaleStatus.COMPLETED) {
            throw new SaleBusinessException("No se puede modificar una venta en estado COMPLETED.");
        }
    }

    private void updateSaleBasicInfo(SaleDomain existingSale, SalePatchDTO request) {
        if (request.status() != null) {
            existingSale.setStatus(request.status());
        }

        if (request.sellerId() != null) {
            existingSale.setSellerId(trimToNull(request.sellerId()));
        }

        if (request.locationId() != null) {
            existingSale.setLocationId(request.locationId());
        }

        if (request.tableId() != null) {
            existingSale.setTableId(trimToNull(request.tableId()));
        }
    }

    private void updateSaleDetails(SaleDomain existingSale, List<SaleDetailDTO> details) {
        if (details == null) {
            return;
        }

        if (details.isEmpty()) {
            throw new SaleValidationException("La venta debe tener al menos una línea de detalle.");
        }

        validateSaleLineItems(details);
        existingSale.getDetails().clear();
        existingSale.setTotalAmount(processSaleDetails(existingSale, details));
    }

    private void validateRequiredSalePayload(SaleRequestDTO request) {
        ValidationUtils.requireObject(request, "El payload de venta es obligatorio.");
        ValidationUtils.requireText(request.getSellerId(), "sellerId");
        ValidationUtils.requireObject(request.getLocationId(), "La locationId es obligatoria.");
        ValidationUtils.requireText(request.getTableId(), "tableId");
        ValidationUtils.requireObject(request.getDetails(), "La lista de detalles es obligatoria.");

        if (request.getDetails().isEmpty()) {
            throw new SaleValidationException("La venta debe tener al menos una línea de detalle.");
        }
    }

    private void validateSaleLineItems(List<SaleDetailDTO> details) {
        for (SaleDetailDTO detail : details) {
            ValidationUtils.requireObject(detail, "Cada línea de detalle es obligatoria.");
            ValidationUtils.requireObject(detail.getRecipeId(), "El recipeId es obligatorio en cada línea.");
            ValidationUtils.requirePositive(detail.getQuantity(), "La cantidad debe ser mayor que cero.");
            ValidationUtils.requirePositive(detail.getUnitPrice(), "El precio unitario debe ser mayor que cero.");
            ValidationUtils.requireText(detail.getRecipeLineComment(), "recipeLineComment");
            ValidationUtils.validateMaxLength(detail.getRecipeLineComment(), 500, "recipeLineComment");
            ValidationUtils.validateMaxLength(detail.getLineDisplayName(), 255, "lineDisplayName");
        }
    }

    private void validatePersistableSale(SaleDomain sale) {
        ValidationUtils.requireText(sale.getSellerId(), "sellerId");
        ValidationUtils.requireObject(sale.getLocationId(), "La locationId es obligatoria.");
        ValidationUtils.requireText(sale.getTableId(), "tableId");
        ValidationUtils.requireObject(sale.getDetails(), "La lista de detalles es obligatoria.");
        if (sale.getDetails().isEmpty()) {
            throw new SaleValidationException("La venta debe tener al menos una línea de detalle.");
        }
    }

    private BigDecimal processSaleDetails(SaleDomain sale, List<SaleDetailDTO> detailDtos) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SaleDetailDTO detailDto : detailDtos) {
            SaleDetailDomain detail = new SaleDetailDomain();
            detail.setRecipeId(detailDto.getRecipeId());
            detail.setQuantity(detailDto.getQuantity());
            detail.setUnitPrice(detailDto.getUnitPrice());
            detail.setRecipeLineComment(detailDto.getRecipeLineComment().trim());
            detail.setLineDisplayName(trimToNull(detailDto.getLineDisplayName()));

            BigDecimal subtotal = calculateSubtotal(detailDto.getUnitPrice(), detailDto.getQuantity());
            detail.setSubtotal(subtotal);

            sale.addDetail(detail);
            totalAmount = totalAmount.add(subtotal);
        }

        return totalAmount;
    }

    private BigDecimal calculateSubtotal(BigDecimal unitPrice, BigDecimal quantity) {
        return unitPrice.multiply(quantity);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
