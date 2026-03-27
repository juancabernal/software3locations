package com.co.eatupapi.dto.commercial.sales;

import java.util.List;

public class SaleRequestDTO {

    private String sellerId;
    private String tableId;
    private List<SaleDetailDTO> details;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public List<SaleDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<SaleDetailDTO> details) {
        this.details = details;
    }
}
