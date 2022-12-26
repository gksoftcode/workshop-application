package com.gksoft.workshop.service;

import com.gksoft.workshop.domain.Invoice;
import com.gksoft.workshop.repository.InvoiceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Invoice}.
 */
@Service
@Transactional
public class InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Save a invoice.
     *
     * @param invoice the entity to save.
     * @return the persisted entity.
     */
    public Invoice save(Invoice invoice) {
        log.debug("Request to save Invoice : {}", invoice);
        return invoiceRepository.save(invoice);
    }

    /**
     * Update a invoice.
     *
     * @param invoice the entity to save.
     * @return the persisted entity.
     */
    public Invoice update(Invoice invoice) {
        log.debug("Request to update Invoice : {}", invoice);
        return invoiceRepository.save(invoice);
    }

    /**
     * Partially update a invoice.
     *
     * @param invoice the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Invoice> partialUpdate(Invoice invoice) {
        log.debug("Request to partially update Invoice : {}", invoice);

        return invoiceRepository
            .findById(invoice.getId())
            .map(existingInvoice -> {
                if (invoice.getInvoiceDate() != null) {
                    existingInvoice.setInvoiceDate(invoice.getInvoiceDate());
                }
                if (invoice.getIssueDate() != null) {
                    existingInvoice.setIssueDate(invoice.getIssueDate());
                }
                if (invoice.getPaymentTerms() != null) {
                    existingInvoice.setPaymentTerms(invoice.getPaymentTerms());
                }
                if (invoice.getDiscount() != null) {
                    existingInvoice.setDiscount(invoice.getDiscount());
                }
                if (invoice.getNotes() != null) {
                    existingInvoice.setNotes(invoice.getNotes());
                }
                if (invoice.getDiscountType() != null) {
                    existingInvoice.setDiscountType(invoice.getDiscountType());
                }
                if (invoice.getDepositAmount() != null) {
                    existingInvoice.setDepositAmount(invoice.getDepositAmount());
                }
                if (invoice.getIsDepositPaied() != null) {
                    existingInvoice.setIsDepositPaied(invoice.getIsDepositPaied());
                }
                if (invoice.getDepositMethod() != null) {
                    existingInvoice.setDepositMethod(invoice.getDepositMethod());
                }
                if (invoice.getDepositPayRef() != null) {
                    existingInvoice.setDepositPayRef(invoice.getDepositPayRef());
                }
                if (invoice.getIsAlreadyPaied() != null) {
                    existingInvoice.setIsAlreadyPaied(invoice.getIsAlreadyPaied());
                }
                if (invoice.getPaymentMethod() != null) {
                    existingInvoice.setPaymentMethod(invoice.getPaymentMethod());
                }
                if (invoice.getPaymentRef() != null) {
                    existingInvoice.setPaymentRef(invoice.getPaymentRef());
                }
                if (invoice.getAmount() != null) {
                    existingInvoice.setAmount(invoice.getAmount());
                }
                if (invoice.getLastAmount() != null) {
                    existingInvoice.setLastAmount(invoice.getLastAmount());
                }
                if (invoice.getShippingFees() != null) {
                    existingInvoice.setShippingFees(invoice.getShippingFees());
                }

                return existingInvoice;
            })
            .map(invoiceRepository::save);
    }

    /**
     * Get all the invoices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Invoice> findAll(Pageable pageable) {
        log.debug("Request to get all Invoices");
        return invoiceRepository.findAll(pageable);
    }

    /**
     * Get all the invoices with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Invoice> findAllWithEagerRelationships(Pageable pageable) {
        return invoiceRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one invoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Invoice> findOne(Long id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the invoice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
    }
}
