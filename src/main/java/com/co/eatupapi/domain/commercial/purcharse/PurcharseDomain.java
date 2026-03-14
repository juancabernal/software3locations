package com.co.eatupapi.domain.commercial.purcharse;

import com.co.eatupapi.controllers.commercial.provider.ProviderController;

import java.time.LocalDateTime;

public class PurcharseDomain {
    private String id;
    private ProviderController ProviderId;
    private int NumberProducts;
    private double total;
    private PurcharseStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
