package com.co.eatupapi.services.payment.invoice;

import com.co.eatupapi.domain.payment.invoice.Invoice;
import com.co.eatupapi.domain.payment.invoice.InvoiceStatus;
import com.co.eatupapi.dto.commercial.customerDiscount.CustomerDiscountDTO;
import com.co.eatupapi.dto.commercial.discount.DiscountDTO;
import com.co.eatupapi.dto.commercial.sales.SaleResponseDTO;
import com.co.eatupapi.dto.inventory.location.LocationResponseDTO;
import com.co.eatupapi.dto.payment.invoice.InvoiceRequest;
import com.co.eatupapi.dto.payment.invoice.InvoiceResponse;
import com.co.eatupapi.dto.payment.invoice.InvoiceStatusUpdateRequest;
import com.co.eatupapi.repositories.payment.invoice.InvoiceRepository;
import com.co.eatupapi.services.commercial.customerDiscount.CustomerDiscountService;
import com.co.eatupapi.services.commercial.discount.DiscountService;
import com.co.eatupapi.services.commercial.sales.SaleService;
import com.co.eatupapi.services.inventory.location.LocationService;
import com.co.eatupapi.utils.payment.invoice.exceptions.InvoiceBusinessException;
import com.co.eatupapi.utils.payment.invoice.exceptions.InvoiceNotFoundException;
import com.co.eatupapi.utils.payment.invoice.exceptions.InvoiceValidationException;
import com.co.eatupapi.utils.payment.invoice.mapper.InvoiceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final SaleService saleService;
    private final CustomerDiscountService customerDiscountService;
    private final DiscountService discountService;
    private final LocationService locationService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              InvoiceMapper invoiceMapper,
                              SaleService saleService,
                              CustomerDiscountService customerDiscountService,
                              DiscountService discountService,
                              LocationService locationService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.saleService = saleService;
        this.customerDiscountService = customerDiscountService;
        this.discountService = discountService;
        this.locationService = locationService;
    }

    @Override
    public InvoiceResponse createInvoice(UUID locationId, InvoiceRequest request) {

        String invoiceNumber = normalizeInvoiceNumber(request.getInvoiceNumber());

        if (!locationId.equals(request.getLocationId())) {
            throw new InvoiceValidationException("Location ID must match locationId path parameter");
        }

        if (invoiceRepository.existsByInvoiceNumberAndLocationId(invoiceNumber, locationId)) {
            throw new InvoiceBusinessException("Invoice number already exists");
        }

        SaleResponseDTO sale = resolveSale(request.getSalesId());
        CustomerDiscountDTO customerDiscount = resolveCustomerDiscount(request.getCustomerDiscountId());
        LocationResponseDTO location = resolveLocation(request.getLocationId());

        if (!customerDiscount.getLocationId().equals(request.getLocationId())) {
            throw new InvoiceBusinessException("Customer discount does not belong to the requested location");
        }

        DiscountDTO discount = resolveDiscount(customerDiscount.getDiscountId());


        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setSalesId(request.getSalesId());
        invoice.setCustomerDiscountId(request.getCustomerDiscountId());
        invoice.setLocationId(request.getLocationId());

        invoice.setTableId(sale.getTableId());
        invoice.setTotalPrice(safeAmount(sale.getTotalAmount()));

        invoice.setCustomerId(customerDiscount.getCustomerId());
        invoice.setDiscountId(customerDiscount.getDiscountId());
        invoice.setDiscountPercentage(BigDecimal.valueOf(discount.getPercentage()));
        invoice.setDiscountDescription(discount.getDescription());

        invoice.setLocationName(location.getName());

        invoice.setStatus(InvoiceStatus.OPEN);
        invoice.setInvoiceDate(LocalDateTime.now());

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    @Override
    public Page<InvoiceResponse> getInvoicesByLocation(UUID locationId, Pageable pageable) {

        return invoiceRepository
                .findByLocationId(locationId, pageable)
                .map(invoiceMapper::toResponse);
    }

    @Override
    public InvoiceResponse getInvoiceById(UUID locationId, UUID invoiceId) {

        Invoice invoice = invoiceRepository
                .findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));

        if (!invoice.getLocationId().equals(locationId)) {
            throw new InvoiceBusinessException("Invoice does not belong to this location");
        }

        return invoiceMapper.toResponse(invoice);
    }

    @Override
    public InvoiceResponse updateStatus(UUID locationId, UUID invoiceId, InvoiceStatusUpdateRequest request) {

        Invoice invoice = invoiceRepository
                .findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));

        if (!invoice.getLocationId().equals(locationId)) {
            throw new InvoiceBusinessException("Invoice does not belong to this location");
        }

        if (invoice.getStatus() == request.getStatus()) {
            throw new InvoiceBusinessException("Invoice already has this status");
        }

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new InvoiceBusinessException("Cancelled invoice cannot be modified");
        }

        invoice.setStatus(request.getStatus());

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    private SaleResponseDTO resolveSale(UUID salesId) {
        try {
            return saleService.getSaleById(salesId);
        } catch (RuntimeException ex) {
            throw new InvoiceValidationException("Sale not found with id: " + salesId);
        }
    }

    private CustomerDiscountDTO resolveCustomerDiscount(UUID customerDiscountId) {
        Optional<CustomerDiscountDTO> customerDiscount = customerDiscountService
                .getAllCustomerDiscounts()
                .stream()
                .filter(discount -> customerDiscountId.equals(discount.getId()))
                .findFirst();

        return customerDiscount.orElseThrow(
                () -> new InvoiceValidationException("Customer discount not found with id: " + customerDiscountId)
        );
    }

    private DiscountDTO resolveDiscount(UUID discountId) {
        return discountService
                .getDiscountById(discountId)
                .orElseThrow(() -> new InvoiceValidationException("Discount not found with id: " + discountId));
    }

    private LocationResponseDTO resolveLocation(UUID locationId) {
        try {
            return locationService.findById(locationId.toString());
        } catch (RuntimeException ex) {
            throw new InvoiceValidationException("Location not found with id: " + locationId);
        }
    }

    private String normalizeInvoiceNumber(String invoiceNumber) {
        if (invoiceNumber == null || invoiceNumber.isBlank()) {
            throw new InvoiceValidationException("Invoice number is required");
        }
        return invoiceNumber.trim();
    }

    private BigDecimal safeAmount(BigDecimal amount) {
        if (amount == null) {
            throw new InvoiceValidationException("Sale total amount is required to create invoice");
        }
        return amount;
    }
}
