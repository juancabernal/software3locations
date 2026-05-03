package com.co.eatupapi.services.payment.cashreceipt;

import com.co.eatupapi.dto.payment.cashreceipt.CashReceiptResponse;
import com.co.eatupapi.dto.payment.cashreceipt.CreateCashReceiptRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CashReceiptService {

    void createCashReceipt(UUID locationId, CreateCashReceiptRequest request);

    Page<CashReceiptResponse> getCashReceiptsBySite(UUID locationId, Pageable pageable);

    void cancelCashReceipt(UUID locationId, UUID receiptId);

}
