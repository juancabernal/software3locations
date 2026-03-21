package com.descuentos.descuentos_mio.client;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CategoryServiceClient {

    private static final UUID CATEGORY_ID_1 = UUID.fromString("10f11111-1111-1111-1111-111111111111");
    private static final UUID CATEGORY_ID_2 = UUID.fromString("20f22222-2222-2222-2222-222222222222");
    private static final UUID CATEGORY_ID_3 = UUID.fromString("30f33333-3333-3333-3333-333333333333");

    private final List<UUID> categoryIds = List.of(CATEGORY_ID_1, CATEGORY_ID_2, CATEGORY_ID_3);

    public Optional<UUID> findCategoryById(UUID categoryId) {
        return categoryIds.stream()
                .filter(id -> id.equals(categoryId))
                .findFirst();
    }

    public List<UUID> getAvailableCategoryIds() {
        return categoryIds;
    }
}
