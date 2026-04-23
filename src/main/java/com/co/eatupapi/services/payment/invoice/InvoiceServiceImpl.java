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

    private static final String INVOICE_NOT_FOUND = "Invoice not found";
    private static final String LOCATION_ID_REQUIRED = "Location ID is required";
    private static final String INVOICE_ID_REQUIRED = "Invoice ID is required";
    private static final String SALES_ID_REQUIRED = "Sales ID is required";
    private static final String CUSTOMER_DISCOUNT_ID_REQUIRED = "Customer discount ID is required";
    private static final String DISCOUNT_ID_REQUIRED = "Discount ID is required";
    private static final String REQUEST_REQUIRED = "Request body is required";
    private static final String STATUS_REQUIRED = "Status is required";
    private static final String INVOICE_NUMBER_REQUIRED = "Invoice number is required";
    private static final String LOCATION_ID_MISMATCH = "Location ID must match locationId path parameter";
    private static final String INVOICE_NUMBER_ALREADY_EXISTS = "Invoice number already exists";
    private static final String INVOICE_DOES_NOT_BELONG_TO_LOCATION = "Invoice does not belong to this location";
    private static final String CUSTOMER_DISCOUNT_LOCATION_MISMATCH =
            "Customer discount does not belong to the requested location";
    private static final String INVOICE_ALREADY_HAS_STATUS = "Invoice already has this status";
    private static final String CANCELLED_INVOICE_CANNOT_BE_MODIFIED = "Cancelled invoice cannot be modified";
    private static final String SALE_TOTAL_REQUIRED = "Sale total amount is required to create invoice";

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
        validateCreateRequest(locationId, request);

        String invoiceNumber = normalizeInvoiceNumber(request.getInvoiceNumber());
        validateInvoiceNumberUniqueness(invoiceNumber, locationId);

        SaleResponseDTO sale = resolveSale(request.getSalesId());
        CustomerDiscountDTO customerDiscount = resolveCustomerDiscount(request.getCustomerDiscountId());
        validateCustomerDiscountBelongsToLocation(customerDiscount, locationId);

        DiscountDTO discount = resolveDiscount(customerDiscount.getDiscountId());
        LocationResponseDTO location = resolveLocation(locationId);

        Invoice invoice = buildInvoice(
                invoiceNumber,
                request,
                sale,
                customerDiscount,
                discount,
                location
        );

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    @Override
    public Page<InvoiceResponse> getInvoicesByLocation(UUID locationId, Pageable pageable) {
        validateRequired(locationId, LOCATION_ID_REQUIRED);

        return invoiceRepository.findByLocationId(locationId, pageable)
                .map(invoiceMapper::toResponse);
    }

    @Override
    public InvoiceResponse getInvoiceById(UUID locationId, UUID invoiceId) {
        validateRequired(locationId, LOCATION_ID_REQUIRED);
        validateRequired(invoiceId, INVOICE_ID_REQUIRED);

        Invoice invoice = findInvoiceById(invoiceId);
        validateInvoiceBelongsToLocation(invoice, locationId);

        return invoiceMapper.toResponse(invoice);
    }

    @Override
    public InvoiceResponse updateStatus(UUID locationId, UUID invoiceId, InvoiceStatusUpdateRequest request) {
        validateRequired(locationId, LOCATION_ID_REQUIRED);
        validateRequired(invoiceId, INVOICE_ID_REQUIRED);
        validateStatusUpdateRequest(request);

        Invoice invoice = findInvoiceById(invoiceId);
        validateInvoiceBelongsToLocation(invoice, locationId);
        validateStatusChange(invoice, request.getStatus());

        invoice.setStatus(request.getStatus());

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    private void validateCreateRequest(UUID locationId, InvoiceRequest request) {
        if (request == null) {
            throw new InvoiceValidationException(REQUEST_REQUIRED);
        }

        validateRequired(locationId, LOCATION_ID_REQUIRED);
        validateRequired(request.getLocationId(), LOCATION_ID_REQUIRED);
        validateRequired(request.getSalesId(), SALES_ID_REQUIRED);
        validateRequired(request.getCustomerDiscountId(), CUSTOMER_DISCOUNT_ID_REQUIRED);

        if (!locationId.equals(request.getLocationId())) {
            throw new InvoiceValidationException(LOCATION_ID_MISMATCH);
        }
    }

    private void validateStatusUpdateRequest(InvoiceStatusUpdateRequest request) {
        if (request == null) {
            throw new InvoiceValidationException(REQUEST_REQUIRED);
        }

        if (request.getStatus() == null) {
            throw new InvoiceValidationException(STATUS_REQUIRED);
        }
    }

    private void validateRequired(Object value, String message) {
        if (value == null) {
            throw new InvoiceValidationException(message);
        }
    }

    private void validateInvoiceNumberUniqueness(String invoiceNumber, UUID locationId) {
        if (invoiceRepository.existsByInvoiceNumberAndLocationId(invoiceNumber, locationId)) {
            throw new InvoiceBusinessException(INVOICE_NUMBER_ALREADY_EXISTS);
        }
    }

    private void validateCustomerDiscountBelongsToLocation(CustomerDiscountDTO customerDiscount, UUID locationId) {
        if (!locationId.equals(customerDiscount.getLocationId())) {
            throw new InvoiceBusinessException(CUSTOMER_DISCOUNT_LOCATION_MISMATCH);
        }
    }

    private void validateInvoiceBelongsToLocation(Invoice invoice, UUID locationId) {
        if (!locationId.equals(invoice.getLocationId())) {
            throw new InvoiceBusinessException(INVOICE_DOES_NOT_BELONG_TO_LOCATION);
        }
    }

    private void validateStatusChange(Invoice invoice, InvoiceStatus newStatus) {
        if (invoice.getStatus() == newStatus) {
            throw new InvoiceBusinessException(INVOICE_ALREADY_HAS_STATUS);
        }

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new InvoiceBusinessException(CANCELLED_INVOICE_CANNOT_BE_MODIFIED);
        }
    }

    private Invoice findInvoiceById(UUID invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException(INVOICE_NOT_FOUND));
    }

    private SaleResponseDTO resolveSale(UUID salesId) {
        validateRequired(salesId, SALES_ID_REQUIRED);

        try {
            return saleService.getSaleById(salesId);
        } catch (RuntimeException ex) {
            throw new InvoiceValidationException("Sale not found with id: " + salesId);
        }
    }

    private CustomerDiscountDTO resolveCustomerDiscount(UUID customerDiscountId) {
        validateRequired(customerDiscountId, CUSTOMER_DISCOUNT_ID_REQUIRED);

        Optional<CustomerDiscountDTO> customerDiscount = customerDiscountService.getAllCustomerDiscounts()
                .stream()
                .filter(discount -> customerDiscountId.equals(discount.getId()))
                .findFirst();

        return customerDiscount.orElseThrow(
                () -> new InvoiceValidationException("Customer discount not found with id: " + customerDiscountId)
        );
    }

    private DiscountDTO resolveDiscount(UUID discountId) {
        validateRequired(discountId, DISCOUNT_ID_REQUIRED);

        return discountService.getDiscountById(discountId)
                .orElseThrow(() -> new InvoiceValidationException("Discount not found with id: " + discountId));
    }

    private LocationResponseDTO resolveLocation(UUID locationId) {
        validateRequired(locationId, LOCATION_ID_REQUIRED);

        try {
            return locationService.findById(locationId);
        } catch (RuntimeException ex) {
            throw new InvoiceValidationException("Location not found with id: " + locationId);
        }
    }

    private Invoice buildInvoice(String invoiceNumber,
                                 InvoiceRequest request,
                                 SaleResponseDTO sale,
                                 CustomerDiscountDTO customerDiscount,
                                 DiscountDTO discount,
                                 LocationResponseDTO location) {

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

        return invoice;
    }

    private String normalizeInvoiceNumber(String invoiceNumber) {
        if (invoiceNumber == null || invoiceNumber.isBlank()) {
            throw new InvoiceValidationException(INVOICE_NUMBER_REQUIRED);
        }
        return invoiceNumber.trim();
    }

    private BigDecimal safeAmount(BigDecimal amount) {
        if (amount == null) {
            throw new InvoiceValidationException(SALE_TOTAL_REQUIRED);
        }
        return amount;
    }
}