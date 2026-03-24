package com.co.eatupapi.repositories.payment.invoice;

import com.co.eatupapi.domain.payment.invoice.InvoiceDomain;
import com.co.eatupapi.domain.payment.invoice.InvoiceStatus;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class InvoiceRepository {

    public List<InvoiceDomain> loadInitialInvoices() {

        LocalDateTime now = LocalDateTime.now();

        InvoiceDomain invoice1 = new InvoiceDomain();
        invoice1.setInvoiceId("INV-001");
        invoice1.setInvoiceNumber("F-001");
        invoice1.setStatus(InvoiceStatus.ABIERTA);
        invoice1.setInvoiceDate(now.minusHours(2));
        invoice1.setTableNumber("Mesa 1");
        invoice1.setTableLocation("Primer piso");
        invoice1.setCustomerId("123");
        invoice1.setCustomerName("Juan Pérez");
        invoice1.setCustomerIsFinalConsumer(true);
        invoice1.setDiscountPercentage(new BigDecimal("10"));
        invoice1.setDiscountDescription("Promo cliente frecuente");
        invoice1.setTotalPrice(new BigDecimal("50000"));

        InvoiceDomain invoice2 = new InvoiceDomain();
        invoice2.setInvoiceId("INV-002");
        invoice2.setInvoiceNumber("F-002");
        invoice2.setStatus(InvoiceStatus.CERRADA);
        invoice2.setInvoiceDate(now.minusDays(1));
        invoice2.setTableNumber("Mesa 5");
        invoice2.setTableLocation("Segundo piso");
        invoice2.setCustomerId("456");
        invoice2.setCustomerName("María Gómez");
        invoice2.setCustomerIsFinalConsumer(false);
        invoice2.setDiscountPercentage(new BigDecimal("0"));
        invoice2.setDiscountDescription("Sin descuento");
        invoice2.setTotalPrice(new BigDecimal("120000"));

        InvoiceDomain invoice3 = new InvoiceDomain();
        invoice3.setInvoiceId("INV-003");
        invoice3.setInvoiceNumber("F-003");
        invoice3.setStatus(InvoiceStatus.INACTIVA);
        invoice3.setInvoiceDate(now.minusDays(3));
        invoice3.setTableNumber("Mesa 3");
        invoice3.setTableLocation("Terraza");
        invoice3.setCustomerId("789");
        invoice3.setCustomerName("Carlos Ruiz");
        invoice3.setCustomerIsFinalConsumer(true);
        invoice3.setDiscountPercentage(new BigDecimal("5"));
        invoice3.setDiscountDescription("Descuento especial");
        invoice3.setTotalPrice(new BigDecimal("80000"));

        return List.of(invoice1, invoice2, invoice3);
    }
}