package com.co.eatupapi.utils.payment.invoice.mapper;

import com.co.eatupapi.domain.payment.invoice.InvoiceDomain;
import com.co.eatupapi.dto.payment.invoice.InvoiceDTO;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    public InvoiceDTO toDto(InvoiceDomain domain) {
        InvoiceDTO dto = new InvoiceDTO();

        dto.setInvoiceId(domain.getInvoiceId());
        dto.setInvoiceNumber(domain.getInvoiceNumber());
        dto.setStatus(domain.getStatus());
        dto.setInvoiceDate(domain.getInvoiceDate());

        dto.setTableNumber(domain.getTableNumber());
        dto.setTableLocation(domain.getTableLocation());

        dto.setCustomerId(domain.getCustomerId());
        dto.setCustomerName(domain.getCustomerName());
        dto.setCustomerIsFinalConsumer(domain.getCustomerIsFinalConsumer());

        dto.setDiscountPercentage(domain.getDiscountPercentage());
        dto.setDiscountDescription(domain.getDiscountDescription());

        dto.setTotalPrice(domain.getTotalPrice());

        return dto;
    }

    public InvoiceDomain toDomain(InvoiceDTO dto) {
        InvoiceDomain domain = new InvoiceDomain();

        domain.setInvoiceId(dto.getInvoiceId());
        domain.setInvoiceNumber(dto.getInvoiceNumber());
        domain.setStatus(dto.getStatus());
        domain.setInvoiceDate(dto.getInvoiceDate());

        domain.setTableNumber(dto.getTableNumber());
        domain.setTableLocation(dto.getTableLocation());

        domain.setCustomerId(dto.getCustomerId());
        domain.setCustomerName(dto.getCustomerName());
        domain.setCustomerIsFinalConsumer(dto.getCustomerIsFinalConsumer());

        domain.setDiscountPercentage(dto.getDiscountPercentage());
        domain.setDiscountDescription(dto.getDiscountDescription());

        domain.setTotalPrice(dto.getTotalPrice());

        return domain;
    }
}