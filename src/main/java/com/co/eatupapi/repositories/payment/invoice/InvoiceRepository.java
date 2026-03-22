package com.co.eatupapi.repositories.payment.invoice;

import com.co.eatupapi.domain.payment.invoice.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    Page<Invoice> findBySiteId(UUID siteId, Pageable pageable);

}