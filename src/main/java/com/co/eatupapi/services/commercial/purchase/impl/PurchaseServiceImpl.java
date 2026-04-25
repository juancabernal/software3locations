package com.co.eatupapi.services.commercial.purchase.impl;

import com.co.eatupapi.domain.commercial.purchase.PurchaseDomain;
import com.co.eatupapi.domain.commercial.purchase.PurchaseItemDomain;
import com.co.eatupapi.domain.commercial.purchase.PurchaseStatus;
import com.co.eatupapi.dto.commercial.purchase.CreatePurchaseRequest;
import com.co.eatupapi.dto.commercial.purchase.PurchaseResponse;
import com.co.eatupapi.repositories.commercial.purchase.PurchaseRepository;
import com.co.eatupapi.services.commercial.purchase.PurchaseService;
import com.co.eatupapi.utils.commercial.purchase.exceptions.PurchaseBusinessException;
import com.co.eatupapi.utils.commercial.purchase.exceptions.PurchaseErrorCode;
import com.co.eatupapi.utils.commercial.purchase.exceptions.PurchaseNotFoundException;
import com.co.eatupapi.utils.commercial.purchase.mapper.PurchaseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               PurchaseMapper purchaseMapper) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
    }

    @Override
    @Transactional
    public PurchaseResponse createPurchase(UUID locationId, CreatePurchaseRequest request) {

        PurchaseDomain domain = new PurchaseDomain();
        domain.setOrderNumber(generateOrderNumber());
        domain.setProviderId(request.getProviderId());
        domain.setLocationId(locationId);
        domain.setStatus(PurchaseStatus.CREATED);
        domain.setDeleted(false);
        domain.markAsCreated();

        List<PurchaseItemDomain> items = request.getItems().stream()
                .map(purchaseMapper::toItemDomain)
                .toList();

        items.forEach(PurchaseItemDomain::initialize);

        domain.replaceItems(items);

        return purchaseMapper.toResponse(purchaseRepository.save(domain));
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseResponse getPurchaseById(UUID locationId, UUID purchaseId) {
        return purchaseMapper.toResponse(findByIdOrThrow(purchaseId, locationId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseResponse> getPurchases(UUID locationId, PurchaseStatus status, Pageable pageable) {

        if (status == null) {
            return purchaseRepository
                    .findByLocationIdAndDeletedFalse(locationId, pageable)
                    .map(purchaseMapper::toResponse);
        }

        return purchaseRepository
                .findByLocationIdAndStatusAndDeletedFalse(locationId, status, pageable)
                .map(purchaseMapper::toResponse);
    }

    @Override
    @Transactional
    public PurchaseResponse updatePurchase(UUID locationId, UUID purchaseId, CreatePurchaseRequest request) {

        PurchaseDomain existing = findByIdOrThrow(purchaseId, locationId);

        if (existing.getStatus() != PurchaseStatus.CREATED) {
            throw new PurchaseBusinessException(
                    PurchaseErrorCode.INVALID_STATUS_TRANSITION,
                    "Only purchases in CREATED status can be modified"
            );
        }

        existing.setProviderId(request.getProviderId());

        List<PurchaseItemDomain> newItems = request.getItems().stream()
                .map(purchaseMapper::toItemDomain)
                .toList();

        newItems.forEach(PurchaseItemDomain::initialize);

        existing.replaceItems(newItems);
        existing.markAsModified();

        return purchaseMapper.toResponse(purchaseRepository.save(existing));
    }

    @Override
    @Transactional
    public PurchaseResponse updateStatus(UUID locationId, UUID purchaseId, PurchaseStatus newStatus) {

        PurchaseDomain existing = findByIdOrThrow(purchaseId, locationId);

        existing.changeStatus(newStatus);
        existing.markAsModified();

        return purchaseMapper.toResponse(purchaseRepository.save(existing));
    }

    @Override
    @Transactional
    public void deletePurchase(UUID locationId, UUID purchaseId) {

        PurchaseDomain existing = findByIdOrThrow(purchaseId, locationId);

        if (existing.getStatus() == PurchaseStatus.APPROVED
                || existing.getStatus() == PurchaseStatus.RECEIVED) {
            throw new PurchaseBusinessException(
                    PurchaseErrorCode.PURCHASE_ALREADY_DELETED,
                    "Cannot delete a purchase"
            );
        }

        existing.softDelete();
        purchaseRepository.save(existing);
    }

    private PurchaseDomain findByIdOrThrow(UUID id, UUID locationId) {
        return purchaseRepository.findByIdAndLocationIdAndDeletedFalse(id, locationId)
                .orElseThrow(() -> new PurchaseNotFoundException(
                        "Purchase not found with id: " + id + " for location: " + locationId));
    }

    private String generateOrderNumber() {
        return "PO-" + System.currentTimeMillis();
    }
}