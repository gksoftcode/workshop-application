package com.gksoft.workshop.web.rest;

import com.gksoft.workshop.domain.ServiceCategory;
import com.gksoft.workshop.repository.ServiceCategoryRepository;
import com.gksoft.workshop.service.ServiceCategoryService;
import com.gksoft.workshop.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gksoft.workshop.domain.ServiceCategory}.
 */
@RestController
@RequestMapping("/api")
public class ServiceCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ServiceCategoryResource.class);

    private static final String ENTITY_NAME = "serviceCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceCategoryService serviceCategoryService;

    private final ServiceCategoryRepository serviceCategoryRepository;

    public ServiceCategoryResource(ServiceCategoryService serviceCategoryService, ServiceCategoryRepository serviceCategoryRepository) {
        this.serviceCategoryService = serviceCategoryService;
        this.serviceCategoryRepository = serviceCategoryRepository;
    }

    /**
     * {@code POST  /service-categories} : Create a new serviceCategory.
     *
     * @param serviceCategory the serviceCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceCategory, or with status {@code 400 (Bad Request)} if the serviceCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-categories")
    public ResponseEntity<ServiceCategory> createServiceCategory(@RequestBody ServiceCategory serviceCategory) throws URISyntaxException {
        log.debug("REST request to save ServiceCategory : {}", serviceCategory);
        if (serviceCategory.getId() != null) {
            throw new BadRequestAlertException("A new serviceCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceCategory result = serviceCategoryService.save(serviceCategory);
        return ResponseEntity
            .created(new URI("/api/service-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-categories/:id} : Updates an existing serviceCategory.
     *
     * @param id the id of the serviceCategory to save.
     * @param serviceCategory the serviceCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceCategory,
     * or with status {@code 400 (Bad Request)} if the serviceCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-categories/{id}")
    public ResponseEntity<ServiceCategory> updateServiceCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ServiceCategory serviceCategory
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceCategory : {}, {}", id, serviceCategory);
        if (serviceCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServiceCategory result = serviceCategoryService.update(serviceCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /service-categories/:id} : Partial updates given fields of an existing serviceCategory, field will ignore if it is null
     *
     * @param id the id of the serviceCategory to save.
     * @param serviceCategory the serviceCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceCategory,
     * or with status {@code 400 (Bad Request)} if the serviceCategory is not valid,
     * or with status {@code 404 (Not Found)} if the serviceCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceCategory> partialUpdateServiceCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ServiceCategory serviceCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceCategory partially : {}, {}", id, serviceCategory);
        if (serviceCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceCategory> result = serviceCategoryService.partialUpdate(serviceCategory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /service-categories} : get all the serviceCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceCategories in body.
     */
    @GetMapping("/service-categories")
    public ResponseEntity<List<ServiceCategory>> getAllServiceCategories(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ServiceCategories");
        Page<ServiceCategory> page = serviceCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-categories/:id} : get the "id" serviceCategory.
     *
     * @param id the id of the serviceCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-categories/{id}")
    public ResponseEntity<ServiceCategory> getServiceCategory(@PathVariable Long id) {
        log.debug("REST request to get ServiceCategory : {}", id);
        Optional<ServiceCategory> serviceCategory = serviceCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceCategory);
    }

    /**
     * {@code DELETE  /service-categories/:id} : delete the "id" serviceCategory.
     *
     * @param id the id of the serviceCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-categories/{id}")
    public ResponseEntity<Void> deleteServiceCategory(@PathVariable Long id) {
        log.debug("REST request to delete ServiceCategory : {}", id);
        serviceCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
