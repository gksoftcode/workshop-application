package com.gksoft.workshop.web.rest;

import com.gksoft.workshop.domain.PurchaseOrderDetails;
import com.gksoft.workshop.repository.PurchaseOrderDetailsRepository;
import com.gksoft.workshop.service.PurchaseOrderDetailsService;
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
 * REST controller for managing {@link com.gksoft.workshop.domain.PurchaseOrderDetails}.
 */
@RestController
@RequestMapping("/api")
public class PurchaseOrderDetailsResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderDetailsResource.class);

    private static final String ENTITY_NAME = "purchaseOrderDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseOrderDetailsService purchaseOrderDetailsService;

    private final PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;

    public PurchaseOrderDetailsResource(
        PurchaseOrderDetailsService purchaseOrderDetailsService,
        PurchaseOrderDetailsRepository purchaseOrderDetailsRepository
    ) {
        this.purchaseOrderDetailsService = purchaseOrderDetailsService;
        this.purchaseOrderDetailsRepository = purchaseOrderDetailsRepository;
    }

    /**
     * {@code POST  /purchase-order-details} : Create a new purchaseOrderDetails.
     *
     * @param purchaseOrderDetails the purchaseOrderDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseOrderDetails, or with status {@code 400 (Bad Request)} if the purchaseOrderDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchase-order-details")
    public ResponseEntity<PurchaseOrderDetails> createPurchaseOrderDetails(@RequestBody PurchaseOrderDetails purchaseOrderDetails)
        throws URISyntaxException {
        log.debug("REST request to save PurchaseOrderDetails : {}", purchaseOrderDetails);
        if (purchaseOrderDetails.getId() != null) {
            throw new BadRequestAlertException("A new purchaseOrderDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseOrderDetails result = purchaseOrderDetailsService.save(purchaseOrderDetails);
        return ResponseEntity
            .created(new URI("/api/purchase-order-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchase-order-details/:id} : Updates an existing purchaseOrderDetails.
     *
     * @param id the id of the purchaseOrderDetails to save.
     * @param purchaseOrderDetails the purchaseOrderDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrderDetails,
     * or with status {@code 400 (Bad Request)} if the purchaseOrderDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrderDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchase-order-details/{id}")
    public ResponseEntity<PurchaseOrderDetails> updatePurchaseOrderDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PurchaseOrderDetails purchaseOrderDetails
    ) throws URISyntaxException {
        log.debug("REST request to update PurchaseOrderDetails : {}, {}", id, purchaseOrderDetails);
        if (purchaseOrderDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseOrderDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseOrderDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PurchaseOrderDetails result = purchaseOrderDetailsService.update(purchaseOrderDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseOrderDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /purchase-order-details/:id} : Partial updates given fields of an existing purchaseOrderDetails, field will ignore if it is null
     *
     * @param id the id of the purchaseOrderDetails to save.
     * @param purchaseOrderDetails the purchaseOrderDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrderDetails,
     * or with status {@code 400 (Bad Request)} if the purchaseOrderDetails is not valid,
     * or with status {@code 404 (Not Found)} if the purchaseOrderDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrderDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/purchase-order-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchaseOrderDetails> partialUpdatePurchaseOrderDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PurchaseOrderDetails purchaseOrderDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update PurchaseOrderDetails partially : {}, {}", id, purchaseOrderDetails);
        if (purchaseOrderDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseOrderDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseOrderDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchaseOrderDetails> result = purchaseOrderDetailsService.partialUpdate(purchaseOrderDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseOrderDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /purchase-order-details} : get all the purchaseOrderDetails.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseOrderDetails in body.
     */
    @GetMapping("/purchase-order-details")
    public ResponseEntity<List<PurchaseOrderDetails>> getAllPurchaseOrderDetails(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of PurchaseOrderDetails");
        Page<PurchaseOrderDetails> page;
        if (eagerload) {
            page = purchaseOrderDetailsService.findAllWithEagerRelationships(pageable);
        } else {
            page = purchaseOrderDetailsService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /purchase-order-details/:id} : get the "id" purchaseOrderDetails.
     *
     * @param id the id of the purchaseOrderDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseOrderDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchase-order-details/{id}")
    public ResponseEntity<PurchaseOrderDetails> getPurchaseOrderDetails(@PathVariable Long id) {
        log.debug("REST request to get PurchaseOrderDetails : {}", id);
        Optional<PurchaseOrderDetails> purchaseOrderDetails = purchaseOrderDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseOrderDetails);
    }

    /**
     * {@code DELETE  /purchase-order-details/:id} : delete the "id" purchaseOrderDetails.
     *
     * @param id the id of the purchaseOrderDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchase-order-details/{id}")
    public ResponseEntity<Void> deletePurchaseOrderDetails(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseOrderDetails : {}", id);
        purchaseOrderDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
