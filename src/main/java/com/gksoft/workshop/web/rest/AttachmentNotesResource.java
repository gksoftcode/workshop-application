package com.gksoft.workshop.web.rest;

import com.gksoft.workshop.domain.AttachmentNotes;
import com.gksoft.workshop.repository.AttachmentNotesRepository;
import com.gksoft.workshop.service.AttachmentNotesService;
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
 * REST controller for managing {@link com.gksoft.workshop.domain.AttachmentNotes}.
 */
@RestController
@RequestMapping("/api")
public class AttachmentNotesResource {

    private final Logger log = LoggerFactory.getLogger(AttachmentNotesResource.class);

    private static final String ENTITY_NAME = "attachmentNotes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttachmentNotesService attachmentNotesService;

    private final AttachmentNotesRepository attachmentNotesRepository;

    public AttachmentNotesResource(AttachmentNotesService attachmentNotesService, AttachmentNotesRepository attachmentNotesRepository) {
        this.attachmentNotesService = attachmentNotesService;
        this.attachmentNotesRepository = attachmentNotesRepository;
    }

    /**
     * {@code POST  /attachment-notes} : Create a new attachmentNotes.
     *
     * @param attachmentNotes the attachmentNotes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attachmentNotes, or with status {@code 400 (Bad Request)} if the attachmentNotes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attachment-notes")
    public ResponseEntity<AttachmentNotes> createAttachmentNotes(@RequestBody AttachmentNotes attachmentNotes) throws URISyntaxException {
        log.debug("REST request to save AttachmentNotes : {}", attachmentNotes);
        if (attachmentNotes.getId() != null) {
            throw new BadRequestAlertException("A new attachmentNotes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttachmentNotes result = attachmentNotesService.save(attachmentNotes);
        return ResponseEntity
            .created(new URI("/api/attachment-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attachment-notes/:id} : Updates an existing attachmentNotes.
     *
     * @param id the id of the attachmentNotes to save.
     * @param attachmentNotes the attachmentNotes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachmentNotes,
     * or with status {@code 400 (Bad Request)} if the attachmentNotes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attachmentNotes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attachment-notes/{id}")
    public ResponseEntity<AttachmentNotes> updateAttachmentNotes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AttachmentNotes attachmentNotes
    ) throws URISyntaxException {
        log.debug("REST request to update AttachmentNotes : {}, {}", id, attachmentNotes);
        if (attachmentNotes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachmentNotes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attachmentNotesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AttachmentNotes result = attachmentNotesService.update(attachmentNotes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attachmentNotes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /attachment-notes/:id} : Partial updates given fields of an existing attachmentNotes, field will ignore if it is null
     *
     * @param id the id of the attachmentNotes to save.
     * @param attachmentNotes the attachmentNotes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachmentNotes,
     * or with status {@code 400 (Bad Request)} if the attachmentNotes is not valid,
     * or with status {@code 404 (Not Found)} if the attachmentNotes is not found,
     * or with status {@code 500 (Internal Server Error)} if the attachmentNotes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attachment-notes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AttachmentNotes> partialUpdateAttachmentNotes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AttachmentNotes attachmentNotes
    ) throws URISyntaxException {
        log.debug("REST request to partial update AttachmentNotes partially : {}, {}", id, attachmentNotes);
        if (attachmentNotes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachmentNotes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attachmentNotesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AttachmentNotes> result = attachmentNotesService.partialUpdate(attachmentNotes);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attachmentNotes.getId().toString())
        );
    }

    /**
     * {@code GET  /attachment-notes} : get all the attachmentNotes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attachmentNotes in body.
     */
    @GetMapping("/attachment-notes")
    public ResponseEntity<List<AttachmentNotes>> getAllAttachmentNotes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of AttachmentNotes");
        Page<AttachmentNotes> page;
        if (eagerload) {
            page = attachmentNotesService.findAllWithEagerRelationships(pageable);
        } else {
            page = attachmentNotesService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /attachment-notes/:id} : get the "id" attachmentNotes.
     *
     * @param id the id of the attachmentNotes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attachmentNotes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attachment-notes/{id}")
    public ResponseEntity<AttachmentNotes> getAttachmentNotes(@PathVariable Long id) {
        log.debug("REST request to get AttachmentNotes : {}", id);
        Optional<AttachmentNotes> attachmentNotes = attachmentNotesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attachmentNotes);
    }

    /**
     * {@code DELETE  /attachment-notes/:id} : delete the "id" attachmentNotes.
     *
     * @param id the id of the attachmentNotes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attachment-notes/{id}")
    public ResponseEntity<Void> deleteAttachmentNotes(@PathVariable Long id) {
        log.debug("REST request to delete AttachmentNotes : {}", id);
        attachmentNotesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
