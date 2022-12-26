package com.gksoft.workshop.web.rest;

import com.gksoft.workshop.domain.Attachments;
import com.gksoft.workshop.repository.AttachmentsRepository;
import com.gksoft.workshop.service.AttachmentsService;
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
 * REST controller for managing {@link com.gksoft.workshop.domain.Attachments}.
 */
@RestController
@RequestMapping("/api")
public class AttachmentsResource {

    private final Logger log = LoggerFactory.getLogger(AttachmentsResource.class);

    private static final String ENTITY_NAME = "attachments";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttachmentsService attachmentsService;

    private final AttachmentsRepository attachmentsRepository;

    public AttachmentsResource(AttachmentsService attachmentsService, AttachmentsRepository attachmentsRepository) {
        this.attachmentsService = attachmentsService;
        this.attachmentsRepository = attachmentsRepository;
    }

    /**
     * {@code POST  /attachments} : Create a new attachments.
     *
     * @param attachments the attachments to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attachments, or with status {@code 400 (Bad Request)} if the attachments has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attachments")
    public ResponseEntity<Attachments> createAttachments(@RequestBody Attachments attachments) throws URISyntaxException {
        log.debug("REST request to save Attachments : {}", attachments);
        if (attachments.getId() != null) {
            throw new BadRequestAlertException("A new attachments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Attachments result = attachmentsService.save(attachments);
        return ResponseEntity
            .created(new URI("/api/attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attachments/:id} : Updates an existing attachments.
     *
     * @param id the id of the attachments to save.
     * @param attachments the attachments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachments,
     * or with status {@code 400 (Bad Request)} if the attachments is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attachments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attachments/{id}")
    public ResponseEntity<Attachments> updateAttachments(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Attachments attachments
    ) throws URISyntaxException {
        log.debug("REST request to update Attachments : {}, {}", id, attachments);
        if (attachments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attachmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Attachments result = attachmentsService.update(attachments);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attachments.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /attachments/:id} : Partial updates given fields of an existing attachments, field will ignore if it is null
     *
     * @param id the id of the attachments to save.
     * @param attachments the attachments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachments,
     * or with status {@code 400 (Bad Request)} if the attachments is not valid,
     * or with status {@code 404 (Not Found)} if the attachments is not found,
     * or with status {@code 500 (Internal Server Error)} if the attachments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attachments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Attachments> partialUpdateAttachments(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Attachments attachments
    ) throws URISyntaxException {
        log.debug("REST request to partial update Attachments partially : {}, {}", id, attachments);
        if (attachments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attachmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Attachments> result = attachmentsService.partialUpdate(attachments);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attachments.getId().toString())
        );
    }

    /**
     * {@code GET  /attachments} : get all the attachments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attachments in body.
     */
    @GetMapping("/attachments")
    public ResponseEntity<List<Attachments>> getAllAttachments(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Attachments");
        Page<Attachments> page = attachmentsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /attachments/:id} : get the "id" attachments.
     *
     * @param id the id of the attachments to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attachments, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attachments/{id}")
    public ResponseEntity<Attachments> getAttachments(@PathVariable Long id) {
        log.debug("REST request to get Attachments : {}", id);
        Optional<Attachments> attachments = attachmentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attachments);
    }

    /**
     * {@code DELETE  /attachments/:id} : delete the "id" attachments.
     *
     * @param id the id of the attachments to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attachments/{id}")
    public ResponseEntity<Void> deleteAttachments(@PathVariable Long id) {
        log.debug("REST request to delete Attachments : {}", id);
        attachmentsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
