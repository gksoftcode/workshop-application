package com.gksoft.workshop.web.rest;

import com.gksoft.workshop.domain.Supplier;
import com.gksoft.workshop.repository.SupplierRepository;
import com.gksoft.workshop.service.SupplierService;
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
 * REST controller for managing {@link com.gksoft.workshop.domain.Supplier}.
 */
@RestController
@RequestMapping("/api")
public class SupplierResource {

    private final Logger log = LoggerFactory.getLogger(SupplierResource.class);

    private static final String ENTITY_NAME = "supplier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierService supplierService;

    private final SupplierRepository supplierRepository;

    public SupplierResource(SupplierService supplierService, SupplierRepository supplierRepository) {
        this.supplierService = supplierService;
        this.supplierRepository = supplierRepository;
    }

    /**
     * {@code POST  /suppliers} : Create a new supplier.
     *
     * @param supplier the supplier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplier, or with status {@code 400 (Bad Request)} if the supplier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/suppliers")
    public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) throws URISyntaxException {
        log.debug("REST request to save Supplier : {}", supplier);
        if (supplier.getId() != null) {
            throw new BadRequestAlertException("A new supplier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Supplier result = supplierService.save(supplier);
        return ResponseEntity
            .created(new URI("/api/suppliers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /suppliers/:id} : Updates an existing supplier.
     *
     * @param id the id of the supplier to save.
     * @param supplier the supplier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplier,
     * or with status {@code 400 (Bad Request)} if the supplier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/suppliers/{id}")
    public ResponseEntity<Supplier> updateSupplier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Supplier supplier
    ) throws URISyntaxException {
        log.debug("REST request to update Supplier : {}, {}", id, supplier);
        if (supplier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Supplier result = supplierService.update(supplier);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplier.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /suppliers/:id} : Partial updates given fields of an existing supplier, field will ignore if it is null
     *
     * @param id the id of the supplier to save.
     * @param supplier the supplier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplier,
     * or with status {@code 400 (Bad Request)} if the supplier is not valid,
     * or with status {@code 404 (Not Found)} if the supplier is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/suppliers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Supplier> partialUpdateSupplier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Supplier supplier
    ) throws URISyntaxException {
        log.debug("REST request to partial update Supplier partially : {}, {}", id, supplier);
        if (supplier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Supplier> result = supplierService.partialUpdate(supplier);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplier.getId().toString())
        );
    }

    /**
     * {@code GET  /suppliers} : get all the suppliers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suppliers in body.
     */
    @GetMapping("/suppliers")
    public ResponseEntity<List<Supplier>> getAllSuppliers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Suppliers");
        Page<Supplier> page;
        if (eagerload) {
            page = supplierService.findAllWithEagerRelationships(pageable);
        } else {
            page = supplierService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /suppliers/:id} : get the "id" supplier.
     *
     * @param id the id of the supplier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/suppliers/{id}")
    public ResponseEntity<Supplier> getSupplier(@PathVariable Long id) {
        log.debug("REST request to get Supplier : {}", id);
        Optional<Supplier> supplier = supplierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplier);
    }

    /**
     * {@code DELETE  /suppliers/:id} : delete the "id" supplier.
     *
     * @param id the id of the supplier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/suppliers/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        log.debug("REST request to delete Supplier : {}", id);
        supplierService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
