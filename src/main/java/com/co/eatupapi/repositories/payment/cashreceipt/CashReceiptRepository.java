package com.co.eatupapi.repositories.payment.cashreceipt;

import com.co.eatupapi.domain.payment.cashreceipt.CashReceipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CashReceiptRepository extends JpaRepository<CashReceipt, UUID> {

    Page<CashReceipt> findByLocationId(UUID siteId, Pageable pageable);

}