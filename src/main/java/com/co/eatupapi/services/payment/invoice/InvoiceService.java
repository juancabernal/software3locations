package com.co.eatupapi.services.payment.invoice;

import com.co.eatupapi.domain.payment.invoice.InvoiceDomain;
import com.co.eatupapi.domain.payment.invoice.InvoiceStatus;
import com.co.eatupapi.dto.payment.invoice.InvoiceDTO;
import com.co.eatupapi.repositories.payment.invoice.InvoiceRepository;
import com.co.eatupapi.utils.payment.invoice.exceptions.*;
import com.co.eatupapi.utils.payment.invoice.mapper.InvoiceMapper;
import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final InvoiceRepository repository;
    private final InvoiceMapper mapper;
    private final List<InvoiceDomain> invoices = new ArrayList<>();

    public InvoiceService(InvoiceRepository repository, InvoiceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @PostConstruct
    public void init() {
        invoices.clear();
        invoices.addAll(repository.loadInitialInvoices());
    }

    public InvoiceDTO createInvoice(InvoiceDTO request) {
        validate(request);

        InvoiceDomain domain = mapper.toDomain(request);
        domain.setInvoiceId(UUID.randomUUID().toString());
        domain.setStatus(InvoiceStatus.ABIERTA);
        domain.setInvoiceDate(LocalDateTime.now());

        invoices.add(domain);
        return mapper.toDto(domain);
    }

    public InvoiceDTO getById(String id) {
        return mapper.toDto(findById(id));
    }

    public List<InvoiceDTO> getAll(String status) {
        InvoiceStatus parsed = parseStatus(status);
        List<InvoiceDTO> result = new ArrayList<>();

        for (InvoiceDomain inv : invoices) {
            if (parsed == null || inv.getStatus() == parsed) {
                result.add(mapper.toDto(inv));
            }
        }
        return result;
    }

    public InvoiceDTO updateStatus(String id, String status) {
        InvoiceDomain invoice = findById(id);
        InvoiceStatus newStatus = parseRequiredStatus(status);

        validateTransition(invoice.getStatus(), newStatus);

        invoice.setStatus(newStatus);
        return mapper.toDto(invoice);
    }



    private InvoiceDomain findById(String id) {
        for (InvoiceDomain inv : invoices) {
            if (inv.getInvoiceId().equals(id)) {
                return inv;
            }
        }
        throw new InvoiceNotFoundException("Invoice not found: " + id);
    }

    private InvoiceStatus parseStatus(String status) {
        if (status == null || status.isBlank()) return null;
        try {
            return InvoiceStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new InvoiceValidationException("Invalid status");
        }
    }

    private InvoiceStatus parseRequiredStatus(String status) {
        InvoiceStatus s = parseStatus(status);
        if (s == null) throw new InvoiceValidationException("Status required");
        return s;
    }

    private void validate(InvoiceDTO dto) {
        if (dto.getInvoiceNumber() == null || dto.getInvoiceNumber().isBlank()) {
            throw new InvoiceValidationException("invoiceNumber is required");
        }

        if (dto.getTotalPrice() == null || dto.getTotalPrice().doubleValue() < 0) {
            throw new InvoiceValidationException("Invalid totalPrice");
        }
    }


    private void validateTransition(InvoiceStatus current, InvoiceStatus next) {

        if (current == InvoiceStatus.CERRADA) {
            throw new InvoiceBusinessException("Closed invoice cannot change");
        }

        if (current == InvoiceStatus.INACTIVA) {
            throw new InvoiceBusinessException("Inactive invoice cannot change");
        }

        if (next == InvoiceStatus.ABIERTA && current != InvoiceStatus.ABIERTA) {
            throw new InvoiceBusinessException("Cannot revert to ABIERTA");
        }
    }
}