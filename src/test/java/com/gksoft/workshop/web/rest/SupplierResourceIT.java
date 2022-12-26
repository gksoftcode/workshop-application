package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.Supplier;
import com.gksoft.workshop.repository.SupplierRepository;
import com.gksoft.workshop.service.SupplierService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SupplierResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_PONE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_PONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/suppliers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierRepository supplierRepositoryMock;

    @Mock
    private SupplierService supplierServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierMockMvc;

    private Supplier supplier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplier createEntity(EntityManager em) {
        Supplier supplier = new Supplier().name(DEFAULT_NAME).email(DEFAULT_EMAIL).phone(DEFAULT_PHONE).mobilePone(DEFAULT_MOBILE_PONE);
        return supplier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplier createUpdatedEntity(EntityManager em) {
        Supplier supplier = new Supplier().name(UPDATED_NAME).email(UPDATED_EMAIL).phone(UPDATED_PHONE).mobilePone(UPDATED_MOBILE_PONE);
        return supplier;
    }

    @BeforeEach
    public void initTest() {
        supplier = createEntity(em);
    }

    @Test
    @Transactional
    void createSupplier() throws Exception {
        int databaseSizeBeforeCreate = supplierRepository.findAll().size();
        // Create the Supplier
        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isCreated());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeCreate + 1);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSupplier.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSupplier.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSupplier.getMobilePone()).isEqualTo(DEFAULT_MOBILE_PONE);
    }

    @Test
    @Transactional
    void createSupplierWithExistingId() throws Exception {
        // Create the Supplier with an existing ID
        supplier.setId(1L);

        int databaseSizeBeforeCreate = supplierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSuppliers() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].mobilePone").value(hasItem(DEFAULT_MOBILE_PONE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSuppliersWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSuppliersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get the supplier
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL_ID, supplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplier.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.mobilePone").value(DEFAULT_MOBILE_PONE));
    }

    @Test
    @Transactional
    void getNonExistingSupplier() throws Exception {
        // Get the supplier
        restSupplierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Update the supplier
        Supplier updatedSupplier = supplierRepository.findById(supplier.getId()).get();
        // Disconnect from session so that the updates on updatedSupplier are not directly saved in db
        em.detach(updatedSupplier);
        updatedSupplier.name(UPDATED_NAME).email(UPDATED_EMAIL).phone(UPDATED_PHONE).mobilePone(UPDATED_MOBILE_PONE);

        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSupplier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSupplier))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupplier.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSupplier.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSupplier.getMobilePone()).isEqualTo(UPDATED_MOBILE_PONE);
    }

    @Test
    @Transactional
    void putNonExistingSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierWithPatch() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Update the supplier using partial update
        Supplier partialUpdatedSupplier = new Supplier();
        partialUpdatedSupplier.setId(supplier.getId());

        partialUpdatedSupplier.email(UPDATED_EMAIL).mobilePone(UPDATED_MOBILE_PONE);

        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSupplier))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSupplier.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSupplier.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSupplier.getMobilePone()).isEqualTo(UPDATED_MOBILE_PONE);
    }

    @Test
    @Transactional
    void fullUpdateSupplierWithPatch() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Update the supplier using partial update
        Supplier partialUpdatedSupplier = new Supplier();
        partialUpdatedSupplier.setId(supplier.getId());

        partialUpdatedSupplier.name(UPDATED_NAME).email(UPDATED_EMAIL).phone(UPDATED_PHONE).mobilePone(UPDATED_MOBILE_PONE);

        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSupplier))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupplier.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSupplier.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSupplier.getMobilePone()).isEqualTo(UPDATED_MOBILE_PONE);
    }

    @Test
    @Transactional
    void patchNonExistingSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();
        supplier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(supplier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeDelete = supplierRepository.findAll().size();

        // Delete the supplier
        restSupplierMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
