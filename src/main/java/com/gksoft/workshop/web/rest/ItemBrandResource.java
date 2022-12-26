package com.gksoft.workshop.web.rest;

import com.gksoft.workshop.domain.ItemBrand;
import com.gksoft.workshop.repository.ItemBrandRepository;
import com.gksoft.workshop.service.ItemBrandService;
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
 * REST controller for managing {@link com.gksoft.workshop.domain.ItemBrand}.
 */
@RestController
@RequestMapping("/api")
public class ItemBrandResource {

    private final Logger log = LoggerFactory.getLogger(ItemBrandResource.class);

    private static final String ENTITY_NAME = "itemBrand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemBrandService itemBrandService;

    private final ItemBrandRepository itemBrandRepository;

    public ItemBrandResource(ItemBrandService itemBrandService, ItemBrandRepository itemBrandRepository) {
        this.itemBrandService = itemBrandService;
        this.itemBrandRepository = itemBrandRepository;
    }

    /**
     * {@code POST  /item-brands} : Create a new itemBrand.
     *
     * @param itemBrand the itemBrand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemBrand, or with status {@code 400 (Bad Request)} if the itemBrand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-brands")
    public ResponseEntity<ItemBrand> createItemBrand(@RequestBody ItemBrand itemBrand) throws URISyntaxException {
        log.debug("REST request to save ItemBrand : {}", itemBrand);
        if (itemBrand.getId() != null) {
            throw new BadRequestAlertException("A new itemBrand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemBrand result = itemBrandService.save(itemBrand);
        return ResponseEntity
            .created(new URI("/api/item-brands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-brands/:id} : Updates an existing itemBrand.
     *
     * @param id the id of the itemBrand to save.
     * @param itemBrand the itemBrand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemBrand,
     * or with status {@code 400 (Bad Request)} if the itemBrand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemBrand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-brands/{id}")
    public ResponseEntity<ItemBrand> updateItemBrand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemBrand itemBrand
    ) throws URISyntaxException {
        log.debug("REST request to update ItemBrand : {}, {}", id, itemBrand);
        if (itemBrand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemBrand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemBrandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemBrand result = itemBrandService.update(itemBrand);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemBrand.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-brands/:id} : Partial updates given fields of an existing itemBrand, field will ignore if it is null
     *
     * @param id the id of the itemBrand to save.
     * @param itemBrand the itemBrand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemBrand,
     * or with status {@code 400 (Bad Request)} if the itemBrand is not valid,
     * or with status {@code 404 (Not Found)} if the itemBrand is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemBrand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-brands/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ItemBrand> partialUpdateItemBrand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemBrand itemBrand
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemBrand partially : {}, {}", id, itemBrand);
        if (itemBrand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemBrand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemBrandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemBrand> result = itemBrandService.partialUpdate(itemBrand);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemBrand.getId().toString())
        );
    }

    /**
     * {@code GET  /item-brands} : get all the itemBrands.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemBrands in body.
     */
    @GetMapping("/item-brands")
    public ResponseEntity<List<ItemBrand>> getAllItemBrands(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ItemBrands");
        Page<ItemBrand> page = itemBrandService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-brands/:id} : get the "id" itemBrand.
     *
     * @param id the id of the itemBrand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemBrand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-brands/{id}")
    public ResponseEntity<ItemBrand> getItemBrand(@PathVariable Long id) {
        log.debug("REST request to get ItemBrand : {}", id);
        Optional<ItemBrand> itemBrand = itemBrandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemBrand);
    }

    /**
     * {@code DELETE  /item-brands/:id} : delete the "id" itemBrand.
     *
     * @param id the id of the itemBrand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-brands/{id}")
    public ResponseEntity<Void> deleteItemBrand(@PathVariable Long id) {
        log.debug("REST request to delete ItemBrand : {}", id);
        itemBrandService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
