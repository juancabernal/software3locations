package com.co.eatupapi.services.commercial.provider;

import com.co.eatupapi.dto.commercial.provider.ProviderDTO;

import java.util.List;

public interface ProviderService {

    ProviderDTO createProvider(ProviderDTO request);

    ProviderDTO getProviderById(String providerId);

    List<ProviderDTO> getProviders(String status);

    ProviderDTO updateProvider(String providerId, ProviderDTO request);

    ProviderDTO updateStatus(String providerId, String status);
}
