package com.gksoft.workshop.service;

import com.gksoft.workshop.domain.PurchaseOrderDetails;
import com.gksoft.workshop.repository.PurchaseOrderDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PurchaseOrderDetails}.
 */
@Service
@Transactional
public class PurchaseOrderDetailsService {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderDetailsService.class);

    private final PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;

    public PurchaseOrderDetailsService(PurchaseOrderDetailsRepository purchaseOrderDetailsRepository) {
        this.purchaseOrderDetailsRepository = purchaseOrderDetailsRepository;
    }

    /**
     * Save a purchaseOrderDetails.
     *
     * @param purchaseOrderDetails the entity to save.
     * @return the persisted entity.
     */
    public PurchaseOrderDetails save(PurchaseOrderDetails purchaseOrderDetails) {
        log.debug("Request to save PurchaseOrderDetails : {}", purchaseOrderDetails);
        return purchaseOrderDetailsRepository.save(purchaseOrderDetails);
    }

    /**
     * Update a purchaseOrderDetails.
     *
     * @param purchaseOrderDetails the entity to save.
     * @return the persisted entity.
     */
    public PurchaseOrderDetails update(PurchaseOrderDetails purchaseOrderDetails) {
        log.debug("Request to update PurchaseOrderDetails : {}", purchaseOrderDetails);
        return purchaseOrderDetailsRepository.save(purchaseOrderDetails);
    }

    /**
     * Partially update a purchaseOrderDetails.
     *
     * @param purchaseOrderDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PurchaseOrderDetails> partialUpdate(PurchaseOrderDetails purchaseOrderDetails) {
        log.debug("Request to partially update PurchaseOrderDetails : {}", purchaseOrderDetails);

        return purchaseOrderDetailsRepository
            .findById(purchaseOrderDetails.getId())
            .map(existingPurchaseOrderDetails -> {
                if (purchaseOrderDetails.getItemNo() != null) {
                    existingPurchaseOrderDetails.setItemNo(purchaseOrderDetails.getItemNo());
                }
                if (purchaseOrderDetails.getItemDesc() != null) {
                    existingPurchaseOrderDetails.setItemDesc(purchaseOrderDetails.getItemDesc());
                }
                if (purchaseOrderDetails.getItemPrice() != null) {
                    existingPurchaseOrderDetails.setItemPrice(purchaseOrderDetails.getItemPrice());
                }
                if (purchaseOrderDetails.getItemQty() != null) {
                    existingPurchaseOrderDetails.setItemQty(purchaseOrderDetails.getItemQty());
                }
                if (purchaseOrderDetails.getDiscountType() != null) {
                    existingPurchaseOrderDetails.setDiscountType(purchaseOrderDetails.getDiscountType());
                }
                if (purchaseOrderDetails.getDiscount() != null) {
                    existingPurchaseOrderDetails.setDiscount(purchaseOrderDetails.getDiscount());
                }
                if (purchaseOrderDetails.getTotalAmount() != null) {
                    existingPurchaseOrderDetails.setTotalAmount(purchaseOrderDetails.getTotalAmount());
                }

                return existingPurchaseOrderDetails;
            })
            .map(purchaseOrderDetailsRepository::save);
    }

    /**
     * Get all the purchaseOrderDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PurchaseOrderDetails> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseOrderDetails");
        return purchaseOrderDetailsRepository.findAll(pageable);
    }

    /**
     * Get all the purchaseOrderDetails with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PurchaseOrderDetails> findAllWithEagerRelationships(Pageable pageable) {
        return purchaseOrderDetailsRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one purchaseOrderDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PurchaseOrderDetails> findOne(Long id) {
        log.debug("Request to get PurchaseOrderDetails : {}", id);
        return purchaseOrderDetailsRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the purchaseOrderDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchaseOrderDetails : {}", id);
        purchaseOrderDetailsRepository.deleteById(id);
    }
}
