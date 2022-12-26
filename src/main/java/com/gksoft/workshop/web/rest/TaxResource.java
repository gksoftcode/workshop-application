package com.gksoft.workshop.web.rest;

import com.gksoft.workshop.domain.Tax;
import com.gksoft.workshop.repository.TaxRepository;
import com.gksoft.workshop.service.TaxService;
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
 * REST controller for managing {@link com.gksoft.workshop.domain.Tax}.
 */
@RestController
@RequestMapping("/api")
public class TaxResource {

    private final Logger log = LoggerFactory.getLogger(TaxResource.class);

    private static final String ENTITY_NAME = "tax";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxService taxService;

    private final TaxRepository taxRepository;

    public TaxResource(TaxService taxService, TaxRepository taxRepository) {
        this.taxService = taxService;
        this.taxRepository = taxRepository;
    }

    /**
     * {@code POST  /taxes} : Create a new tax.
     *
     * @param tax the tax to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tax, or with status {@code 400 (Bad Request)} if the tax has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/taxes")
    public ResponseEntity<Tax> createTax(@RequestBody Tax tax) throws URISyntaxException {
        log.debug("REST request to save Tax : {}", tax);
        if (tax.getId() != null) {
            throw new BadRequestAlertException("A new tax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tax result = taxService.save(tax);
        return ResponseEntity
            .created(new URI("/api/taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /taxes/:id} : Updates an existing tax.
     *
     * @param id the id of the tax to save.
     * @param tax the tax to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tax,
     * or with status {@code 400 (Bad Request)} if the tax is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tax couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/taxes/{id}")
    public ResponseEntity<Tax> updateTax(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tax tax)
        throws URISyntaxException {
        log.debug("REST request to update Tax : {}, {}", id, tax);
        if (tax.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tax.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tax result = taxService.update(tax);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tax.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /taxes/:id} : Partial updates given fields of an existing tax, field will ignore if it is null
     *
     * @param id the id of the tax to save.
     * @param tax the tax to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tax,
     * or with status {@code 400 (Bad Request)} if the tax is not valid,
     * or with status {@code 404 (Not Found)} if the tax is not found,
     * or with status {@code 500 (Internal Server Error)} if the tax couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/taxes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tax> partialUpdateTax(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tax tax)
        throws URISyntaxException {
        log.debug("REST request to partial update Tax partially : {}, {}", id, tax);
        if (tax.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tax.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tax> result = taxService.partialUpdate(tax);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tax.getId().toString())
        );
    }

    /**
     * {@code GET  /taxes} : get all the taxes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxes in body.
     */
    @GetMapping("/taxes")
    public ResponseEntity<List<Tax>> getAllTaxes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Taxes");
        Page<Tax> page = taxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /taxes/:id} : get the "id" tax.
     *
     * @param id the id of the tax to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tax, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/taxes/{id}")
    public ResponseEntity<Tax> getTax(@PathVariable Long id) {
        log.debug("REST request to get Tax : {}", id);
        Optional<Tax> tax = taxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tax);
    }

    /**
     * {@code DELETE  /taxes/:id} : delete the "id" tax.
     *
     * @param id the id of the tax to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/taxes/{id}")
    public ResponseEntity<Void> deleteTax(@PathVariable Long id) {
        log.debug("REST request to delete Tax : {}", id);
        taxService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
