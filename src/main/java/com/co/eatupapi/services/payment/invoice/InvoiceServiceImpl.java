package com.co.eatupapi.services.payment.invoice;

import com.co.eatupapi.domain.payment.invoice.Invoice;
import com.co.eatupapi.domain.payment.invoice.InvoiceStatus;
import com.co.eatupapi.dto.payment.invoice.InvoiceRequest;
import com.co.eatupapi.dto.payment.invoice.InvoiceResponse;
import com.co.eatupapi.dto.payment.invoice.InvoiceStatusUpdateRequest;
import com.co.eatupapi.repositories.payment.invoice.InvoiceRepository;
import com.co.eatupapi.utils.payment.invoice.exceptions.InvoiceBusinessException;
import com.co.eatupapi.utils.payment.invoice.exceptions.InvoiceNotFoundException;
import com.co.eatupapi.utils.payment.invoice.mapper.InvoiceMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    public InvoiceResponse createInvoice(UUID siteId, InvoiceRequest request) {

        Invoice invoice = new Invoice();
        invoice.setId(UUID.randomUUID());
        invoice.setSiteId(siteId);
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setTableId(request.getTableId());
        invoice.setCustomerId(request.getCustomerId());
        invoice.setDiscountPercentage(request.getDiscountPercentage());
        invoice.setDiscountDescription(request.getDiscountDescription());

        invoice.setStatus(InvoiceStatus.OPEN);
        invoice.setInvoiceDate(LocalDateTime.now());

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    @Override
    public Page<InvoiceResponse> getInvoicesBySite(UUID siteId, Pageable pageable) {

        return invoiceRepository
                .findBySiteId(siteId, pageable)
                .map(invoiceMapper::toResponse);
    }

    @Override
    public InvoiceResponse getInvoiceById(UUID siteId, UUID invoiceId) {

        Invoice invoice = invoiceRepository
                .findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));

        if (!invoice.getSiteId().equals(siteId)) {
            throw new InvoiceBusinessException("Invoice does not belong to this site");
        }

        return invoiceMapper.toResponse(invoice);
    }

    @Override
    public InvoiceResponse updateStatus(UUID siteId, UUID invoiceId, InvoiceStatusUpdateRequest request) {

        Invoice invoice = invoiceRepository
                .findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));

        if (!invoice.getSiteId().equals(siteId)) {
            throw new InvoiceBusinessException("Invoice does not belong to this site");
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
}