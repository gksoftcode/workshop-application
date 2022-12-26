package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.ItemBrand;
import com.gksoft.workshop.repository.ItemBrandRepository;
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
 * Integration tests for the {@link ItemBrandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemBrandResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/item-brands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemBrandRepository itemBrandRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemBrandMockMvc;

    private ItemBrand itemBrand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemBrand createEntity(EntityManager em) {
        ItemBrand itemBrand = new ItemBrand().name(DEFAULT_NAME);
        return itemBrand;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemBrand createUpdatedEntity(EntityManager em) {
        ItemBrand itemBrand = new ItemBrand().name(UPDATED_NAME);
        return itemBrand;
    }

    @BeforeEach
    public void initTest() {
        itemBrand = createEntity(em);
    }

    @Test
    @Transactional
    void createItemBrand() throws Exception {
        int databaseSizeBeforeCreate = itemBrandRepository.findAll().size();
        // Create the ItemBrand
        restItemBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemBrand)))
            .andExpect(status().isCreated());

        // Validate the ItemBrand in the database
        List<ItemBrand> itemBrandList = itemBrandRepository.findAll();
        assertThat(itemBrandList).hasSize(databaseSizeBeforeCreate + 1);
        ItemBrand testItemBrand = itemBrandList.get(itemBrandList.size() - 1);
        assertThat(testItemBrand.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createItemBrandWithExistingId() throws Exception {
        // Create the ItemBrand with an existing ID
        itemBrand.setId(1L);

        int databaseSizeBeforeCreate = itemBrandRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemBrand)))
            .andExpect(status().isBadRequest());

        // Validate the ItemBrand in the database
        List<ItemBrand> itemBrandList = itemBrandRepository.findAll();
        assertThat(itemBrandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllItemBrands() throws Exception {
        // Initialize the database
        itemBrandRepository.saveAndFlush(itemBrand);

        // Get all the itemBrandList
        restItemBrandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemBrand.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getItemBrand() throws Exception {
        // Initialize the database
        itemBrandRepository.saveAndFlush(itemBrand);

        // Get the itemBrand
        restItemBrandMockMvc
            .perform(get(ENTITY_API_URL_ID, itemBrand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemBrand.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingItemBrand() throws Exception {
        // Get the itemBrand
        restItemBrandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItemBrand() throws Exception {
        // Initialize the database
        itemBrandRepository.saveAndFlush(itemBrand);

        int databaseSizeBeforeUpdate = itemBrandRepository.findAll().size();

        // Update the itemBrand
        ItemBrand updatedItemBrand = itemBrandRepository.findById(itemBrand.getId()).get();
        // Disconnect from session so that the updates on updatedItemBrand are not directly saved in db
        em.detach(updatedItemBrand);
        updatedItemBrand.name(UPDATED_NAME);

        restItemBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedItemBrand.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedItemBrand))
            )
            .andExpect(status().isOk());

        // Validate the ItemBrand in the database
        List<ItemBrand> itemBrandList = itemBrandRepository.findAll();
        assertThat(itemBrandList).hasSize(databaseSizeBeforeUpdate);
        ItemBrand testItemBrand = itemBrandList.get(itemBrandList.size() - 1);
        assertThat(testItemBrand.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingItemBrand() throws Exception {
        int databaseSizeBeforeUpdate = itemBrandRepository.findAll().size();
        itemBrand.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemBrand.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemBrand))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemBrand in the database
        List<ItemBrand> itemBrandList = itemBrandRepository.findAll();
        assertThat(itemBrandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemBrand() throws Exception {
        int databaseSizeBeforeUpdate = itemBrandRepository.findAll().size();
        itemBrand.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemBrand))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemBrand in the database
        List<ItemBrand> itemBrandList = itemBrandRepository.findAll();
        assertThat(itemBrandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemBrand() throws Exception {
        int databaseSizeBeforeUpdate = itemBrandRepository.findAll().size();
        itemBrand.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemBrandMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemBrand)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemBrand in the database
        List<ItemBrand> itemBrandList = itemBrandRepository.findAll();
        assertThat(itemBrandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemBrandWithPatch() throws Exception {
        // Initialize the database
        itemBrandRepository.saveAndFlush(itemBrand);

        int databaseSizeBeforeUpdate = itemBrandRepository.findAll().size();

        // Update the itemBrand using partial update
        ItemBrand partialUpdatedItemBrand = new ItemBrand();
        partialUpdatedItemBrand.setId(itemBrand.getId());

        partialUpdatedItemBrand.name(UPDATED_NAME);

        restItemBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemBrand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemBrand))
            )
            .andExpect(status().isOk());

        // Validate the ItemBrand in the database
        List<ItemBrand> itemBrandList = itemBrandRepository.findAll();
        assertThat(itemBrandList).hasSize(databaseSizeBeforeUpdate);
        ItemBrand testItemBrand = itemBrandList.get(itemBrandList.size() - 1);
        assertThat(testItemBrand.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateItemBrandWithPatch() throws Exception {
        // Initialize the database
        itemBrandRepository.saveAndFlush(itemBrand);

        int databaseSizeBeforeUpdate = itemBrandRepository.findAll().size();

        // Update the itemBrand using partial update
        ItemBrand partialUpdatedItemBrand = new ItemBrand();
        partialUpdatedItemBrand.setId(itemBrand.getId());

        partialUpdatedItemBrand.name(UPDATED_NAME);

        restItemBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemBrand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemBrand))
            )
            .andExpect(status().isOk());

        // Validate the ItemBrand in the database
        List<ItemBrand> itemBrandList = itemBrandRepository.findAll();
        assertThat(itemBrandList).hasSize(databaseSizeBeforeUpdate);
        ItemBrand testItemBrand = itemBrandList.get(itemBrandList.size() - 1);
        assertThat(testItemBrand.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingItemBrand() throws Exception {
        int databaseSizeBeforeUpdate = itemBrandRepository.findAll().size();
        itemBrand.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemBrand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemBrand))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemBrand in the database
        List<ItemBrand> itemBrandList = itemBrandRepository.findAll();
        assertThat(itemBrandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemBrand() throws Exception {
        int databaseSizeBeforeUpdate = itemBrandRepository.findAll().size();
        itemBrand.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemBrand))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemBrand in the database
        List<ItemBrand> itemBrandList = itemBrandRepository.findAll();
        assertThat(itemBrandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemBrand() throws Exception {
        int databaseSizeBeforeUpdate = itemBrandRepository.findAll().size();
        itemBrand.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemBrandMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(itemBrand))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemBrand in the database
        List<ItemBrand> itemBrandList = itemBrandRepository.findAll();
        assertThat(itemBrandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemBrand() throws Exception {
        // Initialize the database
        itemBrandRepository.saveAndFlush(itemBrand);

        int databaseSizeBeforeDelete = itemBrandRepository.findAll().size();

        // Delete the itemBrand
        restItemBrandMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemBrand.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemBrand> itemBrandList = itemBrandRepository.findAll();
        assertThat(itemBrandList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
