package com.co.eatupapi.repositories.inventory.location;

import com.co.eatupapi.domain.inventory.location.LocationDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<LocationDomain, UUID> {
    List<LocationDomain> findByActiveTrue();
}
