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
import com.co.eatupapi.repositories.inventory.recipe.RecipeRepository;
import com.co.eatupapi.services.commercial.table.TableService;
import com.co.eatupapi.utils.commercial.sales.exceptions.SaleBusinessException;
import com.co.eatupapi.utils.commercial.sales.exceptions.SaleNotFoundException;
import com.co.eatupapi.utils.commercial.sales.exceptions.SaleValidationException;
import com.co.eatupapi.utils.commercial.sales.mapper.SaleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {

    private static final String SALE_NOT_FOUND_MSG = "Sale not found with id: ";
    private static final String SELLER_NOT_FOUND_MSG = "Seller not found with id: ";
    private static final String RECIPE_NOT_FOUND_MSG = "Recipe not found with id: ";
    private static final String LOCATION_NOT_FOUND_MSG = "Location not found with id: ";

    private final SaleRepository saleRepository;
    private final SellerRepository sellerRepository;
    private final RecipeRepository recipeRepository;
    private final LocationRepository locationRepository;
    private final TableService tableService;
    private final SaleMapper saleMapper;

    public SaleServiceImpl(SaleRepository saleRepository,
                           SellerRepository sellerRepository,
                           RecipeRepository recipeRepository,
                           LocationRepository locationRepository,
                           TableService tableService,
                           SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.sellerRepository = sellerRepository;
        this.recipeRepository = recipeRepository;
        this.locationRepository = locationRepository;
        this.tableService = tableService;
        this.saleMapper = saleMapper;
    }
    @Override
    @Transactional
    public SaleResponseDTO createSale(SaleRequestDTO request) {
        validateRequest(request);

        SellerDomain seller = sellerRepository.findById(UUID.fromString(request.getSellerId()))
                .orElseThrow(() -> new SaleNotFoundException(SELLER_NOT_FOUND_MSG + request.getSellerId()));

        var location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new SaleNotFoundException(LOCATION_NOT_FOUND_MSG + request.getLocationId()));

        if (request.getTableId() != null && !request.getTableId().isBlank()) {
            validateTableExists(request.getTableId());
        }

        SaleDomain sale = new SaleDomain();
        sale.setSeller(seller);
        sale.setLocation(location);
        sale.setTableId(request.getTableId());
        sale.setStatus(SaleStatus.CREATED);
        sale.setCreatedDate(LocalDateTime.now());
        sale.setModifiedDate(LocalDateTime.now());

        BigDecimal totalAmount = processSaleDetails(sale, request.getDetails());

        sale.setTotalAmount(totalAmount);
        SaleDomain savedSale = saleRepository.save(sale);
        return saleMapper.toDto(savedSale);
    }

    private void validateTableExists(String tableId) {
        try {
            tableService.getTableById(tableId);
        } catch (Exception e) {
            throw new SaleNotFoundException("Table not found with id: " + tableId);
        }
    }

    private BigDecimal processSaleDetails(SaleDomain sale, List<SaleDetailDTO> detailDtos) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SaleDetailDTO detailDto : detailDtos) {
            RecipeDomain recipe = recipeRepository.findById(detailDto.getRecipeId())
                    .orElseThrow(() -> new SaleNotFoundException(RECIPE_NOT_FOUND_MSG + detailDto.getRecipeId()));

            SaleDetailDomain detail = new SaleDetailDomain();
            detail.setRecipe(recipe);
            detail.setQuantity(detailDto.getQuantity());
            detail.setUnitPrice(detailDto.getUnitPrice() != null ? detailDto.getUnitPrice() : recipe.getSellingPrice());

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
                .orElseThrow(() -> new SaleNotFoundException(SALE_NOT_FOUND_MSG + id));
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
                .orElseThrow(() -> new SaleNotFoundException(SALE_NOT_FOUND_MSG + id));

        if (existingSale.getStatus() == SaleStatus.COMPLETED) {
            throw new SaleBusinessException("A COMPLETED sale cannot be modified");
        }

        validateRequest(request);

        SellerDomain seller = sellerRepository.findById(UUID.fromString(request.getSellerId()))
                .orElseThrow(() -> new SaleNotFoundException(SELLER_NOT_FOUND_MSG + request.getSellerId()));

        var location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new SaleNotFoundException(LOCATION_NOT_FOUND_MSG + request.getLocationId()));

        if (request.getTableId() != null && !request.getTableId().isBlank()) {
            validateTableExists(request.getTableId());
        }

        existingSale.setSeller(seller);
        existingSale.setLocation(location);
        existingSale.setTableId(request.getTableId());
        existingSale.setModifiedDate(LocalDateTime.now());

        // Clear existing details and add new ones
        existingSale.getDetails().clear();

        BigDecimal totalAmount = processSaleDetails(existingSale, request.getDetails());

        existingSale.setTotalAmount(totalAmount);
        SaleDomain updatedSale = saleRepository.save(existingSale);
        return saleMapper.toDto(updatedSale);
    }

    @Override
    @Transactional
    public SaleResponseDTO patchSale(UUID id, SalePatchDTO request) {
        SaleDomain existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(SALE_NOT_FOUND_MSG + id));

        if (existingSale.getStatus() == SaleStatus.COMPLETED) {
            throw new SaleBusinessException("A COMPLETED sale cannot be modified");
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

        if (request.sellerId() != null && !request.sellerId().isBlank()) {
            SellerDomain seller = sellerRepository.findById(UUID.fromString(request.sellerId()))
                    .orElseThrow(() -> new SaleNotFoundException(SELLER_NOT_FOUND_MSG + request.sellerId()));
            existingSale.setSeller(seller);
        }

        if (request.locationId() != null) {
            var location = locationRepository.findById(request.locationId())
                    .orElseThrow(() -> new SaleNotFoundException(LOCATION_NOT_FOUND_MSG + request.locationId()));
            existingSale.setLocation(location);
        }

        if (request.tableId() != null) {
            if (!request.tableId().isBlank()) {
                validateTableExists(request.tableId());
            }
            existingSale.setTableId(request.tableId().isBlank() ? null : request.tableId());
        }
    }

    private void updateSaleDetails(SaleDomain existingSale, List<SaleDetailDTO> details) {
        if (details == null || details.isEmpty()) {
            return;
        }

        existingSale.getDetails().clear();
        BigDecimal totalAmount = processSaleDetails(existingSale, details);
        existingSale.setTotalAmount(totalAmount);
    }

    @Override
    @Transactional
    public void deleteSale(UUID id) {
        if (!saleRepository.existsById(id)) {
            throw new SaleNotFoundException(SALE_NOT_FOUND_MSG + id);
        }
        saleRepository.deleteById(id);

    }

    private void validateRequest(SaleRequestDTO request) {
        if (request.getSellerId() == null || request.getSellerId().isBlank()) {
            throw new SaleValidationException("Seller ID is required");
        }
        if (request.getLocationId() == null) {
            throw new SaleValidationException("Location ID is required");
        }
        if (request.getDetails() == null || request.getDetails().isEmpty()) {
            throw new SaleBusinessException("A sale must have at least one item");
        }
        for (SaleDetailDTO detail : request.getDetails()) {
            if (detail.getRecipeId() == null) {
                throw new SaleValidationException("Recipe ID is required for all items");
            }
            if (detail.getQuantity() == null || detail.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new SaleValidationException("Quantity must be greater than zero for all items");
            }
        }
    }
}
