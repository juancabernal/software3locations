package com.co.eatupapi.services.payment.invoice;

import com.co.eatupapi.dto.payment.invoice.InvoiceRequest;
import com.co.eatupapi.dto.payment.invoice.InvoiceResponse;
import com.co.eatupapi.dto.payment.invoice.InvoiceStatusUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface InvoiceService {

    InvoiceResponse createInvoice(UUID siteId, InvoiceRequest request);

    Page<InvoiceResponse> getInvoicesBySite(UUID siteId, Pageable pageable);

    InvoiceResponse getInvoiceById(UUID siteId, UUID invoiceId);

    InvoiceResponse updateStatus(UUID siteId, UUID invoiceId, InvoiceStatusUpdateRequest request);
}