package com.co.eatupapi.services.payment.cashreceipt;

import com.co.eatupapi.domain.payment.cashreceipt.CashReceipt;
import com.co.eatupapi.domain.payment.cashreceipt.CashReceiptStatus;
import com.co.eatupapi.dto.payment.cashreceipt.CashReceiptResponse;
import com.co.eatupapi.dto.payment.cashreceipt.CreateCashReceiptRequest;
import com.co.eatupapi.repositories.payment.cashreceipt.CashReceiptRepository;
import com.co.eatupapi.utils.payment.cashreceipt.exceptions.CashReceiptBusinessException;
import com.co.eatupapi.utils.payment.cashreceipt.exceptions.CashReceiptNotFoundException;
import com.co.eatupapi.utils.payment.cashreceipt.exceptions.ErrorCode;
import com.co.eatupapi.utils.payment.cashreceipt.mapper.CashReceiptMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CashReceiptServiceImpl implements CashReceiptService {

    private final CashReceiptRepository cashReceiptRepository;
    private final CashReceiptMapper cashReceiptMapper;

    public CashReceiptServiceImpl(CashReceiptRepository cashReceiptRepository,
                                  CashReceiptMapper cashReceiptMapper) {
        this.cashReceiptRepository = cashReceiptRepository;
        this.cashReceiptMapper = cashReceiptMapper;
    }

    @Override
    public CashReceiptResponse createCashReceipt(UUID siteId, CreateCashReceiptRequest request) {

        CashReceipt receipt = new CashReceipt();
        receipt.setSiteId(siteId);
        receipt.setInvoiceId(request.getInvoiceId());
        receipt.setAmount(request.getAmount());
        receipt.setPaymentMethod(request.getPaymentMethod());
        receipt.setStatus(CashReceiptStatus.PAID);
        receipt.setCreatedAt(LocalDateTime.now());

        return cashReceiptMapper.toResponse(cashReceiptRepository.save(receipt));
    }

    @Override
    public Page<CashReceiptResponse> getCashReceiptsBySite(UUID siteId, Pageable pageable) {

        return cashReceiptRepository
                .findBySiteId(siteId, pageable)
                .map(cashReceiptMapper::toResponse);
    }

    @Override
    public CashReceiptResponse cancelCashReceipt(UUID siteId, UUID receiptId) {
        CashReceipt receipt = cashReceiptRepository
                .findById(receiptId)
                .orElseThrow(() -> new CashReceiptNotFoundException("Cash receipt not found"));

        if (!receipt.getSiteId().equals(siteId)) {
            throw new CashReceiptBusinessException(
                    ErrorCode.RECEIPT_DOES_NOT_BELONG_TO_SITE,
                    "Receipt does not belong to this site"
            );
        }

        if (receipt.getStatus() == CashReceiptStatus.CANCELLED) {
            throw new CashReceiptBusinessException(
                    ErrorCode.CASH_RECEIPT_ALREADY_CANCELLED,
                    "Cash receipt already cancelled"
            );
        }

        receipt.setStatus(CashReceiptStatus.CANCELLED);
        receipt.setCancelledAt(LocalDateTime.now());

        return cashReceiptMapper.toResponse(cashReceiptRepository.save(receipt));
    }
}
