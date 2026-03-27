package com.co.eatupapi.utils.payment.cashreceipt.mapper;

import com.co.eatupapi.domain.payment.cashreceipt.CashReceipt;
import com.co.eatupapi.dto.payment.cashreceipt.CashReceiptResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CashReceiptMapper {

    @Mapping(target = "status", expression = "java(receipt.getStatus().name())")
    CashReceiptResponse toResponse(CashReceipt receipt);

}
