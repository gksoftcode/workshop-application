package com.gksoft.workshop.service;

import com.gksoft.workshop.domain.PaymentCredit;
import com.gksoft.workshop.repository.PaymentCreditRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PaymentCredit}.
 */
@Service
@Transactional
public class PaymentCreditService {

    private final Logger log = LoggerFactory.getLogger(PaymentCreditService.class);

    private final PaymentCreditRepository paymentCreditRepository;

    public PaymentCreditService(PaymentCreditRepository paymentCreditRepository) {
        this.paymentCreditRepository = paymentCreditRepository;
    }

    /**
     * Save a paymentCredit.
     *
     * @param paymentCredit the entity to save.
     * @return the persisted entity.
     */
    public PaymentCredit save(PaymentCredit paymentCredit) {
        log.debug("Request to save PaymentCredit : {}", paymentCredit);
        return paymentCreditRepository.save(paymentCredit);
    }

    /**
     * Update a paymentCredit.
     *
     * @param paymentCredit the entity to save.
     * @return the persisted entity.
     */
    public PaymentCredit update(PaymentCredit paymentCredit) {
        log.debug("Request to update PaymentCredit : {}", paymentCredit);
        return paymentCreditRepository.save(paymentCredit);
    }

    /**
     * Partially update a paymentCredit.
     *
     * @param paymentCredit the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentCredit> partialUpdate(PaymentCredit paymentCredit) {
        log.debug("Request to partially update PaymentCredit : {}", paymentCredit);

        return paymentCreditRepository
            .findById(paymentCredit.getId())
            .map(existingPaymentCredit -> {
                if (paymentCredit.getPaymentMethod() != null) {
                    existingPaymentCredit.setPaymentMethod(paymentCredit.getPaymentMethod());
                }
                if (paymentCredit.getPaymentRef() != null) {
                    existingPaymentCredit.setPaymentRef(paymentCredit.getPaymentRef());
                }
                if (paymentCredit.getAmount() != null) {
                    existingPaymentCredit.setAmount(paymentCredit.getAmount());
                }
                if (paymentCredit.getAddDate() != null) {
                    existingPaymentCredit.setAddDate(paymentCredit.getAddDate());
                }
                if (paymentCredit.getPaymentStatus() != null) {
                    existingPaymentCredit.setPaymentStatus(paymentCredit.getPaymentStatus());
                }
                if (paymentCredit.getPaymentDetails() != null) {
                    existingPaymentCredit.setPaymentDetails(paymentCredit.getPaymentDetails());
                }
                if (paymentCredit.getReceiptNotes() != null) {
                    existingPaymentCredit.setReceiptNotes(paymentCredit.getReceiptNotes());
                }

                return existingPaymentCredit;
            })
            .map(paymentCreditRepository::save);
    }

    /**
     * Get all the paymentCredits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentCredit> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentCredits");
        return paymentCreditRepository.findAll(pageable);
    }

    /**
     * Get all the paymentCredits with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PaymentCredit> findAllWithEagerRelationships(Pageable pageable) {
        return paymentCreditRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one paymentCredit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentCredit> findOne(Long id) {
        log.debug("Request to get PaymentCredit : {}", id);
        return paymentCreditRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the paymentCredit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PaymentCredit : {}", id);
        paymentCreditRepository.deleteById(id);
    }
}
