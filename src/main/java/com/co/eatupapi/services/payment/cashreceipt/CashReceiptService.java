package com.co.eatupapi.services.payment.cashreceipt;

import com.co.eatupapi.dto.payment.cashreceipt.CashReceiptResponse;
import com.co.eatupapi.dto.payment.cashreceipt.CreateCashReceiptRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CashReceiptService {

    CashReceiptResponse createCashReceipt(UUID siteId, CreateCashReceiptRequest request);

    Page<CashReceiptResponse> getCashReceiptsBySite(UUID siteId, Pageable pageable);

    CashReceiptResponse cancelCashReceipt(UUID siteId, UUID receiptId);

}
