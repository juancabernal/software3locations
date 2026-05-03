package com.co.eatupapi.services.payment.cashreceipt.impl;

import com.co.eatupapi.dto.payment.cashreceipt.CashReceiptResponse;
import com.co.eatupapi.dto.payment.cashreceipt.CreateCashReceiptRequest;
import com.co.eatupapi.messaging.payment.cashreceipt.CashReceiptCancelMessage;
import com.co.eatupapi.messaging.payment.cashreceipt.CashReceiptCreateMessage;
import com.co.eatupapi.messaging.payment.cashreceipt.CashReceiptMessagePublisher;
import com.co.eatupapi.repositories.payment.cashreceipt.CashReceiptRepository;
import com.co.eatupapi.services.payment.cashreceipt.CashReceiptService;
import com.co.eatupapi.utils.payment.cashreceipt.mapper.CashReceiptMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CashReceiptServiceImpl implements CashReceiptService {

    private final CashReceiptRepository cashReceiptRepository;
    private final CashReceiptMapper cashReceiptMapper;
    private final CashReceiptMessagePublisher cashReceiptMessagePublisher;

    public CashReceiptServiceImpl(CashReceiptRepository cashReceiptRepository,
                                  CashReceiptMapper cashReceiptMapper,
                                  CashReceiptMessagePublisher cashReceiptMessagePublisher) {
        this.cashReceiptRepository = cashReceiptRepository;
        this.cashReceiptMapper = cashReceiptMapper;
        this.cashReceiptMessagePublisher = cashReceiptMessagePublisher;
    }

    @Override
    public void createCashReceipt(UUID locationId, CreateCashReceiptRequest request) {
        CashReceiptCreateMessage event = new CashReceiptCreateMessage(
                locationId,
                request.getInvoiceId(),
                request.getAmount(),
                request.getPaymentMethodId()
        );
        cashReceiptMessagePublisher.publishCreate(event);
    }

    @Override
    public Page<CashReceiptResponse> getCashReceiptsBySite(UUID locationId, Pageable pageable) {

        return cashReceiptRepository
                .findByLocationId(locationId, pageable)
                .map(cashReceiptMapper::toResponse);
    }

    @Override
    public void cancelCashReceipt(UUID locationId, UUID receiptId) {
        CashReceiptCancelMessage event = new CashReceiptCancelMessage(locationId, receiptId);
        cashReceiptMessagePublisher.publishCancel(event);
    }
}
