package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.ItemModels;
import com.gksoft.workshop.repository.ItemModelsRepository;
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
 * Integration tests for the {@link ItemModelsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemModelsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/item-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemModelsRepository itemModelsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemModelsMockMvc;

    private ItemModels itemModels;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemModels createEntity(EntityManager em) {
        ItemModels itemModels = new ItemModels().name(DEFAULT_NAME);
        return itemModels;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemModels createUpdatedEntity(EntityManager em) {
        ItemModels itemModels = new ItemModels().name(UPDATED_NAME);
        return itemModels;
    }

    @BeforeEach
    public void initTest() {
        itemModels = createEntity(em);
    }

    @Test
    @Transactional
    void createItemModels() throws Exception {
        int databaseSizeBeforeCreate = itemModelsRepository.findAll().size();
        // Create the ItemModels
        restItemModelsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemModels)))
            .andExpect(status().isCreated());

        // Validate the ItemModels in the database
        List<ItemModels> itemModelsList = itemModelsRepository.findAll();
        assertThat(itemModelsList).hasSize(databaseSizeBeforeCreate + 1);
        ItemModels testItemModels = itemModelsList.get(itemModelsList.size() - 1);
        assertThat(testItemModels.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createItemModelsWithExistingId() throws Exception {
        // Create the ItemModels with an existing ID
        itemModels.setId(1L);

        int databaseSizeBeforeCreate = itemModelsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemModelsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemModels)))
            .andExpect(status().isBadRequest());

        // Validate the ItemModels in the database
        List<ItemModels> itemModelsList = itemModelsRepository.findAll();
        assertThat(itemModelsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllItemModels() throws Exception {
        // Initialize the database
        itemModelsRepository.saveAndFlush(itemModels);

        // Get all the itemModelsList
        restItemModelsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemModels.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getItemModels() throws Exception {
        // Initialize the database
        itemModelsRepository.saveAndFlush(itemModels);

        // Get the itemModels
        restItemModelsMockMvc
            .perform(get(ENTITY_API_URL_ID, itemModels.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemModels.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingItemModels() throws Exception {
        // Get the itemModels
        restItemModelsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItemModels() throws Exception {
        // Initialize the database
        itemModelsRepository.saveAndFlush(itemModels);

        int databaseSizeBeforeUpdate = itemModelsRepository.findAll().size();

        // Update the itemModels
        ItemModels updatedItemModels = itemModelsRepository.findById(itemModels.getId()).get();
        // Disconnect from session so that the updates on updatedItemModels are not directly saved in db
        em.detach(updatedItemModels);
        updatedItemModels.name(UPDATED_NAME);

        restItemModelsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedItemModels.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedItemModels))
            )
            .andExpect(status().isOk());

        // Validate the ItemModels in the database
        List<ItemModels> itemModelsList = itemModelsRepository.findAll();
        assertThat(itemModelsList).hasSize(databaseSizeBeforeUpdate);
        ItemModels testItemModels = itemModelsList.get(itemModelsList.size() - 1);
        assertThat(testItemModels.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingItemModels() throws Exception {
        int databaseSizeBeforeUpdate = itemModelsRepository.findAll().size();
        itemModels.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemModelsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemModels.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemModels))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemModels in the database
        List<ItemModels> itemModelsList = itemModelsRepository.findAll();
        assertThat(itemModelsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemModels() throws Exception {
        int databaseSizeBeforeUpdate = itemModelsRepository.findAll().size();
        itemModels.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemModelsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemModels))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemModels in the database
        List<ItemModels> itemModelsList = itemModelsRepository.findAll();
        assertThat(itemModelsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemModels() throws Exception {
        int databaseSizeBeforeUpdate = itemModelsRepository.findAll().size();
        itemModels.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemModelsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemModels)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemModels in the database
        List<ItemModels> itemModelsList = itemModelsRepository.findAll();
        assertThat(itemModelsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemModelsWithPatch() throws Exception {
        // Initialize the database
        itemModelsRepository.saveAndFlush(itemModels);

        int databaseSizeBeforeUpdate = itemModelsRepository.findAll().size();

        // Update the itemModels using partial update
        ItemModels partialUpdatedItemModels = new ItemModels();
        partialUpdatedItemModels.setId(itemModels.getId());

        restItemModelsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemModels.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemModels))
            )
            .andExpect(status().isOk());

        // Validate the ItemModels in the database
        List<ItemModels> itemModelsList = itemModelsRepository.findAll();
        assertThat(itemModelsList).hasSize(databaseSizeBeforeUpdate);
        ItemModels testItemModels = itemModelsList.get(itemModelsList.size() - 1);
        assertThat(testItemModels.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateItemModelsWithPatch() throws Exception {
        // Initialize the database
        itemModelsRepository.saveAndFlush(itemModels);

        int databaseSizeBeforeUpdate = itemModelsRepository.findAll().size();

        // Update the itemModels using partial update
        ItemModels partialUpdatedItemModels = new ItemModels();
        partialUpdatedItemModels.setId(itemModels.getId());

        partialUpdatedItemModels.name(UPDATED_NAME);

        restItemModelsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemModels.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemModels))
            )
            .andExpect(status().isOk());

        // Validate the ItemModels in the database
        List<ItemModels> itemModelsList = itemModelsRepository.findAll();
        assertThat(itemModelsList).hasSize(databaseSizeBeforeUpdate);
        ItemModels testItemModels = itemModelsList.get(itemModelsList.size() - 1);
        assertThat(testItemModels.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingItemModels() throws Exception {
        int databaseSizeBeforeUpdate = itemModelsRepository.findAll().size();
        itemModels.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemModelsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemModels.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemModels))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemModels in the database
        List<ItemModels> itemModelsList = itemModelsRepository.findAll();
        assertThat(itemModelsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemModels() throws Exception {
        int databaseSizeBeforeUpdate = itemModelsRepository.findAll().size();
        itemModels.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemModelsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemModels))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemModels in the database
        List<ItemModels> itemModelsList = itemModelsRepository.findAll();
        assertThat(itemModelsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemModels() throws Exception {
        int databaseSizeBeforeUpdate = itemModelsRepository.findAll().size();
        itemModels.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemModelsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(itemModels))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemModels in the database
        List<ItemModels> itemModelsList = itemModelsRepository.findAll();
        assertThat(itemModelsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemModels() throws Exception {
        // Initialize the database
        itemModelsRepository.saveAndFlush(itemModels);

        int databaseSizeBeforeDelete = itemModelsRepository.findAll().size();

        // Delete the itemModels
        restItemModelsMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemModels.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemModels> itemModelsList = itemModelsRepository.findAll();
        assertThat(itemModelsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
