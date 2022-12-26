package com.gksoft.workshop.service;

import com.gksoft.workshop.domain.ServiceCategory;
import com.gksoft.workshop.repository.ServiceCategoryRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ServiceCategory}.
 */
@Service
@Transactional
public class ServiceCategoryService {

    private final Logger log = LoggerFactory.getLogger(ServiceCategoryService.class);

    private final ServiceCategoryRepository serviceCategoryRepository;

    public ServiceCategoryService(ServiceCategoryRepository serviceCategoryRepository) {
        this.serviceCategoryRepository = serviceCategoryRepository;
    }

    /**
     * Save a serviceCategory.
     *
     * @param serviceCategory the entity to save.
     * @return the persisted entity.
     */
    public ServiceCategory save(ServiceCategory serviceCategory) {
        log.debug("Request to save ServiceCategory : {}", serviceCategory);
        return serviceCategoryRepository.save(serviceCategory);
    }

    /**
     * Update a serviceCategory.
     *
     * @param serviceCategory the entity to save.
     * @return the persisted entity.
     */
    public ServiceCategory update(ServiceCategory serviceCategory) {
        log.debug("Request to update ServiceCategory : {}", serviceCategory);
        return serviceCategoryRepository.save(serviceCategory);
    }

    /**
     * Partially update a serviceCategory.
     *
     * @param serviceCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ServiceCategory> partialUpdate(ServiceCategory serviceCategory) {
        log.debug("Request to partially update ServiceCategory : {}", serviceCategory);

        return serviceCategoryRepository
            .findById(serviceCategory.getId())
            .map(existingServiceCategory -> {
                if (serviceCategory.getCategoryName() != null) {
                    existingServiceCategory.setCategoryName(serviceCategory.getCategoryName());
                }

                return existingServiceCategory;
            })
            .map(serviceCategoryRepository::save);
    }

    /**
     * Get all the serviceCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceCategory> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceCategories");
        return serviceCategoryRepository.findAll(pageable);
    }

    /**
     * Get one serviceCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServiceCategory> findOne(Long id) {
        log.debug("Request to get ServiceCategory : {}", id);
        return serviceCategoryRepository.findById(id);
    }

    /**
     * Delete the serviceCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ServiceCategory : {}", id);
        serviceCategoryRepository.deleteById(id);
    }
}
