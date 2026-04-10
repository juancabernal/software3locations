package com.co.eatupapi.services.payment.invoice;

import com.co.eatupapi.dto.payment.invoice.InvoiceRequest;
import com.co.eatupapi.dto.payment.invoice.InvoiceResponse;
import com.co.eatupapi.dto.payment.invoice.InvoiceStatusUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface InvoiceService {

    InvoiceResponse createInvoice(UUID locationId, InvoiceRequest request);

    Page<InvoiceResponse> getInvoicesByLocation(UUID locationId, Pageable pageable);

    InvoiceResponse getInvoiceById(UUID locationId, UUID invoiceId);

    InvoiceResponse updateStatus(UUID locationId, UUID invoiceId, InvoiceStatusUpdateRequest request);
}
