package com.gksoft.workshop.web.rest;

import com.gksoft.workshop.domain.ItemModels;
import com.gksoft.workshop.repository.ItemModelsRepository;
import com.gksoft.workshop.service.ItemModelsService;
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
 * REST controller for managing {@link com.gksoft.workshop.domain.ItemModels}.
 */
@RestController
@RequestMapping("/api")
public class ItemModelsResource {

    private final Logger log = LoggerFactory.getLogger(ItemModelsResource.class);

    private static final String ENTITY_NAME = "itemModels";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemModelsService itemModelsService;

    private final ItemModelsRepository itemModelsRepository;

    public ItemModelsResource(ItemModelsService itemModelsService, ItemModelsRepository itemModelsRepository) {
        this.itemModelsService = itemModelsService;
        this.itemModelsRepository = itemModelsRepository;
    }

    /**
     * {@code POST  /item-models} : Create a new itemModels.
     *
     * @param itemModels the itemModels to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemModels, or with status {@code 400 (Bad Request)} if the itemModels has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-models")
    public ResponseEntity<ItemModels> createItemModels(@RequestBody ItemModels itemModels) throws URISyntaxException {
        log.debug("REST request to save ItemModels : {}", itemModels);
        if (itemModels.getId() != null) {
            throw new BadRequestAlertException("A new itemModels cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemModels result = itemModelsService.save(itemModels);
        return ResponseEntity
            .created(new URI("/api/item-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-models/:id} : Updates an existing itemModels.
     *
     * @param id the id of the itemModels to save.
     * @param itemModels the itemModels to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemModels,
     * or with status {@code 400 (Bad Request)} if the itemModels is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemModels couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-models/{id}")
    public ResponseEntity<ItemModels> updateItemModels(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemModels itemModels
    ) throws URISyntaxException {
        log.debug("REST request to update ItemModels : {}, {}", id, itemModels);
        if (itemModels.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemModels.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemModelsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemModels result = itemModelsService.update(itemModels);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemModels.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-models/:id} : Partial updates given fields of an existing itemModels, field will ignore if it is null
     *
     * @param id the id of the itemModels to save.
     * @param itemModels the itemModels to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemModels,
     * or with status {@code 400 (Bad Request)} if the itemModels is not valid,
     * or with status {@code 404 (Not Found)} if the itemModels is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemModels couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-models/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ItemModels> partialUpdateItemModels(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemModels itemModels
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemModels partially : {}, {}", id, itemModels);
        if (itemModels.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemModels.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemModelsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemModels> result = itemModelsService.partialUpdate(itemModels);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemModels.getId().toString())
        );
    }

    /**
     * {@code GET  /item-models} : get all the itemModels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemModels in body.
     */
    @GetMapping("/item-models")
    public ResponseEntity<List<ItemModels>> getAllItemModels(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ItemModels");
        Page<ItemModels> page = itemModelsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-models/:id} : get the "id" itemModels.
     *
     * @param id the id of the itemModels to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemModels, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-models/{id}")
    public ResponseEntity<ItemModels> getItemModels(@PathVariable Long id) {
        log.debug("REST request to get ItemModels : {}", id);
        Optional<ItemModels> itemModels = itemModelsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemModels);
    }

    /**
     * {@code DELETE  /item-models/:id} : delete the "id" itemModels.
     *
     * @param id the id of the itemModels to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-models/{id}")
    public ResponseEntity<Void> deleteItemModels(@PathVariable Long id) {
        log.debug("REST request to delete ItemModels : {}", id);
        itemModelsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
