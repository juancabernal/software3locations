package com.descuentos.descuentos_mio.client;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerServiceClient {

    private static final UUID CUSTOMER_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID CUSTOMER_ID_2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID CUSTOMER_ID_3 = UUID.fromString("33333333-3333-3333-3333-333333333333");

    private final List<UUID> customerIds = List.of(CUSTOMER_ID_1, CUSTOMER_ID_2, CUSTOMER_ID_3);

    public Optional<UUID> findCustomerById(UUID customerId) {
        return customerIds.stream()
                .filter(id -> id.equals(customerId))
                .findFirst();
    }

    public List<UUID> getAvailableCustomerIds() {
        return customerIds;
    }
}
