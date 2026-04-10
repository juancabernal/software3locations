package com.co.eatupapi.repositories.commercial.client;

import java.util.Optional;
import java.util.UUID;
import com.co.eatupapi.domain.commercial.client.ClientDomain;
import com.co.eatupapi.domain.commercial.client.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientDomain, UUID> {

    List<ClientDomain> findByFirstNameContainingIgnoreCase(String firstName);

    Optional<ClientDomain> findByEmail(String email);

    List<ClientDomain> findByDocumentNumber(String documentNumber);

    List<ClientDomain> findByStatus(ClientStatus status);

    List<ClientDomain> findByApplyDiscounts(Boolean applyDiscounts);

    List<ClientDomain> findByAssignedSellerId(Long sellerId);
}