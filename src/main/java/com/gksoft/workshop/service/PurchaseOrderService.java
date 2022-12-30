package com.gksoft.workshop.service;

import com.gksoft.workshop.domain.PurchaseOrder;
import com.gksoft.workshop.repository.PurchaseOrderRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PurchaseOrder}.
 */
@Service
@Transactional
public class PurchaseOrderService {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderService.class);

    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    /**
     * Save a purchaseOrder.
     *
     * @param purchaseOrder the entity to save.
     * @return the persisted entity.
     */
    public PurchaseOrder save(PurchaseOrder purchaseOrder) {
        log.debug("Request to save PurchaseOrder : {}", purchaseOrder);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    /**
     * Update a purchaseOrder.
     *
     * @param purchaseOrder the entity to save.
     * @return the persisted entity.
     */
    public PurchaseOrder update(PurchaseOrder purchaseOrder) {
        log.debug("Request to update PurchaseOrder : {}", purchaseOrder);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    /**
     * Partially update a purchaseOrder.
     *
     * @param purchaseOrder the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PurchaseOrder> partialUpdate(PurchaseOrder purchaseOrder) {
        log.debug("Request to partially update PurchaseOrder : {}", purchaseOrder);

        return purchaseOrderRepository
            .findById(purchaseOrder.getId())
            .map(existingPurchaseOrder -> {
                if (purchaseOrder.getInvoiceDate() != null) {
                    existingPurchaseOrder.setInvoiceDate(purchaseOrder.getInvoiceDate());
                }
                if (purchaseOrder.getIssueDate() != null) {
                    existingPurchaseOrder.setIssueDate(purchaseOrder.getIssueDate());
                }
                if (purchaseOrder.getPaymentTerms() != null) {
                    existingPurchaseOrder.setPaymentTerms(purchaseOrder.getPaymentTerms());
                }
                if (purchaseOrder.getDiscount() != null) {
                    existingPurchaseOrder.setDiscount(purchaseOrder.getDiscount());
                }
                if (purchaseOrder.getNotes() != null) {
                    existingPurchaseOrder.setNotes(purchaseOrder.getNotes());
                }
                if (purchaseOrder.getDiscountType() != null) {
                    existingPurchaseOrder.setDiscountType(purchaseOrder.getDiscountType());
                }
                if (purchaseOrder.getDepositAmount() != null) {
                    existingPurchaseOrder.setDepositAmount(purchaseOrder.getDepositAmount());
                }
                if (purchaseOrder.getIsDepositPaied() != null) {
                    existingPurchaseOrder.setIsDepositPaied(purchaseOrder.getIsDepositPaied());
                }
                if (purchaseOrder.getDepositMethod() != null) {
                    existingPurchaseOrder.setDepositMethod(purchaseOrder.getDepositMethod());
                }
                if (purchaseOrder.getDepositPayRef() != null) {
                    existingPurchaseOrder.setDepositPayRef(purchaseOrder.getDepositPayRef());
                }
                if (purchaseOrder.getIsAlreadyPaied() != null) {
                    existingPurchaseOrder.setIsAlreadyPaied(purchaseOrder.getIsAlreadyPaied());
                }
                if (purchaseOrder.getPaymentMethod() != null) {
                    existingPurchaseOrder.setPaymentMethod(purchaseOrder.getPaymentMethod());
                }
                if (purchaseOrder.getPaymentStatus() != null) {
                    existingPurchaseOrder.setPaymentStatus(purchaseOrder.getPaymentStatus());
                }
                if (purchaseOrder.getPaymentRef() != null) {
                    existingPurchaseOrder.setPaymentRef(purchaseOrder.getPaymentRef());
                }
                if (purchaseOrder.getAmount() != null) {
                    existingPurchaseOrder.setAmount(purchaseOrder.getAmount());
                }
                if (purchaseOrder.getLastAmount() != null) {
                    existingPurchaseOrder.setLastAmount(purchaseOrder.getLastAmount());
                }
                if (purchaseOrder.getShippingFees() != null) {
                    existingPurchaseOrder.setShippingFees(purchaseOrder.getShippingFees());
                }

                return existingPurchaseOrder;
            })
            .map(purchaseOrderRepository::save);
    }

    /**
     * Get all the purchaseOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseOrder> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseOrders");
        return purchaseOrderRepository.findAll(pageable);
    }

    /**
     * Get all the purchaseOrders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PurchaseOrder> findAllWithEagerRelationships(Pageable pageable) {
        return purchaseOrderRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one purchaseOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PurchaseOrder> findOne(Long id) {
        log.debug("Request to get PurchaseOrder : {}", id);
        return purchaseOrderRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the purchaseOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchaseOrder : {}", id);
        purchaseOrderRepository.deleteById(id);
    }
}
