package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.Tax;
import com.gksoft.workshop.repository.TaxRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TaxResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaxResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_TAX_VALUE = 1L;
    private static final Long UPDATED_TAX_VALUE = 2L;

    private static final String ENTITY_API_URL = "/api/taxes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaxRepository taxRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaxMockMvc;

    private Tax tax;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tax createEntity(EntityManager em) {
        Tax tax = new Tax().name(DEFAULT_NAME).taxValue(DEFAULT_TAX_VALUE);
        return tax;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tax createUpdatedEntity(EntityManager em) {
        Tax tax = new Tax().name(UPDATED_NAME).taxValue(UPDATED_TAX_VALUE);
        return tax;
    }

    @BeforeEach
    public void initTest() {
        tax = createEntity(em);
    }

    @Test
    @Transactional
    void createTax() throws Exception {
        int databaseSizeBeforeCreate = taxRepository.findAll().size();
        // Create the Tax
        restTaxMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tax)))
            .andExpect(status().isCreated());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeCreate + 1);
        Tax testTax = taxList.get(taxList.size() - 1);
        assertThat(testTax.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTax.getTaxValue()).isEqualTo(DEFAULT_TAX_VALUE);
    }

    @Test
    @Transactional
    void createTaxWithExistingId() throws Exception {
        // Create the Tax with an existing ID
        tax.setId(1L);

        int databaseSizeBeforeCreate = taxRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tax)))
            .andExpect(status().isBadRequest());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTaxes() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxList
        restTaxMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tax.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].taxValue").value(hasItem(DEFAULT_TAX_VALUE.intValue())));
    }

    @Test
    @Transactional
    void getTax() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get the tax
        restTaxMockMvc
            .perform(get(ENTITY_API_URL_ID, tax.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tax.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.taxValue").value(DEFAULT_TAX_VALUE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTax() throws Exception {
        // Get the tax
        restTaxMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTax() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        int databaseSizeBeforeUpdate = taxRepository.findAll().size();

        // Update the tax
        Tax updatedTax = taxRepository.findById(tax.getId()).get();
        // Disconnect from session so that the updates on updatedTax are not directly saved in db
        em.detach(updatedTax);
        updatedTax.name(UPDATED_NAME).taxValue(UPDATED_TAX_VALUE);

        restTaxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTax.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTax))
            )
            .andExpect(status().isOk());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeUpdate);
        Tax testTax = taxList.get(taxList.size() - 1);
        assertThat(testTax.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTax.getTaxValue()).isEqualTo(UPDATED_TAX_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingTax() throws Exception {
        int databaseSizeBeforeUpdate = taxRepository.findAll().size();
        tax.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tax.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tax))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTax() throws Exception {
        int databaseSizeBeforeUpdate = taxRepository.findAll().size();
        tax.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tax))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTax() throws Exception {
        int databaseSizeBeforeUpdate = taxRepository.findAll().size();
        tax.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tax)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaxWithPatch() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        int databaseSizeBeforeUpdate = taxRepository.findAll().size();

        // Update the tax using partial update
        Tax partialUpdatedTax = new Tax();
        partialUpdatedTax.setId(tax.getId());

        partialUpdatedTax.name(UPDATED_NAME).taxValue(UPDATED_TAX_VALUE);

        restTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTax.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTax))
            )
            .andExpect(status().isOk());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeUpdate);
        Tax testTax = taxList.get(taxList.size() - 1);
        assertThat(testTax.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTax.getTaxValue()).isEqualTo(UPDATED_TAX_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateTaxWithPatch() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        int databaseSizeBeforeUpdate = taxRepository.findAll().size();

        // Update the tax using partial update
        Tax partialUpdatedTax = new Tax();
        partialUpdatedTax.setId(tax.getId());

        partialUpdatedTax.name(UPDATED_NAME).taxValue(UPDATED_TAX_VALUE);

        restTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTax.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTax))
            )
            .andExpect(status().isOk());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeUpdate);
        Tax testTax = taxList.get(taxList.size() - 1);
        assertThat(testTax.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTax.getTaxValue()).isEqualTo(UPDATED_TAX_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingTax() throws Exception {
        int databaseSizeBeforeUpdate = taxRepository.findAll().size();
        tax.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tax.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tax))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTax() throws Exception {
        int databaseSizeBeforeUpdate = taxRepository.findAll().size();
        tax.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tax))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTax() throws Exception {
        int databaseSizeBeforeUpdate = taxRepository.findAll().size();
        tax.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tax)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tax in the database
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTax() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        int databaseSizeBeforeDelete = taxRepository.findAll().size();

        // Delete the tax
        restTaxMockMvc.perform(delete(ENTITY_API_URL_ID, tax.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tax> taxList = taxRepository.findAll();
        assertThat(taxList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
