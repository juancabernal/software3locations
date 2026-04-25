package com.co.eatupapi.services.commercial.sales;

import com.co.eatupapi.domain.commercial.sales.SaleDetailDomain;
import com.co.eatupapi.domain.commercial.sales.SaleDomain;
import com.co.eatupapi.domain.commercial.sales.SaleStatus;
import com.co.eatupapi.domain.commercial.seller.SellerDomain;
import com.co.eatupapi.domain.inventory.recipe.RecipeDomain;
import com.co.eatupapi.dto.commercial.sales.SaleDetailDTO;
import com.co.eatupapi.dto.commercial.sales.SalePatchDTO;
import com.co.eatupapi.dto.commercial.sales.SaleRequestDTO;
import com.co.eatupapi.dto.commercial.sales.SaleResponseDTO;
import com.co.eatupapi.repositories.commercial.sales.SaleRepository;
import com.co.eatupapi.repositories.commercial.seller.SellerRepository;
import com.co.eatupapi.repositories.inventory.location.LocationRepository;
import com.co.eatupapi.domain.inventory.location.LocationDomain;
import com.co.eatupapi.repositories.inventory.recipe.RecipeRepository;
import com.co.eatupapi.services.commercial.table.TableService;
import com.co.eatupapi.utils.commercial.sales.exceptions.SaleBusinessException;
import com.co.eatupapi.utils.commercial.sales.exceptions.SaleNotFoundException;
import com.co.eatupapi.utils.commercial.sales.exceptions.SaleValidationException;
import com.co.eatupapi.utils.commercial.sales.mapper.SaleMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SaleServiceImpl implements SaleService {

    private static final String VENTA_NO_ENCONTRADA = "No existe una venta con el id: ";
    private static final int MAX_LINE_DISPLAY = 255;

    private final SaleRepository saleRepository;
    private final ObjectProvider<SellerRepository> sellerRepositoryProvider;
    private final ObjectProvider<RecipeRepository> recipeRepositoryProvider;
    private final ObjectProvider<LocationRepository> locationRepositoryProvider;
    private final ObjectProvider<TableService> tableServiceProvider;
    private final SaleMapper saleMapper;

    public SaleServiceImpl(SaleRepository saleRepository,
                           ObjectProvider<SellerRepository> sellerRepositoryProvider,
                           ObjectProvider<RecipeRepository> recipeRepositoryProvider,
                           ObjectProvider<LocationRepository> locationRepositoryProvider,
                           ObjectProvider<TableService> tableServiceProvider,
                           SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.sellerRepositoryProvider = sellerRepositoryProvider;
        this.recipeRepositoryProvider = recipeRepositoryProvider;
        this.locationRepositoryProvider = locationRepositoryProvider;
        this.tableServiceProvider = tableServiceProvider;
        this.saleMapper = saleMapper;
    }

    @Override
    @Transactional
    public SaleResponseDTO createSale(SaleRequestDTO request) {
        List<SaleDetailDTO> details = request.getDetails() != null ? request.getDetails() : List.of();
        validateSaleLineItems(details);

        SellerDomain seller = resolveSellerIfPresent(request.getSellerId());
        LocationDomain location = resolveLocationIfPresent(request.getLocationId());
        String tableId = trimToNull(request.getTableId());
        validateTableIfPresent(tableId);

        SaleDomain sale = new SaleDomain();
        sale.setSeller(seller);
        sale.setLocation(location);
        sale.setTableId(tableId);
        sale.setStatus(SaleStatus.CREATED);
        sale.setCreatedDate(LocalDateTime.now());
        sale.setModifiedDate(LocalDateTime.now());

        BigDecimal totalAmount = processSaleDetails(sale, details);

        sale.setTotalAmount(totalAmount);
        SaleDomain savedSale = saleRepository.save(sale);
        return saleMapper.toDto(savedSale);
    }

    private SellerDomain resolveSellerIfPresent(String sellerId) {
        if (sellerId == null || sellerId.isBlank()) {
            return null;
        }
        SellerRepository repo = sellerRepositoryProvider.getIfAvailable();
        if (repo == null) {
            throw new SaleBusinessException("No hay integración de vendedores disponible para verificar el id enviado.");
        }
        UUID id = parseUuidOrThrow(sellerId, "vendedor");
        return repo.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("El vendedor no existe (id: " + sellerId + ")."));
    }

    private LocationDomain resolveLocationIfPresent(UUID locationId) {
        if (locationId == null) {
            return null;
        }
        LocationRepository repo = locationRepositoryProvider.getIfAvailable();
        if (repo == null) {
            throw new SaleBusinessException("No hay integración de sedes disponible para verificar el id enviado.");
        }
        return repo.findById(locationId)
                .orElseThrow(() -> new SaleNotFoundException("La sede no existe (id: " + locationId + ")."));
    }

    private RecipeDomain resolveRecipeIfPresent(UUID recipeId) {
        if (recipeId == null) {
            return null;
        }
        RecipeRepository repo = recipeRepositoryProvider.getIfAvailable();
        if (repo == null) {
            throw new SaleBusinessException("No hay integración de recetas disponible para verificar el id enviado.");
        }
        return repo.findById(recipeId)
                .orElseThrow(() -> new SaleNotFoundException("La receta no existe (id: " + recipeId + ")."));
    }

    private void validateTableIfPresent(String tableId) {
        if (tableId == null || tableId.isBlank()) {
            return;
        }
        TableService tableService = tableServiceProvider.getIfAvailable();
        if (tableService == null) {
            throw new SaleBusinessException("No hay integración de mesas disponible para verificar la mesa indicada.");
        }
        try {
            tableService.getTableById(tableId);
        } catch (Exception e) {
            throw new SaleNotFoundException("La mesa no existe (id: " + tableId + ").");
        }
    }

    private BigDecimal processSaleDetails(SaleDomain sale, List<SaleDetailDTO> detailDtos) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SaleDetailDTO detailDto : detailDtos) {
            RecipeDomain recipe = resolveRecipeIfPresent(detailDto.getRecipeId());

            BigDecimal unitPrice;
            if (recipe != null) {
                unitPrice = detailDto.getUnitPrice() != null ? detailDto.getUnitPrice() : recipe.getSellingPrice();
            } else {
                if (detailDto.getUnitPrice() == null || detailDto.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new SaleValidationException(
                            "En líneas sin receta, el precio unitario es obligatorio y debe ser mayor que cero.");
                }
                unitPrice = detailDto.getUnitPrice();
            }

            SaleDetailDomain detail = new SaleDetailDomain();
            detail.setRecipe(recipe);
            detail.setQuantity(detailDto.getQuantity());
            detail.setRecipeLineComment(normalizeRecipeComment(detailDto.getRecipeComment()));
            detail.setUnitPrice(unitPrice);
            detail.setLineDisplayName(recipe != null ? null : normalizeLineDisplayName(detailDto.getRecipeName()));

            BigDecimal subtotal = detail.getUnitPrice().multiply(detail.getQuantity());
            detail.setSubtotal(subtotal);

            sale.addDetail(detail);
            totalAmount = totalAmount.add(subtotal);
        }
        return totalAmount;
    }

    @Override
    public SaleResponseDTO getSaleById(UUID id) {
        SaleDomain sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(VENTA_NO_ENCONTRADA + id));
        return saleMapper.toDto(sale);
    }

    @Override
    public List<SaleResponseDTO> getAllSales() {
        return saleRepository.findAll().stream()
                .map(saleMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public SaleResponseDTO updateSale(UUID id, SaleRequestDTO request) {
        SaleDomain existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(VENTA_NO_ENCONTRADA + id));

        if (existingSale.getStatus() == SaleStatus.COMPLETED) {
            throw new SaleBusinessException("No se puede modificar una venta en estado COMPLETADA.");
        }

        List<SaleDetailDTO> details = request.getDetails() != null ? request.getDetails() : List.of();
        validateSaleLineItems(details);

        SellerDomain seller = resolveSellerIfPresent(request.getSellerId());
        LocationDomain location = resolveLocationIfPresent(request.getLocationId());
        String tableId = trimToNull(request.getTableId());
        validateTableIfPresent(tableId);

        existingSale.setSeller(seller);
        existingSale.setLocation(location);
        existingSale.setTableId(tableId);
        existingSale.setModifiedDate(LocalDateTime.now());

        existingSale.getDetails().clear();

        BigDecimal totalAmount = processSaleDetails(existingSale, details);

        existingSale.setTotalAmount(totalAmount);
        SaleDomain updatedSale = saleRepository.save(existingSale);
        return saleMapper.toDto(updatedSale);
    }

    @Override
    @Transactional
    public SaleResponseDTO patchSale(UUID id, SalePatchDTO request) {
        SaleDomain existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(VENTA_NO_ENCONTRADA + id));

        if (existingSale.getStatus() == SaleStatus.COMPLETED) {
            throw new SaleBusinessException("No se puede modificar una venta en estado COMPLETADA.");
        }

        updateSaleBasicInfo(existingSale, request);
        updateSaleDetails(existingSale, request.details());

        existingSale.setModifiedDate(LocalDateTime.now());
        SaleDomain updatedSale = saleRepository.save(existingSale);
        return saleMapper.toDto(updatedSale);
    }

    private void updateSaleBasicInfo(SaleDomain existingSale, SalePatchDTO request) {
        if (request.status() != null) {
            existingSale.setStatus(request.status());
        }

        if (request.sellerId() != null) {
            if (request.sellerId().isBlank()) {
                existingSale.setSeller(null);
            } else {
                existingSale.setSeller(resolveSellerIfPresent(request.sellerId()));
            }
        }

        if (request.locationId() != null) {
            existingSale.setLocation(resolveLocationIfPresent(request.locationId()));
        }

        if (request.tableId() != null) {
            if (request.tableId().isBlank()) {
                existingSale.setTableId(null);
            } else {
                validateTableIfPresent(request.tableId());
                existingSale.setTableId(request.tableId());
            }
        }
    }

    private void updateSaleDetails(SaleDomain existingSale, List<SaleDetailDTO> details) {
        if (details == null || details.isEmpty()) {
            return;
        }

        validateSaleLineItems(details);

        existingSale.getDetails().clear();
        BigDecimal totalAmount = processSaleDetails(existingSale, details);
        existingSale.setTotalAmount(totalAmount);
    }

    @Override
    @Transactional
    public void deleteSale(UUID id) {
        if (!saleRepository.existsById(id)) {
            throw new SaleNotFoundException(VENTA_NO_ENCONTRADA + id);
        }
        saleRepository.deleteById(id);
    }

    private void validateSaleLineItems(List<SaleDetailDTO> details) {
        if (details == null) {
            return;
        }
        for (SaleDetailDTO detail : details) {
            if (detail.getQuantity() == null || detail.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new SaleValidationException("La cantidad debe ser mayor que cero en cada línea.");
            }
            if (detail.getRecipeId() == null) {
                if (detail.getUnitPrice() == null || detail.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new SaleValidationException(
                            "En líneas sin receta, el precio unitario es obligatorio y debe ser mayor que cero.");
                }
            }
            if (detail.getRecipeComment() != null && detail.getRecipeComment().length() > 500) {
                throw new SaleValidationException("El comentario de la línea no puede superar los 500 caracteres.");
            }
        }
    }

    private static UUID parseUuidOrThrow(String raw, String campo) {
        try {
            return UUID.fromString(raw.trim());
        } catch (IllegalArgumentException e) {
            throw new SaleValidationException("El identificador del " + campo + " no tiene un formato UUID válido.");
        }
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String t = value.trim();
        return t.isEmpty() ? null : t;
    }

    private static String normalizeRecipeComment(String recipeComment) {
        if (recipeComment == null) {
            return null;
        }
        String trimmed = recipeComment.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static String normalizeLineDisplayName(String name) {
        if (name == null) {
            return null;
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        if (trimmed.length() > MAX_LINE_DISPLAY) {
            return trimmed.substring(0, MAX_LINE_DISPLAY);
        }
        return trimmed;
    }
}
