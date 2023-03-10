package com.gksoft.workshop.service;

import com.gksoft.workshop.domain.Tax;
import com.gksoft.workshop.repository.TaxRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tax}.
 */
@Service
@Transactional
public class TaxService {

    private final Logger log = LoggerFactory.getLogger(TaxService.class);

    private final TaxRepository taxRepository;

    public TaxService(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }

    /**
     * Save a tax.
     *
     * @param tax the entity to save.
     * @return the persisted entity.
     */
    public Tax save(Tax tax) {
        log.debug("Request to save Tax : {}", tax);
        return taxRepository.save(tax);
    }

    /**
     * Update a tax.
     *
     * @param tax the entity to save.
     * @return the persisted entity.
     */
    public Tax update(Tax tax) {
        log.debug("Request to update Tax : {}", tax);
        return taxRepository.save(tax);
    }

    /**
     * Partially update a tax.
     *
     * @param tax the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Tax> partialUpdate(Tax tax) {
        log.debug("Request to partially update Tax : {}", tax);

        return taxRepository
            .findById(tax.getId())
            .map(existingTax -> {
                if (tax.getName() != null) {
                    existingTax.setName(tax.getName());
                }
                if (tax.getTaxValue() != null) {
                    existingTax.setTaxValue(tax.getTaxValue());
                }

                return existingTax;
            })
            .map(taxRepository::save);
    }

    /**
     * Get all the taxes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Tax> findAll(Pageable pageable) {
        log.debug("Request to get all Taxes");
        return taxRepository.findAll(pageable);
    }

    /**
     * Get one tax by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Tax> findOne(Long id) {
        log.debug("Request to get Tax : {}", id);
        return taxRepository.findById(id);
    }

    /**
     * Delete the tax by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tax : {}", id);
        taxRepository.deleteById(id);
    }
}
