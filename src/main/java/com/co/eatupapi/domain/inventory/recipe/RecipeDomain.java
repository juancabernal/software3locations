package com.co.eatupapi.domain.inventory.recipe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recipes")
public class RecipeDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private UUID categoryId;

    @Column(nullable = false)
    private UUID locationId;

    @ElementCollection
    @CollectionTable(name = "recipe_products", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "product_id", nullable = false)
    private List<UUID> productIds;

    @ElementCollection
    @CollectionTable(name = "recipe_subrecipes", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "subrecipe_id", nullable = false)
    private List<UUID> subRecipeIds;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal baseCost;

    @Column(nullable = false)
    private Integer profitMargin;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal sellingPrice;

    @Column(nullable = false)
    private Boolean visibleInMenu;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}