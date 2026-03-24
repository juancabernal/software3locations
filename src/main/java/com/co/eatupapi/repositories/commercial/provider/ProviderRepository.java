package com.co.eatupapi.repositories.commercial.provider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.co.eatupapi.domain.commercial.provider.ProviderDomain;
import com.co.eatupapi.domain.commercial.provider.ProviderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<ProviderDomain, UUID> {

    List<ProviderDomain> findByStatus(ProviderStatus status);

    Optional<ProviderDomain> findByEmail(String email);
}
