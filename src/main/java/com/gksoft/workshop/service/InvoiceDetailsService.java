package com.gksoft.workshop.service;

import com.gksoft.workshop.domain.InvoiceDetails;
import com.gksoft.workshop.repository.InvoiceDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InvoiceDetails}.
 */
@Service
@Transactional
public class InvoiceDetailsService {

    private final Logger log = LoggerFactory.getLogger(InvoiceDetailsService.class);

    private final InvoiceDetailsRepository invoiceDetailsRepository;

    public InvoiceDetailsService(InvoiceDetailsRepository invoiceDetailsRepository) {
        this.invoiceDetailsRepository = invoiceDetailsRepository;
    }

    /**
     * Save a invoiceDetails.
     *
     * @param invoiceDetails the entity to save.
     * @return the persisted entity.
     */
    public InvoiceDetails save(InvoiceDetails invoiceDetails) {
        log.debug("Request to save InvoiceDetails : {}", invoiceDetails);
        return invoiceDetailsRepository.save(invoiceDetails);
    }

    /**
     * Update a invoiceDetails.
     *
     * @param invoiceDetails the entity to save.
     * @return the persisted entity.
     */
    public InvoiceDetails update(InvoiceDetails invoiceDetails) {
        log.debug("Request to update InvoiceDetails : {}", invoiceDetails);
        return invoiceDetailsRepository.save(invoiceDetails);
    }

    /**
     * Partially update a invoiceDetails.
     *
     * @param invoiceDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceDetails> partialUpdate(InvoiceDetails invoiceDetails) {
        log.debug("Request to partially update InvoiceDetails : {}", invoiceDetails);

        return invoiceDetailsRepository
            .findById(invoiceDetails.getId())
            .map(existingInvoiceDetails -> {
                if (invoiceDetails.getItemNo() != null) {
                    existingInvoiceDetails.setItemNo(invoiceDetails.getItemNo());
                }
                if (invoiceDetails.getItemDesc() != null) {
                    existingInvoiceDetails.setItemDesc(invoiceDetails.getItemDesc());
                }
                if (invoiceDetails.getItemPrice() != null) {
                    existingInvoiceDetails.setItemPrice(invoiceDetails.getItemPrice());
                }
                if (invoiceDetails.getItemQty() != null) {
                    existingInvoiceDetails.setItemQty(invoiceDetails.getItemQty());
                }
                if (invoiceDetails.getDiscountType() != null) {
                    existingInvoiceDetails.setDiscountType(invoiceDetails.getDiscountType());
                }
                if (invoiceDetails.getDiscount() != null) {
                    existingInvoiceDetails.setDiscount(invoiceDetails.getDiscount());
                }
                if (invoiceDetails.getTotalAmount() != null) {
                    existingInvoiceDetails.setTotalAmount(invoiceDetails.getTotalAmount());
                }

                return existingInvoiceDetails;
            })
            .map(invoiceDetailsRepository::save);
    }

    /**
     * Get all the invoiceDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceDetails> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceDetails");
        return invoiceDetailsRepository.findAll(pageable);
    }

    /**
     * Get one invoiceDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceDetails> findOne(Long id) {
        log.debug("Request to get InvoiceDetails : {}", id);
        return invoiceDetailsRepository.findById(id);
    }

    /**
     * Delete the invoiceDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InvoiceDetails : {}", id);
        invoiceDetailsRepository.deleteById(id);
    }
}
