package com.co.eatupapi.services.commercial.purchase.impl;

import com.co.eatupapi.domain.commercial.provider.ProviderDomain;
import com.co.eatupapi.domain.commercial.provider.ProviderStatus;
import com.co.eatupapi.domain.commercial.purchase.PurchaseDomain;
import com.co.eatupapi.domain.commercial.purchase.PurchaseItemDomain;
import com.co.eatupapi.domain.commercial.purchase.PurchaseStatus;
import com.co.eatupapi.dto.commercial.purchase.PurchaseDTO;
import com.co.eatupapi.repositories.commercial.provider.ProviderRepository;
import com.co.eatupapi.repositories.commercial.purchase.PurchaseRepository;
import com.co.eatupapi.services.commercial.purchase.PurchaseService;
import com.co.eatupapi.utils.commercial.provider.exceptions.BusinessException;
import com.co.eatupapi.utils.commercial.provider.exceptions.ResourceNotFoundException;
import com.co.eatupapi.utils.commercial.provider.exceptions.ValidationException;
import com.co.eatupapi.utils.commercial.purchase.mapper.PurchaseMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProviderRepository providerRepository;
    private final PurchaseMapper purchaseMapper;


    public PurchaseServiceImpl(
            PurchaseRepository purchaseRepository,
            ProviderRepository providerRepository,
            PurchaseMapper purchaseMapper) {
        this.purchaseRepository = purchaseRepository;
        this.providerRepository = providerRepository;
        this.purchaseMapper = purchaseMapper;
    }

    // ── Crear ──────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public PurchaseDTO createPurchase(PurchaseDTO request) {
        validatePurchaseRequest(request);

        ProviderDomain provider = resolveActiveProvider(request.getProviderId());

        PurchaseDomain domain = new PurchaseDomain();
        domain.setId(UUID.randomUUID().toString());
        domain.setOrderNumber(generateOrderNumber());
        domain.setProvider(provider);
        domain.setBranchId(request.getBranchId());
        domain.setStatus(PurchaseStatus.CREATED);
        domain.setDeleted(false);
        domain.setCreatedDate(LocalDateTime.now());
        domain.setModifiedDate(LocalDateTime.now());

        List<PurchaseItemDomain> items = purchaseMapper.toItemDomainList(request.getItems());
        domain.replaceItems(items);
        domain.setTotal(calculateTotal(domain.getItems()));

        return purchaseMapper.toDto(purchaseRepository.save(domain));
    }

    // ── Consultar ──────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public PurchaseDTO getPurchaseById(String purchaseId) {
        return purchaseMapper.toDto(findByIdOrThrow(purchaseId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseDTO> getPurchases(String status) {
        List<PurchaseDomain> results = (status == null || status.isBlank())
                ? purchaseRepository.findByDeletedFalse()
                : purchaseRepository.findByStatusAndDeletedFalse(parseStatus(status));

        return results.stream()
                .map(purchaseMapper::toDto)
                .collect(Collectors.toList());
    }

    // ── Actualizar ─────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public PurchaseDTO updatePurchase(String purchaseId, PurchaseDTO request) {
        validatePurchaseRequest(request);

        PurchaseDomain existing = findByIdOrThrow(purchaseId);

        if (existing.getStatus() != PurchaseStatus.CREATED) {
            throw new BusinessException("Only purchases in CREATED status can be modified");
        }

        ProviderDomain provider = resolveActiveProvider(request.getProviderId());

        existing.setProvider(provider);
        existing.setBranchId(request.getBranchId());

        List<PurchaseItemDomain> newItems = purchaseMapper.toItemDomainList(request.getItems());
        existing.replaceItems(newItems);
        existing.setTotal(calculateTotal(existing.getItems()));
        existing.setModifiedDate(LocalDateTime.now());

        return purchaseMapper.toDto(purchaseRepository.save(existing));
    }

    // ── Cambio de estado ───────────────────────────────────────────────────────

    @Override
    @Transactional
    public PurchaseDTO updateStatus(String purchaseId, String status) {
        PurchaseDomain existing = findByIdOrThrow(purchaseId);
        PurchaseStatus newStatus = parseRequiredStatus(status);

        if (!existing.getStatus().canTransitionTo(newStatus)) {
            throw new BusinessException(
                    String.format("Cannot transition purchase from %s to %s",
                            existing.getStatus(), newStatus));
        }

        existing.setStatus(newStatus);
        existing.setModifiedDate(LocalDateTime.now());

        return purchaseMapper.toDto(purchaseRepository.save(existing));
    }

    // ── Eliminar (soft-delete) ─────────────────────────────────────────────────

    @Override
    @Transactional
    public void deletePurchase(String purchaseId) {
        PurchaseDomain existing = findByIdOrThrow(purchaseId);

        if (existing.getStatus() == PurchaseStatus.APPROVED
                || existing.getStatus() == PurchaseStatus.RECEIVED) {
            throw new BusinessException(
                    "Cannot delete a purchase in " + existing.getStatus() + " status");
        }

        existing.setDeleted(true);
        existing.setModifiedDate(LocalDateTime.now());
        purchaseRepository.save(existing);
    }

    // ── Métodos privados ───────────────────────────────────────────────────────

    private PurchaseDomain findByIdOrThrow(String id) {
        return purchaseRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Purchase not found with id: " + id));
    }

    private ProviderDomain resolveActiveProvider(String providerId) {
        UUID uuid;
        try {
            uuid = UUID.fromString(providerId);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Provider not found with id: " + providerId);
        }

        ProviderDomain provider = providerRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Provider not found with id: " + providerId));

        if (provider.getStatus() != ProviderStatus.ACTIVE) {
            throw new BusinessException("Purchases can only be linked to active providers");
        }

        return provider;
    }

    private PurchaseStatus parseStatus(String status) {
        try {
            return PurchaseStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid purchase status: " + status);
        }
    }

    private PurchaseStatus parseRequiredStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new ValidationException("Status is required");
        }
        return parseStatus(status);
    }

    private void validatePurchaseRequest(PurchaseDTO request) {
        if (request.getProviderId() == null || request.getProviderId().isBlank()) {
            throw new ValidationException("providerId is required");
        }
        if (request.getBranchId() == null) {
            throw new ValidationException("branchId is required");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ValidationException("Purchase must contain at least one item");
        }
    }

    private double calculateTotal(List<PurchaseItemDomain> items) {
        double total = 0;
        for (PurchaseItemDomain item : items) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new ValidationException("Item quantity must be greater than 0");
            }
            if (item.getUnitPrice() == null || item.getUnitPrice() <= 0) {
                throw new ValidationException("Item unit price must be greater than 0");
            }
            total += item.getQuantity() * item.getUnitPrice();
        }
        return total;
    }

    private String generateOrderNumber() {
        return "PO-" + System.currentTimeMillis();
    }
}