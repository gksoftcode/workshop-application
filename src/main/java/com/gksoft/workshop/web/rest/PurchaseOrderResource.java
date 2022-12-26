package com.gksoft.workshop.web.rest;

import com.gksoft.workshop.domain.PurchaseOrder;
import com.gksoft.workshop.repository.PurchaseOrderRepository;
import com.gksoft.workshop.service.PurchaseOrderService;
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
 * REST controller for managing {@link com.gksoft.workshop.domain.PurchaseOrder}.
 */
@RestController
@RequestMapping("/api")
public class PurchaseOrderResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderResource.class);

    private static final String ENTITY_NAME = "purchaseOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseOrderService purchaseOrderService;

    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderResource(PurchaseOrderService purchaseOrderService, PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderService = purchaseOrderService;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    /**
     * {@code POST  /purchase-orders} : Create a new purchaseOrder.
     *
     * @param purchaseOrder the purchaseOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseOrder, or with status {@code 400 (Bad Request)} if the purchaseOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchase-orders")
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder) throws URISyntaxException {
        log.debug("REST request to save PurchaseOrder : {}", purchaseOrder);
        if (purchaseOrder.getId() != null) {
            throw new BadRequestAlertException("A new purchaseOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseOrder result = purchaseOrderService.save(purchaseOrder);
        return ResponseEntity
            .created(new URI("/api/purchase-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchase-orders/:id} : Updates an existing purchaseOrder.
     *
     * @param id the id of the purchaseOrder to save.
     * @param purchaseOrder the purchaseOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrder,
     * or with status {@code 400 (Bad Request)} if the purchaseOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchase-orders/{id}")
    public ResponseEntity<PurchaseOrder> updatePurchaseOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PurchaseOrder purchaseOrder
    ) throws URISyntaxException {
        log.debug("REST request to update PurchaseOrder : {}, {}", id, purchaseOrder);
        if (purchaseOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PurchaseOrder result = purchaseOrderService.update(purchaseOrder);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseOrder.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /purchase-orders/:id} : Partial updates given fields of an existing purchaseOrder, field will ignore if it is null
     *
     * @param id the id of the purchaseOrder to save.
     * @param purchaseOrder the purchaseOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrder,
     * or with status {@code 400 (Bad Request)} if the purchaseOrder is not valid,
     * or with status {@code 404 (Not Found)} if the purchaseOrder is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/purchase-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchaseOrder> partialUpdatePurchaseOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PurchaseOrder purchaseOrder
    ) throws URISyntaxException {
        log.debug("REST request to partial update PurchaseOrder partially : {}, {}", id, purchaseOrder);
        if (purchaseOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchaseOrder> result = purchaseOrderService.partialUpdate(purchaseOrder);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseOrder.getId().toString())
        );
    }

    /**
     * {@code GET  /purchase-orders} : get all the purchaseOrders.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseOrders in body.
     */
    @GetMapping("/purchase-orders")
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of PurchaseOrders");
        Page<PurchaseOrder> page;
        if (eagerload) {
            page = purchaseOrderService.findAllWithEagerRelationships(pageable);
        } else {
            page = purchaseOrderService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /purchase-orders/:id} : get the "id" purchaseOrder.
     *
     * @param id the id of the purchaseOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchase-orders/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrder(@PathVariable Long id) {
        log.debug("REST request to get PurchaseOrder : {}", id);
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseOrder);
    }

    /**
     * {@code DELETE  /purchase-orders/:id} : delete the "id" purchaseOrder.
     *
     * @param id the id of the purchaseOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchase-orders/{id}")
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseOrder : {}", id);
        purchaseOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
