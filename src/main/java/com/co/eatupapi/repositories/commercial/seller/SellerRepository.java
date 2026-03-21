package com.co.eatupapi.repositories.commercial.seller;

import com.co.eatupapi.domain.commercial.seller.SellerDomain;
import com.co.eatupapi.domain.commercial.seller.SellerStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class SellerRepository {

    public List<SellerDomain> loadInitialSellers() {
        LocalDateTime now = LocalDateTime.now();

        SellerDomain seller1 = new SellerDomain();
        seller1.setId(UUID.randomUUID().toString());  // ID real generado
        seller1.setDocumentType("CC");
        seller1.setLocationId(1L);
        seller1.setIdentificationNumber("1023456789");
        seller1.setFirstName("Carlos");
        seller1.setLastName("Ruiz");
        seller1.setPhone("3001234567");
        seller1.setEmail("carlos@ventas.com");
        seller1.setCommissionPercentage(15.5);
        seller1.setStatus(SellerStatus.ACTIVE);
        seller1.setCreatedDate(now);        // fecha real de ahora
        seller1.setModifiedDate(now);       // fecha real de ahora

        SellerDomain seller2 = new SellerDomain();
        seller2.setId(UUID.randomUUID().toString());
        seller2.setDocumentType("CE");
        seller2.setLocationId(2L);
        seller2.setIdentificationNumber("9876543210");
        seller2.setFirstName("Laura");
        seller2.setLastName("Gomez");
        seller2.setPhone("3209876543");
        seller2.setEmail("laura@ventas.com");
        seller2.setCommissionPercentage(12.0);
        seller2.setStatus(SellerStatus.ACTIVE);
        seller2.setCreatedDate(now.minusDays(5));
        seller2.setModifiedDate(now.minusDays(1));

        return List.of(seller1, seller2);
    }
}