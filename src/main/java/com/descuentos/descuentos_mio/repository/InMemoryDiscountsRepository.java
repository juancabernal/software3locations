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

    private static final UUID CATEGORY_ID_1 = UUID.fromString("10f11111-1111-1111-1111-111111111111");
    private static final UUID CATEGORY_ID_2 = UUID.fromString("20f22222-2222-2222-2222-222222222222");
    private static final UUID DISCOUNT_ID_1 = UUID.fromString("90000000-0000-0000-0000-000000000001");
    private static final UUID DISCOUNT_ID_2 = UUID.fromString("90000000-0000-0000-0000-000000000002");

    private final Map<UUID, DiscountsDomain> storage = new ConcurrentHashMap<>();

    public InMemoryDiscountsRepository() {
        DiscountsDomain first = new DiscountsDomain(
                DISCOUNT_ID_1,
                CATEGORY_ID_1,
                10,
                "Descuento de bienvenida",
                true
        );

        DiscountsDomain second = new DiscountsDomain(
                DISCOUNT_ID_2,
                CATEGORY_ID_2,
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

    @Override
    public Optional<DiscountsDomain> updateStatus(UUID id, Boolean status) {
        DiscountsDomain current = storage.get(id);
        if (current == null) {
            return Optional.empty();
        }
        current.setStatus(status);
        storage.put(id, current);
        return Optional.of(current);
    }

    @Override
    public boolean deleteById(UUID id) {
        return storage.remove(id) != null;
    }
}
