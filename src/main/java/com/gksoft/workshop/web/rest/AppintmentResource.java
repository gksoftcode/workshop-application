package com.gksoft.workshop.web.rest;

import com.gksoft.workshop.domain.Appintment;
import com.gksoft.workshop.repository.AppintmentRepository;
import com.gksoft.workshop.service.AppintmentService;
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
 * REST controller for managing {@link com.gksoft.workshop.domain.Appintment}.
 */
@RestController
@RequestMapping("/api")
public class AppintmentResource {

    private final Logger log = LoggerFactory.getLogger(AppintmentResource.class);

    private static final String ENTITY_NAME = "appintment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppintmentService appintmentService;

    private final AppintmentRepository appintmentRepository;

    public AppintmentResource(AppintmentService appintmentService, AppintmentRepository appintmentRepository) {
        this.appintmentService = appintmentService;
        this.appintmentRepository = appintmentRepository;
    }

    /**
     * {@code POST  /appintments} : Create a new appintment.
     *
     * @param appintment the appintment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appintment, or with status {@code 400 (Bad Request)} if the appintment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appintments")
    public ResponseEntity<Appintment> createAppintment(@RequestBody Appintment appintment) throws URISyntaxException {
        log.debug("REST request to save Appintment : {}", appintment);
        if (appintment.getId() != null) {
            throw new BadRequestAlertException("A new appintment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Appintment result = appintmentService.save(appintment);
        return ResponseEntity
            .created(new URI("/api/appintments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /appintments/:id} : Updates an existing appintment.
     *
     * @param id the id of the appintment to save.
     * @param appintment the appintment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appintment,
     * or with status {@code 400 (Bad Request)} if the appintment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appintment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appintments/{id}")
    public ResponseEntity<Appintment> updateAppintment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Appintment appintment
    ) throws URISyntaxException {
        log.debug("REST request to update Appintment : {}, {}", id, appintment);
        if (appintment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appintment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appintmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Appintment result = appintmentService.update(appintment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appintment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /appintments/:id} : Partial updates given fields of an existing appintment, field will ignore if it is null
     *
     * @param id the id of the appintment to save.
     * @param appintment the appintment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appintment,
     * or with status {@code 400 (Bad Request)} if the appintment is not valid,
     * or with status {@code 404 (Not Found)} if the appintment is not found,
     * or with status {@code 500 (Internal Server Error)} if the appintment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/appintments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Appintment> partialUpdateAppintment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Appintment appintment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Appintment partially : {}, {}", id, appintment);
        if (appintment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appintment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appintmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Appintment> result = appintmentService.partialUpdate(appintment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appintment.getId().toString())
        );
    }

    /**
     * {@code GET  /appintments} : get all the appintments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appintments in body.
     */
    @GetMapping("/appintments")
    public ResponseEntity<List<Appintment>> getAllAppintments(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Appintments");
        Page<Appintment> page = appintmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /appintments/:id} : get the "id" appintment.
     *
     * @param id the id of the appintment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appintment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appintments/{id}")
    public ResponseEntity<Appintment> getAppintment(@PathVariable Long id) {
        log.debug("REST request to get Appintment : {}", id);
        Optional<Appintment> appintment = appintmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appintment);
    }

    /**
     * {@code DELETE  /appintments/:id} : delete the "id" appintment.
     *
     * @param id the id of the appintment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appintments/{id}")
    public ResponseEntity<Void> deleteAppintment(@PathVariable Long id) {
        log.debug("REST request to delete Appintment : {}", id);
        appintmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
