package com.co.eatupapi.repositories.commercial.purchase;

import com.co.eatupapi.domain.commercial.purchase.PurchaseDomain;
import com.co.eatupapi.domain.commercial.purchase.PurchaseStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseDomain, String> {

    List<PurchaseDomain> findByDeletedFalse();


    List<PurchaseDomain> findByStatusAndDeletedFalse(PurchaseStatus status);


    Optional<PurchaseDomain> findByIdAndDeletedFalse(String id);
}