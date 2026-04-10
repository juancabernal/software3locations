package com.co.eatupapi.repositories.commercial.seller;

import com.co.eatupapi.domain.commercial.seller.SellerDomain;
import com.co.eatupapi.domain.commercial.seller.SellerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<SellerDomain, UUID>{
    boolean existsByEmail(String email);
    boolean existsByIdentificationNumber(String identificationNumber);
    List<SellerDomain> findByStatus(SellerStatus status);
}