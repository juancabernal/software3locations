package com.co.eatupapi.services.commercial.purchase.impl;

import com.co.eatupapi.domain.commercial.purchase.PurchaseDomain;
import com.co.eatupapi.domain.commercial.purchase.PurchaseItemDomain;
import com.co.eatupapi.domain.commercial.purchase.PurchaseStatus;
import com.co.eatupapi.dto.commercial.purchase.CreatePurchaseRequest;
import com.co.eatupapi.dto.commercial.purchase.PurchaseResponse;
import com.co.eatupapi.messaging.commercial.purchase.PurchaseMessage;
import com.co.eatupapi.messaging.commercial.purchase.PurchaseMessagePublisher;
import com.co.eatupapi.repositories.commercial.purchase.PurchaseRepository;
import com.co.eatupapi.services.commercial.purchase.PurchaseService;
import com.co.eatupapi.utils.commercial.purchase.exceptions.PurchaseBusinessException;
import com.co.eatupapi.utils.commercial.purchase.exceptions.PurchaseConflictException;
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
    private final PurchaseMessagePublisher purchaseEventPublisher;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               PurchaseMapper purchaseMapper,
                               PurchaseMessagePublisher purchaseEventPublisher) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
        this.purchaseEventPublisher = purchaseEventPublisher;
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

        List<PurchaseItemDomain> items = mapItems(request);

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

        List<PurchaseItemDomain> newItems = mapItems(request);

        existing.replaceItems(newItems);
        existing.markAsModified();

        return purchaseMapper.toResponse(purchaseRepository.save(existing));
    }

    @Override
    @Transactional
    public PurchaseResponse updateStatus(UUID locationId, UUID purchaseId, PurchaseStatus newStatus) {
        PurchaseDomain existing = findByIdOrThrow(purchaseId, locationId);

        if (!existing.changeStatus(newStatus)) {
            throw new PurchaseConflictException(
                    PurchaseErrorCode.INVALID_STATUS_TRANSITION,
                    "Cannot transition from " + existing.getStatus() + " to " + newStatus
            );
        }

        existing.markAsModified();
        PurchaseDomain saved = purchaseRepository.save(existing);

        if (newStatus == PurchaseStatus.RECEIVED) {
            PurchaseMessage event = new PurchaseMessage(
                    saved.getId(),
                    saved.getOrderNumber(),
                    saved.getProviderId(),
                    saved.getLocationId(),
                    saved.getTotal(),
                    saved.getStatus()
            );
            purchaseEventPublisher.publishPurchaseReceived(event);
        }

        return purchaseMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deletePurchase(UUID locationId, UUID purchaseId) {

        PurchaseDomain existing = findByIdOrThrow(purchaseId, locationId);

        if (existing.isDeleted()) {
            throw new PurchaseBusinessException(
                    PurchaseErrorCode.PURCHASE_ALREADY_DELETED,
                    "Purchase with id: " + purchaseId + " is already deleted"
            );
        }

        if (existing.getStatus() == PurchaseStatus.APPROVED
                || existing.getStatus() == PurchaseStatus.RECEIVED) {
            throw new PurchaseBusinessException(
                    PurchaseErrorCode.INVALID_STATUS_TRANSITION,
                    "Cannot delete a purchase in " + existing.getStatus() + " status"
            );
        }

        existing.softDelete();
        purchaseRepository.save(existing);
    }

    private List<PurchaseItemDomain> mapItems(CreatePurchaseRequest request) {
        return request.getItems().stream()
                .map(purchaseMapper::toItemDomain)
                .map(PurchaseItemDomain::initialize)
                .toList();
    }

    private PurchaseDomain findByIdOrThrow(UUID id, UUID locationId) {
        return purchaseRepository.findByIdAndLocationIdAndDeletedFalse(id, locationId)
                .orElseThrow(() -> new PurchaseNotFoundException(
                        "Purchase not found with id: " + id + " for location: " + locationId));
    }

    private String generateOrderNumber() {
        return "PO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}