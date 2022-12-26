package com.gksoft.workshop.web.rest;

import com.gksoft.workshop.domain.InvoiceDetails;
import com.gksoft.workshop.repository.InvoiceDetailsRepository;
import com.gksoft.workshop.service.InvoiceDetailsService;
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
 * REST controller for managing {@link com.gksoft.workshop.domain.InvoiceDetails}.
 */
@RestController
@RequestMapping("/api")
public class InvoiceDetailsResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceDetailsResource.class);

    private static final String ENTITY_NAME = "invoiceDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceDetailsService invoiceDetailsService;

    private final InvoiceDetailsRepository invoiceDetailsRepository;

    public InvoiceDetailsResource(InvoiceDetailsService invoiceDetailsService, InvoiceDetailsRepository invoiceDetailsRepository) {
        this.invoiceDetailsService = invoiceDetailsService;
        this.invoiceDetailsRepository = invoiceDetailsRepository;
    }

    /**
     * {@code POST  /invoice-details} : Create a new invoiceDetails.
     *
     * @param invoiceDetails the invoiceDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceDetails, or with status {@code 400 (Bad Request)} if the invoiceDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invoice-details")
    public ResponseEntity<InvoiceDetails> createInvoiceDetails(@RequestBody InvoiceDetails invoiceDetails) throws URISyntaxException {
        log.debug("REST request to save InvoiceDetails : {}", invoiceDetails);
        if (invoiceDetails.getId() != null) {
            throw new BadRequestAlertException("A new invoiceDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceDetails result = invoiceDetailsService.save(invoiceDetails);
        return ResponseEntity
            .created(new URI("/api/invoice-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoice-details/:id} : Updates an existing invoiceDetails.
     *
     * @param id the id of the invoiceDetails to save.
     * @param invoiceDetails the invoiceDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceDetails,
     * or with status {@code 400 (Bad Request)} if the invoiceDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invoice-details/{id}")
    public ResponseEntity<InvoiceDetails> updateInvoiceDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InvoiceDetails invoiceDetails
    ) throws URISyntaxException {
        log.debug("REST request to update InvoiceDetails : {}, {}", id, invoiceDetails);
        if (invoiceDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InvoiceDetails result = invoiceDetailsService.update(invoiceDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /invoice-details/:id} : Partial updates given fields of an existing invoiceDetails, field will ignore if it is null
     *
     * @param id the id of the invoiceDetails to save.
     * @param invoiceDetails the invoiceDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceDetails,
     * or with status {@code 400 (Bad Request)} if the invoiceDetails is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/invoice-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceDetails> partialUpdateInvoiceDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InvoiceDetails invoiceDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update InvoiceDetails partially : {}, {}", id, invoiceDetails);
        if (invoiceDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceDetails> result = invoiceDetailsService.partialUpdate(invoiceDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-details} : get all the invoiceDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceDetails in body.
     */
    @GetMapping("/invoice-details")
    public ResponseEntity<List<InvoiceDetails>> getAllInvoiceDetails(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of InvoiceDetails");
        Page<InvoiceDetails> page = invoiceDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-details/:id} : get the "id" invoiceDetails.
     *
     * @param id the id of the invoiceDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invoice-details/{id}")
    public ResponseEntity<InvoiceDetails> getInvoiceDetails(@PathVariable Long id) {
        log.debug("REST request to get InvoiceDetails : {}", id);
        Optional<InvoiceDetails> invoiceDetails = invoiceDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceDetails);
    }

    /**
     * {@code DELETE  /invoice-details/:id} : delete the "id" invoiceDetails.
     *
     * @param id the id of the invoiceDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invoice-details/{id}")
    public ResponseEntity<Void> deleteInvoiceDetails(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceDetails : {}", id);
        invoiceDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
