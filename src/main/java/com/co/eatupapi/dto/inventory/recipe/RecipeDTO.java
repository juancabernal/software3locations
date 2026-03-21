package com.co.eatupapi.dto.inventory.recipe;

import com.co.eatupapi.domain.inventory.product.Product;

import java.util.List;

public class RecipeDTO {

    private String recipeName;
    private String category;
    private List<Product> items;
    private List<RecipeDTO> subRecipes;
    private Double baseCost;
    private Integer profitMargin;
    private Double sellingPrice;
    private Boolean isVisibleInMenu;
    private Boolean isActive;

    public RecipeDTO() {}

    public String getRecipeName() { return recipeName; }
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<Product> getItems() { return items; }
    public void setItems(List<Product> items) { this.items = items; }

    public Double getBaseCost() { return baseCost; }
    public void setBaseCost(Double baseCost) { this.baseCost = baseCost; }

    public Integer getProfitMargin() { return profitMargin; }
    public void setProfitMargin(Integer profitMargin) { this.profitMargin = profitMargin; }

    public Double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(Double sellingPrice) { this.sellingPrice = sellingPrice; }

    public Boolean getIsVisibleInMenu() { return isVisibleInMenu; }
    public void setIsVisibleInMenu(Boolean visibleInMenu) { this.isVisibleInMenu = visibleInMenu; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { this.isActive = active; }
}