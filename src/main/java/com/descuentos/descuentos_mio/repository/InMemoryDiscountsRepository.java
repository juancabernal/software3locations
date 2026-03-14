package com.descuentos.descuentos_mio.repository;

import com.descuentos.descuentos_mio.domain.DiscountsDomain;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryDiscountsRepository implements DiscountsRepository {

    private final Map<UUID, DiscountsDomain> storage = new ConcurrentHashMap<>();

    public InMemoryDiscountsRepository() {
        DiscountsDomain first = new DiscountsDomain(
                UUID.randomUUID(),
                UUID.randomUUID(),
                10,
                "Descuento de bienvenida",
                true
        );

        DiscountsDomain second = new DiscountsDomain(
                UUID.randomUUID(),
                UUID.randomUUID(),
                25,
                "Descuento premium",
                true
        );

        storage.put(first.getId(), first);
        storage.put(second.getId(), second);
    }

    @Override
    public List<DiscountsDomain> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<DiscountsDomain> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public DiscountsDomain save(DiscountsDomain discount) {
        UUID id = discount.getId() != null ? discount.getId() : UUID.randomUUID();
        discount.setId(id);
        storage.put(id, discount);
        return discount;
    }

    @Override
    public Optional<DiscountsDomain> update(UUID id, DiscountsDomain discount) {
        if (!storage.containsKey(id)) {
            return Optional.empty();
        }
        discount.setId(id);
        storage.put(id, discount);
        return Optional.of(discount);
    }
}
