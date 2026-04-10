package com.co.eatupapi.services.commercial.sales;

import com.co.eatupapi.domain.commercial.sales.SaleDetailDomain;
import com.co.eatupapi.domain.commercial.sales.SaleDomain;
import com.co.eatupapi.domain.commercial.sales.SaleStatus;
import com.co.eatupapi.domain.commercial.seller.SellerDomain;
import com.co.eatupapi.domain.inventory.product.Product;
import com.co.eatupapi.dto.commercial.sales.SaleDetailDTO;
import com.co.eatupapi.dto.commercial.sales.SalePatchDTO;
import com.co.eatupapi.dto.commercial.sales.SaleRequestDTO;
import com.co.eatupapi.dto.commercial.sales.SaleResponseDTO;
import com.co.eatupapi.repositories.commercial.sales.SaleRepository;
import com.co.eatupapi.repositories.commercial.seller.SellerRepository;
import com.co.eatupapi.repositories.inventory.product.ProductRepository;
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
    private final SaleRepository saleRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final TableService tableService;
    private final SaleMapper saleMapper;

    public SaleServiceImpl(SaleRepository saleRepository,
                           SellerRepository sellerRepository,
                           ProductRepository productRepository,
                           TableService tableService,
                           SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.sellerRepository = sellerRepository;
        this.productRepository = productRepository;
        this.tableService = tableService;
        this.saleMapper = saleMapper;
    }
    @Override
    @Transactional
    public SaleResponseDTO createSale(SaleRequestDTO request) {
        validateRequest(request);

        SellerDomain seller = sellerRepository.findById(UUID.fromString(request.getSellerId()))
                .orElseThrow(() -> new SaleNotFoundException("Seller not found with id: " + request.getSellerId()));

        if (request.getTableId() != null && !request.getTableId().isBlank()) {
            try {
                tableService.getTableById(request.getTableId());
            } catch (Exception e) {
                throw new SaleNotFoundException("Table not found with id: " + request.getTableId());
            }
        }

        SaleDomain sale = new SaleDomain();
        sale.setSeller(seller);
        sale.setTableId(request.getTableId());
        sale.setStatus(SaleStatus.CREATED);
        sale.setCreatedDate(LocalDateTime.now());
        sale.setModifiedDate(LocalDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SaleDetailDTO detailDto : request.getDetails()) {
            Product product = productRepository.findById(detailDto.getProductId())
                    .orElseThrow(() -> new SaleNotFoundException("Product not found with id: " + detailDto.getProductId()));

            SaleDetailDomain detail = new SaleDetailDomain();
            detail.setProduct(product);
            detail.setQuantity(detailDto.getQuantity());
            detail.setUnitPrice(detailDto.getUnitPrice() != null ? detailDto.getUnitPrice() : product.getSalePrice());

            BigDecimal subtotal = detail.getUnitPrice().multiply(detail.getQuantity());
            detail.setSubtotal(subtotal);

            sale.addDetail(detail);
            totalAmount = totalAmount.add(subtotal);
        }

        sale.setTotalAmount(totalAmount);
        SaleDomain savedSale = saleRepository.save(sale);
        return saleMapper.toDto(savedSale);
    }

    @Override
    public SaleResponseDTO getSaleById(UUID id) {
        SaleDomain sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with id: " + id));
        return saleMapper.toDto(sale);
    }

    @Override
    public List<SaleResponseDTO> getAllSales() {
        return saleRepository.findAll().stream()
                .map(saleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SaleResponseDTO updateSale(UUID id, SaleRequestDTO request) {
        SaleDomain existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with id: " + id));

        if (existingSale.getStatus() == SaleStatus.COMPLETED) {
            throw new SaleBusinessException("A COMPLETED sale cannot be modified");
        }

        validateRequest(request);

        SellerDomain seller = sellerRepository.findById(UUID.fromString(request.getSellerId()))
                .orElseThrow(() -> new SaleNotFoundException("Seller not found with id: " + request.getSellerId()));

        if (request.getTableId() != null && !request.getTableId().isBlank()) {
            try {
                tableService.getTableById(request.getTableId());
            } catch (Exception e) {
                throw new SaleNotFoundException("Table not found with id: " + request.getTableId());
            }
        }

        existingSale.setSeller(seller);
        existingSale.setTableId(request.getTableId());
        existingSale.setModifiedDate(LocalDateTime.now());

        // Clear existing details and add new ones
        existingSale.getDetails().clear();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (SaleDetailDTO detailDto : request.getDetails()) {
            Product product = productRepository.findById(detailDto.getProductId())
                    .orElseThrow(() -> new SaleNotFoundException("Product not found with id: " + detailDto.getProductId()));

            SaleDetailDomain detail = new SaleDetailDomain();
            detail.setProduct(product);
            detail.setQuantity(detailDto.getQuantity());
            detail.setUnitPrice(detailDto.getUnitPrice() != null ? detailDto.getUnitPrice() : product.getSalePrice());

            BigDecimal subtotal = detail.getUnitPrice().multiply(detail.getQuantity());
            detail.setSubtotal(subtotal);

            existingSale.addDetail(detail);
            totalAmount = totalAmount.add(subtotal);
        }

        existingSale.setTotalAmount(totalAmount);
        SaleDomain updatedSale = saleRepository.save(existingSale);
        return saleMapper.toDto(updatedSale);
    }

    @Override
    @Transactional
    public SaleResponseDTO patchSale(UUID id, SalePatchDTO request) {
        SaleDomain existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with id: " + id));

        if (existingSale.getStatus() == SaleStatus.COMPLETED) {
            throw new SaleBusinessException("A COMPLETED sale cannot be modified");
        }

        if (request.status() != null) {
            existingSale.setStatus(request.status());
        }

        if (request.sellerId() != null && !request.sellerId().isBlank()) {
            SellerDomain seller = sellerRepository.findById(UUID.fromString(request.sellerId()))
                    .orElseThrow(() -> new SaleNotFoundException("Seller not found with id: " + request.sellerId()));
            existingSale.setSeller(seller);
        }

        if (request.tableId() != null) {
            if (!request.tableId().isBlank()) {
                try {
                    tableService.getTableById(request.tableId());
                } catch (Exception e) {
                    throw new SaleNotFoundException("Table not found with id: " + request.tableId());
                }
            }
            existingSale.setTableId(request.tableId().isBlank() ? null : request.tableId());
        }

        if (request.details() != null && !request.details().isEmpty()) {
            existingSale.getDetails().clear();
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (SaleDetailDTO detailDto : request.details()) {
                if (detailDto.getProductId() == null) {
                    throw new SaleValidationException("Product ID is required for all items");
                }
                if (detailDto.getQuantity() == null || detailDto.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new SaleValidationException("Quantity must be greater than zero for all items");
                }

                Product product = productRepository.findById(detailDto.getProductId())
                        .orElseThrow(() -> new SaleNotFoundException("Product not found with id: " + detailDto.getProductId()));

                SaleDetailDomain detail = new SaleDetailDomain();
                detail.setProduct(product);
                detail.setQuantity(detailDto.getQuantity());
                detail.setUnitPrice(detailDto.getUnitPrice() != null ? detailDto.getUnitPrice() : product.getSalePrice());

                BigDecimal subtotal = detail.getUnitPrice().multiply(detail.getQuantity());
                detail.setSubtotal(subtotal);

                existingSale.addDetail(detail);
                totalAmount = totalAmount.add(subtotal);
            }
            existingSale.setTotalAmount(totalAmount);
        }

        existingSale.setModifiedDate(LocalDateTime.now());
        SaleDomain updatedSale = saleRepository.save(existingSale);
        return saleMapper.toDto(updatedSale);
    }

    @Override
    @Transactional
    public void deleteSale(UUID id) {
        if (!saleRepository.existsById(id)) {
            throw new SaleNotFoundException("Sale not found with id: " + id);
        }
        saleRepository.deleteById(id);

    }

    private void validateRequest(SaleRequestDTO request) {
        if (request.getSellerId() == null || request.getSellerId().isBlank()) {
            throw new SaleValidationException("Seller ID is required");
        }
        if (request.getDetails() == null || request.getDetails().isEmpty()) {
            throw new SaleBusinessException("A sale must have at least one item");
        }
        for (SaleDetailDTO detail : request.getDetails()) {
            if (detail.getProductId() == null) {
                throw new SaleValidationException("Product ID is required for all items");
            }
            if (detail.getQuantity() == null || detail.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new SaleValidationException("Quantity must be greater than zero for all items");
            }
        }
    }
}
