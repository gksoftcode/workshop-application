package com.gksoft.workshop.service;

import com.gksoft.workshop.domain.Services;
import com.gksoft.workshop.repository.ServicesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Services}.
 */
@Service
@Transactional
public class ServicesService {

    private final Logger log = LoggerFactory.getLogger(ServicesService.class);

    private final ServicesRepository servicesRepository;

    public ServicesService(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    /**
     * Save a services.
     *
     * @param services the entity to save.
     * @return the persisted entity.
     */
    public Services save(Services services) {
        log.debug("Request to save Services : {}", services);
        return servicesRepository.save(services);
    }

    /**
     * Update a services.
     *
     * @param services the entity to save.
     * @return the persisted entity.
     */
    public Services update(Services services) {
        log.debug("Request to update Services : {}", services);
        return servicesRepository.save(services);
    }

    /**
     * Partially update a services.
     *
     * @param services the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Services> partialUpdate(Services services) {
        log.debug("Request to partially update Services : {}", services);

        return servicesRepository
            .findById(services.getId())
            .map(existingServices -> {
                if (services.getName() != null) {
                    existingServices.setName(services.getName());
                }
                if (services.getDescription() != null) {
                    existingServices.setDescription(services.getDescription());
                }
                if (services.getCost() != null) {
                    existingServices.setCost(services.getCost());
                }
                if (services.getPrice() != null) {
                    existingServices.setPrice(services.getPrice());
                }
                if (services.getDiscount() != null) {
                    existingServices.setDiscount(services.getDiscount());
                }
                if (services.getNotes() != null) {
                    existingServices.setNotes(services.getNotes());
                }
                if (services.getDiscountType() != null) {
                    existingServices.setDiscountType(services.getDiscountType());
                }

                return existingServices;
            })
            .map(servicesRepository::save);
    }

    /**
     * Get all the services.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Services> findAll(Pageable pageable) {
        log.debug("Request to get all Services");
        return servicesRepository.findAll(pageable);
    }

    /**
     * Get all the services with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Services> findAllWithEagerRelationships(Pageable pageable) {
        return servicesRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one services by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Services> findOne(Long id) {
        log.debug("Request to get Services : {}", id);
        return servicesRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the services by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Services : {}", id);
        servicesRepository.deleteById(id);
    }
}
