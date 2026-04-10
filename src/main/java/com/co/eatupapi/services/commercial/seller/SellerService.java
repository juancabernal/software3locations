package com.co.eatupapi.services.commercial.seller;

import com.co.eatupapi.dto.commercial.seller.SellerDTO;
import com.co.eatupapi.dto.commercial.seller.SellerPatchDTO;
import java.util.List;
import java.util.UUID;

public interface SellerService {
    SellerDTO createSeller(SellerDTO request);
    SellerDTO getSellerById(UUID sellerId);
    List<SellerDTO> getSellers(String status);
    SellerDTO updateSeller(UUID sellerId, SellerDTO request);
    SellerDTO updateStatus(UUID sellerId, String status);
    SellerDTO patchSeller(UUID sellerId, SellerPatchDTO request);
}
