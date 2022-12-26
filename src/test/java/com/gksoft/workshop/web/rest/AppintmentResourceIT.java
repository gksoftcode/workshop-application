package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.Appintment;
import com.gksoft.workshop.repository.AppintmentRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AppintmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppintmentResourceIT {

    private static final Instant DEFAULT_APP_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_APP_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/appintments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppintmentRepository appintmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppintmentMockMvc;

    private Appintment appintment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appintment createEntity(EntityManager em) {
        Appintment appintment = new Appintment().appDate(DEFAULT_APP_DATE).notes(DEFAULT_NOTES);
        return appintment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appintment createUpdatedEntity(EntityManager em) {
        Appintment appintment = new Appintment().appDate(UPDATED_APP_DATE).notes(UPDATED_NOTES);
        return appintment;
    }

    @BeforeEach
    public void initTest() {
        appintment = createEntity(em);
    }

    @Test
    @Transactional
    void createAppintment() throws Exception {
        int databaseSizeBeforeCreate = appintmentRepository.findAll().size();
        // Create the Appintment
        restAppintmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appintment)))
            .andExpect(status().isCreated());

        // Validate the Appintment in the database
        List<Appintment> appintmentList = appintmentRepository.findAll();
        assertThat(appintmentList).hasSize(databaseSizeBeforeCreate + 1);
        Appintment testAppintment = appintmentList.get(appintmentList.size() - 1);
        assertThat(testAppintment.getAppDate()).isEqualTo(DEFAULT_APP_DATE);
        assertThat(testAppintment.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createAppintmentWithExistingId() throws Exception {
        // Create the Appintment with an existing ID
        appintment.setId(1L);

        int databaseSizeBeforeCreate = appintmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppintmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appintment)))
            .andExpect(status().isBadRequest());

        // Validate the Appintment in the database
        List<Appintment> appintmentList = appintmentRepository.findAll();
        assertThat(appintmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAppintments() throws Exception {
        // Initialize the database
        appintmentRepository.saveAndFlush(appintment);

        // Get all the appintmentList
        restAppintmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appintment.getId().intValue())))
            .andExpect(jsonPath("$.[*].appDate").value(hasItem(DEFAULT_APP_DATE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getAppintment() throws Exception {
        // Initialize the database
        appintmentRepository.saveAndFlush(appintment);

        // Get the appintment
        restAppintmentMockMvc
            .perform(get(ENTITY_API_URL_ID, appintment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appintment.getId().intValue()))
            .andExpect(jsonPath("$.appDate").value(DEFAULT_APP_DATE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingAppintment() throws Exception {
        // Get the appintment
        restAppintmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppintment() throws Exception {
        // Initialize the database
        appintmentRepository.saveAndFlush(appintment);

        int databaseSizeBeforeUpdate = appintmentRepository.findAll().size();

        // Update the appintment
        Appintment updatedAppintment = appintmentRepository.findById(appintment.getId()).get();
        // Disconnect from session so that the updates on updatedAppintment are not directly saved in db
        em.detach(updatedAppintment);
        updatedAppintment.appDate(UPDATED_APP_DATE).notes(UPDATED_NOTES);

        restAppintmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppintment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAppintment))
            )
            .andExpect(status().isOk());

        // Validate the Appintment in the database
        List<Appintment> appintmentList = appintmentRepository.findAll();
        assertThat(appintmentList).hasSize(databaseSizeBeforeUpdate);
        Appintment testAppintment = appintmentList.get(appintmentList.size() - 1);
        assertThat(testAppintment.getAppDate()).isEqualTo(UPDATED_APP_DATE);
        assertThat(testAppintment.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingAppintment() throws Exception {
        int databaseSizeBeforeUpdate = appintmentRepository.findAll().size();
        appintment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppintmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appintment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appintment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appintment in the database
        List<Appintment> appintmentList = appintmentRepository.findAll();
        assertThat(appintmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppintment() throws Exception {
        int databaseSizeBeforeUpdate = appintmentRepository.findAll().size();
        appintment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppintmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appintment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appintment in the database
        List<Appintment> appintmentList = appintmentRepository.findAll();
        assertThat(appintmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppintment() throws Exception {
        int databaseSizeBeforeUpdate = appintmentRepository.findAll().size();
        appintment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppintmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appintment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appintment in the database
        List<Appintment> appintmentList = appintmentRepository.findAll();
        assertThat(appintmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppintmentWithPatch() throws Exception {
        // Initialize the database
        appintmentRepository.saveAndFlush(appintment);

        int databaseSizeBeforeUpdate = appintmentRepository.findAll().size();

        // Update the appintment using partial update
        Appintment partialUpdatedAppintment = new Appintment();
        partialUpdatedAppintment.setId(appintment.getId());

        partialUpdatedAppintment.appDate(UPDATED_APP_DATE).notes(UPDATED_NOTES);

        restAppintmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppintment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppintment))
            )
            .andExpect(status().isOk());

        // Validate the Appintment in the database
        List<Appintment> appintmentList = appintmentRepository.findAll();
        assertThat(appintmentList).hasSize(databaseSizeBeforeUpdate);
        Appintment testAppintment = appintmentList.get(appintmentList.size() - 1);
        assertThat(testAppintment.getAppDate()).isEqualTo(UPDATED_APP_DATE);
        assertThat(testAppintment.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateAppintmentWithPatch() throws Exception {
        // Initialize the database
        appintmentRepository.saveAndFlush(appintment);

        int databaseSizeBeforeUpdate = appintmentRepository.findAll().size();

        // Update the appintment using partial update
        Appintment partialUpdatedAppintment = new Appintment();
        partialUpdatedAppintment.setId(appintment.getId());

        partialUpdatedAppintment.appDate(UPDATED_APP_DATE).notes(UPDATED_NOTES);

        restAppintmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppintment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppintment))
            )
            .andExpect(status().isOk());

        // Validate the Appintment in the database
        List<Appintment> appintmentList = appintmentRepository.findAll();
        assertThat(appintmentList).hasSize(databaseSizeBeforeUpdate);
        Appintment testAppintment = appintmentList.get(appintmentList.size() - 1);
        assertThat(testAppintment.getAppDate()).isEqualTo(UPDATED_APP_DATE);
        assertThat(testAppintment.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingAppintment() throws Exception {
        int databaseSizeBeforeUpdate = appintmentRepository.findAll().size();
        appintment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppintmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appintment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appintment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appintment in the database
        List<Appintment> appintmentList = appintmentRepository.findAll();
        assertThat(appintmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppintment() throws Exception {
        int databaseSizeBeforeUpdate = appintmentRepository.findAll().size();
        appintment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppintmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appintment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appintment in the database
        List<Appintment> appintmentList = appintmentRepository.findAll();
        assertThat(appintmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppintment() throws Exception {
        int databaseSizeBeforeUpdate = appintmentRepository.findAll().size();
        appintment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppintmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appintment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appintment in the database
        List<Appintment> appintmentList = appintmentRepository.findAll();
        assertThat(appintmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppintment() throws Exception {
        // Initialize the database
        appintmentRepository.saveAndFlush(appintment);

        int databaseSizeBeforeDelete = appintmentRepository.findAll().size();

        // Delete the appintment
        restAppintmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, appintment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Appintment> appintmentList = appintmentRepository.findAll();
        assertThat(appintmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
