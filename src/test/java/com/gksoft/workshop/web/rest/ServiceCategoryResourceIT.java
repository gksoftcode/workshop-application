package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.ServiceCategory;
import com.gksoft.workshop.repository.ServiceCategoryRepository;
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
 * Integration tests for the {@link ServiceCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/service-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceCategoryMockMvc;

    private ServiceCategory serviceCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceCategory createEntity(EntityManager em) {
        ServiceCategory serviceCategory = new ServiceCategory().categoryName(DEFAULT_CATEGORY_NAME);
        return serviceCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceCategory createUpdatedEntity(EntityManager em) {
        ServiceCategory serviceCategory = new ServiceCategory().categoryName(UPDATED_CATEGORY_NAME);
        return serviceCategory;
    }

    @BeforeEach
    public void initTest() {
        serviceCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createServiceCategory() throws Exception {
        int databaseSizeBeforeCreate = serviceCategoryRepository.findAll().size();
        // Create the ServiceCategory
        restServiceCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isCreated());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceCategory testServiceCategory = serviceCategoryList.get(serviceCategoryList.size() - 1);
        assertThat(testServiceCategory.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void createServiceCategoryWithExistingId() throws Exception {
        // Create the ServiceCategory with an existing ID
        serviceCategory.setId(1L);

        int databaseSizeBeforeCreate = serviceCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllServiceCategories() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);

        // Get all the serviceCategoryList
        restServiceCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));
    }

    @Test
    @Transactional
    void getServiceCategory() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);

        // Get the serviceCategory
        restServiceCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingServiceCategory() throws Exception {
        // Get the serviceCategory
        restServiceCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceCategory() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);

        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();

        // Update the serviceCategory
        ServiceCategory updatedServiceCategory = serviceCategoryRepository.findById(serviceCategory.getId()).get();
        // Disconnect from session so that the updates on updatedServiceCategory are not directly saved in db
        em.detach(updatedServiceCategory);
        updatedServiceCategory.categoryName(UPDATED_CATEGORY_NAME);

        restServiceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServiceCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedServiceCategory))
            )
            .andExpect(status().isOk());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
        ServiceCategory testServiceCategory = serviceCategoryList.get(serviceCategoryList.size() - 1);
        assertThat(testServiceCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingServiceCategory() throws Exception {
        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();
        serviceCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceCategory() throws Exception {
        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();
        serviceCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceCategory() throws Exception {
        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();
        serviceCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceCategoryWithPatch() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);

        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();

        // Update the serviceCategory using partial update
        ServiceCategory partialUpdatedServiceCategory = new ServiceCategory();
        partialUpdatedServiceCategory.setId(serviceCategory.getId());

        restServiceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceCategory))
            )
            .andExpect(status().isOk());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
        ServiceCategory testServiceCategory = serviceCategoryList.get(serviceCategoryList.size() - 1);
        assertThat(testServiceCategory.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateServiceCategoryWithPatch() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);

        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();

        // Update the serviceCategory using partial update
        ServiceCategory partialUpdatedServiceCategory = new ServiceCategory();
        partialUpdatedServiceCategory.setId(serviceCategory.getId());

        partialUpdatedServiceCategory.categoryName(UPDATED_CATEGORY_NAME);

        restServiceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceCategory))
            )
            .andExpect(status().isOk());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
        ServiceCategory testServiceCategory = serviceCategoryList.get(serviceCategoryList.size() - 1);
        assertThat(testServiceCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingServiceCategory() throws Exception {
        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();
        serviceCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceCategory() throws Exception {
        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();
        serviceCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceCategory() throws Exception {
        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();
        serviceCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceCategory() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);

        int databaseSizeBeforeDelete = serviceCategoryRepository.findAll().size();

        // Delete the serviceCategory
        restServiceCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceCategory> serviceCategoryList = serviceCategoryRepository.findAll();
        assertThat(serviceCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
