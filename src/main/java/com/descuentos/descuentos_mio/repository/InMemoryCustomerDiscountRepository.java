package com.descuentos.descuentos_mio.repository;

import com.descuentos.descuentos_mio.domain.CustomerDiscountDomain;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCustomerDiscountRepository implements CustomerDiscountRepository {

    private static final UUID LOCATION_ID_1 = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private static final UUID CUSTOMER_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DISCOUNT_ID_1 = UUID.fromString("90000000-0000-0000-0000-000000000001");

    private final Map<UUID, CustomerDiscountDomain> storage = new ConcurrentHashMap<>();

    public InMemoryCustomerDiscountRepository() {
        CustomerDiscountDomain first = new CustomerDiscountDomain(
                UUID.fromString("70000000-0000-0000-0000-000000000001"),
                LOCATION_ID_1,
                CUSTOMER_ID_1,
                DISCOUNT_ID_1,
                LocalDate.now()
        );

        storage.put(first.getId(), first);
    }

    @Override
    public List<CustomerDiscountDomain> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<CustomerDiscountDomain> findByCustomerId(UUID customerId) {
        return storage.values().stream()
                .filter(customerDiscount -> customerDiscount.getCustomerId().equals(customerId))
                .toList();
    }

    @Override
    public Optional<CustomerDiscountDomain> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public CustomerDiscountDomain save(CustomerDiscountDomain customerDiscount) {
        UUID id = customerDiscount.getId() != null ? customerDiscount.getId() : UUID.randomUUID();
        customerDiscount.setId(id);
        storage.put(id, customerDiscount);
        return customerDiscount;
    }

    @Override
    public Optional<CustomerDiscountDomain> update(UUID id, CustomerDiscountDomain customerDiscount) {
        if (!storage.containsKey(id)) {
            return Optional.empty();
        }
        customerDiscount.setId(id);
        storage.put(id, customerDiscount);
        return Optional.of(customerDiscount);
    }

    @Override
    public boolean deleteById(UUID id) {
        return storage.remove(id) != null;
    }
}
