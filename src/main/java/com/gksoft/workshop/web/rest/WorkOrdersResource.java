package com.gksoft.workshop.web.rest;

import com.gksoft.workshop.domain.WorkOrders;
import com.gksoft.workshop.repository.WorkOrdersRepository;
import com.gksoft.workshop.service.WorkOrdersService;
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
 * REST controller for managing {@link com.gksoft.workshop.domain.WorkOrders}.
 */
@RestController
@RequestMapping("/api")
public class WorkOrdersResource {

    private final Logger log = LoggerFactory.getLogger(WorkOrdersResource.class);

    private static final String ENTITY_NAME = "workOrders";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkOrdersService workOrdersService;

    private final WorkOrdersRepository workOrdersRepository;

    public WorkOrdersResource(WorkOrdersService workOrdersService, WorkOrdersRepository workOrdersRepository) {
        this.workOrdersService = workOrdersService;
        this.workOrdersRepository = workOrdersRepository;
    }

    /**
     * {@code POST  /work-orders} : Create a new workOrders.
     *
     * @param workOrders the workOrders to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workOrders, or with status {@code 400 (Bad Request)} if the workOrders has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-orders")
    public ResponseEntity<WorkOrders> createWorkOrders(@RequestBody WorkOrders workOrders) throws URISyntaxException {
        log.debug("REST request to save WorkOrders : {}", workOrders);
        if (workOrders.getId() != null) {
            throw new BadRequestAlertException("A new workOrders cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkOrders result = workOrdersService.save(workOrders);
        return ResponseEntity
            .created(new URI("/api/work-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-orders/:id} : Updates an existing workOrders.
     *
     * @param id the id of the workOrders to save.
     * @param workOrders the workOrders to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workOrders,
     * or with status {@code 400 (Bad Request)} if the workOrders is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workOrders couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-orders/{id}")
    public ResponseEntity<WorkOrders> updateWorkOrders(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkOrders workOrders
    ) throws URISyntaxException {
        log.debug("REST request to update WorkOrders : {}, {}", id, workOrders);
        if (workOrders.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workOrders.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workOrdersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkOrders result = workOrdersService.update(workOrders);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workOrders.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-orders/:id} : Partial updates given fields of an existing workOrders, field will ignore if it is null
     *
     * @param id the id of the workOrders to save.
     * @param workOrders the workOrders to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workOrders,
     * or with status {@code 400 (Bad Request)} if the workOrders is not valid,
     * or with status {@code 404 (Not Found)} if the workOrders is not found,
     * or with status {@code 500 (Internal Server Error)} if the workOrders couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkOrders> partialUpdateWorkOrders(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkOrders workOrders
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkOrders partially : {}, {}", id, workOrders);
        if (workOrders.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workOrders.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workOrdersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkOrders> result = workOrdersService.partialUpdate(workOrders);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workOrders.getId().toString())
        );
    }

    /**
     * {@code GET  /work-orders} : get all the workOrders.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workOrders in body.
     */
    @GetMapping("/work-orders")
    public ResponseEntity<List<WorkOrders>> getAllWorkOrders(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of WorkOrders");
        Page<WorkOrders> page;
        if (eagerload) {
            page = workOrdersService.findAllWithEagerRelationships(pageable);
        } else {
            page = workOrdersService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /work-orders/:id} : get the "id" workOrders.
     *
     * @param id the id of the workOrders to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workOrders, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-orders/{id}")
    public ResponseEntity<WorkOrders> getWorkOrders(@PathVariable Long id) {
        log.debug("REST request to get WorkOrders : {}", id);
        Optional<WorkOrders> workOrders = workOrdersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workOrders);
    }

    /**
     * {@code DELETE  /work-orders/:id} : delete the "id" workOrders.
     *
     * @param id the id of the workOrders to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-orders/{id}")
    public ResponseEntity<Void> deleteWorkOrders(@PathVariable Long id) {
        log.debug("REST request to delete WorkOrders : {}", id);
        workOrdersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
