package com.gksoft.workshop.web.rest;

import com.gksoft.workshop.domain.PaymentCredit;
import com.gksoft.workshop.repository.PaymentCreditRepository;
import com.gksoft.workshop.service.PaymentCreditService;
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
 * REST controller for managing {@link com.gksoft.workshop.domain.PaymentCredit}.
 */
@RestController
@RequestMapping("/api")
public class PaymentCreditResource {

    private final Logger log = LoggerFactory.getLogger(PaymentCreditResource.class);

    private static final String ENTITY_NAME = "paymentCredit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentCreditService paymentCreditService;

    private final PaymentCreditRepository paymentCreditRepository;

    public PaymentCreditResource(PaymentCreditService paymentCreditService, PaymentCreditRepository paymentCreditRepository) {
        this.paymentCreditService = paymentCreditService;
        this.paymentCreditRepository = paymentCreditRepository;
    }

    /**
     * {@code POST  /payment-credits} : Create a new paymentCredit.
     *
     * @param paymentCredit the paymentCredit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentCredit, or with status {@code 400 (Bad Request)} if the paymentCredit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-credits")
    public ResponseEntity<PaymentCredit> createPaymentCredit(@RequestBody PaymentCredit paymentCredit) throws URISyntaxException {
        log.debug("REST request to save PaymentCredit : {}", paymentCredit);
        if (paymentCredit.getId() != null) {
            throw new BadRequestAlertException("A new paymentCredit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentCredit result = paymentCreditService.save(paymentCredit);
        return ResponseEntity
            .created(new URI("/api/payment-credits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-credits/:id} : Updates an existing paymentCredit.
     *
     * @param id the id of the paymentCredit to save.
     * @param paymentCredit the paymentCredit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentCredit,
     * or with status {@code 400 (Bad Request)} if the paymentCredit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentCredit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-credits/{id}")
    public ResponseEntity<PaymentCredit> updatePaymentCredit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentCredit paymentCredit
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentCredit : {}, {}", id, paymentCredit);
        if (paymentCredit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentCredit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentCreditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentCredit result = paymentCreditService.update(paymentCredit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentCredit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payment-credits/:id} : Partial updates given fields of an existing paymentCredit, field will ignore if it is null
     *
     * @param id the id of the paymentCredit to save.
     * @param paymentCredit the paymentCredit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentCredit,
     * or with status {@code 400 (Bad Request)} if the paymentCredit is not valid,
     * or with status {@code 404 (Not Found)} if the paymentCredit is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentCredit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-credits/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentCredit> partialUpdatePaymentCredit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentCredit paymentCredit
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentCredit partially : {}, {}", id, paymentCredit);
        if (paymentCredit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentCredit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentCreditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentCredit> result = paymentCreditService.partialUpdate(paymentCredit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentCredit.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-credits} : get all the paymentCredits.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentCredits in body.
     */
    @GetMapping("/payment-credits")
    public ResponseEntity<List<PaymentCredit>> getAllPaymentCredits(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of PaymentCredits");
        Page<PaymentCredit> page;
        if (eagerload) {
            page = paymentCreditService.findAllWithEagerRelationships(pageable);
        } else {
            page = paymentCreditService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-credits/:id} : get the "id" paymentCredit.
     *
     * @param id the id of the paymentCredit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentCredit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-credits/{id}")
    public ResponseEntity<PaymentCredit> getPaymentCredit(@PathVariable Long id) {
        log.debug("REST request to get PaymentCredit : {}", id);
        Optional<PaymentCredit> paymentCredit = paymentCreditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentCredit);
    }

    /**
     * {@code DELETE  /payment-credits/:id} : delete the "id" paymentCredit.
     *
     * @param id the id of the paymentCredit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-credits/{id}")
    public ResponseEntity<Void> deletePaymentCredit(@PathVariable Long id) {
        log.debug("REST request to delete PaymentCredit : {}", id);
        paymentCreditService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
