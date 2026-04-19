package com.co.eatupapi.repositories.inventory.transfer;

import com.co.eatupapi.domain.inventory.transfer.TransferStatus;
import com.co.eatupapi.domain.inventory.transfer.Transfer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByEstado(TransferStatus estado);
}
