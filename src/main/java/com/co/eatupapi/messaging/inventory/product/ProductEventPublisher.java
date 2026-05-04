package com.co.eatupapi.messaging.inventory.product;

import com.co.eatupapi.dto.inventory.product.ProductRequestDTO;

public interface ProductEventPublisher {
    void publishCreateRequested(ProductRequestDTO request);
}
