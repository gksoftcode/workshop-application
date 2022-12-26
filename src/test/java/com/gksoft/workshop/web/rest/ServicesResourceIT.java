package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.Services;
import com.gksoft.workshop.domain.enumeration.DiscountType;
import com.gksoft.workshop.repository.ServicesRepository;
import com.gksoft.workshop.service.ServicesService;
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
 * Integration tests for the {@link ServicesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServicesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_COST = 1L;
    private static final Long UPDATED_COST = 2L;

    private static final Long DEFAULT_PRICE = 1L;
    private static final Long UPDATED_PRICE = 2L;

    private static final Long DEFAULT_DISCOUNT = 1L;
    private static final Long UPDATED_DISCOUNT = 2L;

    private static final Long DEFAULT_NOTES = 1L;
    private static final Long UPDATED_NOTES = 2L;

    private static final DiscountType DEFAULT_DISCOUNT_TYPE = DiscountType.PERCENTAGE;
    private static final DiscountType UPDATED_DISCOUNT_TYPE = DiscountType.AMOUNT;

    private static final String ENTITY_API_URL = "/api/services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServicesRepository servicesRepository;

    @Mock
    private ServicesRepository servicesRepositoryMock;

    @Mock
    private ServicesService servicesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServicesMockMvc;

    private Services services;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Services createEntity(EntityManager em) {
        Services services = new Services()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .cost(DEFAULT_COST)
            .price(DEFAULT_PRICE)
            .discount(DEFAULT_DISCOUNT)
            .notes(DEFAULT_NOTES)
            .discountType(DEFAULT_DISCOUNT_TYPE);
        return services;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Services createUpdatedEntity(EntityManager em) {
        Services services = new Services()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST)
            .price(UPDATED_PRICE)
            .discount(UPDATED_DISCOUNT)
            .notes(UPDATED_NOTES)
            .discountType(UPDATED_DISCOUNT_TYPE);
        return services;
    }

    @BeforeEach
    public void initTest() {
        services = createEntity(em);
    }

    @Test
    @Transactional
    void createServices() throws Exception {
        int databaseSizeBeforeCreate = servicesRepository.findAll().size();
        // Create the Services
        restServicesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(services)))
            .andExpect(status().isCreated());

        // Validate the Services in the database
        List<Services> servicesList = servicesRepository.findAll();
        assertThat(servicesList).hasSize(databaseSizeBeforeCreate + 1);
        Services testServices = servicesList.get(servicesList.size() - 1);
        assertThat(testServices.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testServices.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testServices.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testServices.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testServices.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testServices.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testServices.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void createServicesWithExistingId() throws Exception {
        // Create the Services with an existing ID
        services.setId(1L);

        int databaseSizeBeforeCreate = servicesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(services)))
            .andExpect(status().isBadRequest());

        // Validate the Services in the database
        List<Services> servicesList = servicesRepository.findAll();
        assertThat(servicesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllServices() throws Exception {
        // Initialize the database
        servicesRepository.saveAndFlush(services);

        // Get all the servicesList
        restServicesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(services.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.intValue())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServicesWithEagerRelationshipsIsEnabled() throws Exception {
        when(servicesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(servicesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServicesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(servicesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(servicesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getServices() throws Exception {
        // Initialize the database
        servicesRepository.saveAndFlush(services);

        // Get the services
        restServicesMockMvc
            .perform(get(ENTITY_API_URL_ID, services.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(services.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.intValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.intValue()))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingServices() throws Exception {
        // Get the services
        restServicesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServices() throws Exception {
        // Initialize the database
        servicesRepository.saveAndFlush(services);

        int databaseSizeBeforeUpdate = servicesRepository.findAll().size();

        // Update the services
        Services updatedServices = servicesRepository.findById(services.getId()).get();
        // Disconnect from session so that the updates on updatedServices are not directly saved in db
        em.detach(updatedServices);
        updatedServices
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST)
            .price(UPDATED_PRICE)
            .discount(UPDATED_DISCOUNT)
            .notes(UPDATED_NOTES)
            .discountType(UPDATED_DISCOUNT_TYPE);

        restServicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServices.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedServices))
            )
            .andExpect(status().isOk());

        // Validate the Services in the database
        List<Services> servicesList = servicesRepository.findAll();
        assertThat(servicesList).hasSize(databaseSizeBeforeUpdate);
        Services testServices = servicesList.get(servicesList.size() - 1);
        assertThat(testServices.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testServices.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testServices.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testServices.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testServices.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testServices.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testServices.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingServices() throws Exception {
        int databaseSizeBeforeUpdate = servicesRepository.findAll().size();
        services.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, services.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(services))
            )
            .andExpect(status().isBadRequest());

        // Validate the Services in the database
        List<Services> servicesList = servicesRepository.findAll();
        assertThat(servicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServices() throws Exception {
        int databaseSizeBeforeUpdate = servicesRepository.findAll().size();
        services.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(services))
            )
            .andExpect(status().isBadRequest());

        // Validate the Services in the database
        List<Services> servicesList = servicesRepository.findAll();
        assertThat(servicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServices() throws Exception {
        int databaseSizeBeforeUpdate = servicesRepository.findAll().size();
        services.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(services)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Services in the database
        List<Services> servicesList = servicesRepository.findAll();
        assertThat(servicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServicesWithPatch() throws Exception {
        // Initialize the database
        servicesRepository.saveAndFlush(services);

        int databaseSizeBeforeUpdate = servicesRepository.findAll().size();

        // Update the services using partial update
        Services partialUpdatedServices = new Services();
        partialUpdatedServices.setId(services.getId());

        partialUpdatedServices.price(UPDATED_PRICE).discount(UPDATED_DISCOUNT).discountType(UPDATED_DISCOUNT_TYPE);

        restServicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServices.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServices))
            )
            .andExpect(status().isOk());

        // Validate the Services in the database
        List<Services> servicesList = servicesRepository.findAll();
        assertThat(servicesList).hasSize(databaseSizeBeforeUpdate);
        Services testServices = servicesList.get(servicesList.size() - 1);
        assertThat(testServices.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testServices.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testServices.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testServices.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testServices.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testServices.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testServices.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateServicesWithPatch() throws Exception {
        // Initialize the database
        servicesRepository.saveAndFlush(services);

        int databaseSizeBeforeUpdate = servicesRepository.findAll().size();

        // Update the services using partial update
        Services partialUpdatedServices = new Services();
        partialUpdatedServices.setId(services.getId());

        partialUpdatedServices
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .cost(UPDATED_COST)
            .price(UPDATED_PRICE)
            .discount(UPDATED_DISCOUNT)
            .notes(UPDATED_NOTES)
            .discountType(UPDATED_DISCOUNT_TYPE);

        restServicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServices.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServices))
            )
            .andExpect(status().isOk());

        // Validate the Services in the database
        List<Services> servicesList = servicesRepository.findAll();
        assertThat(servicesList).hasSize(databaseSizeBeforeUpdate);
        Services testServices = servicesList.get(servicesList.size() - 1);
        assertThat(testServices.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testServices.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testServices.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testServices.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testServices.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testServices.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testServices.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingServices() throws Exception {
        int databaseSizeBeforeUpdate = servicesRepository.findAll().size();
        services.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, services.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(services))
            )
            .andExpect(status().isBadRequest());

        // Validate the Services in the database
        List<Services> servicesList = servicesRepository.findAll();
        assertThat(servicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServices() throws Exception {
        int databaseSizeBeforeUpdate = servicesRepository.findAll().size();
        services.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(services))
            )
            .andExpect(status().isBadRequest());

        // Validate the Services in the database
        List<Services> servicesList = servicesRepository.findAll();
        assertThat(servicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServices() throws Exception {
        int databaseSizeBeforeUpdate = servicesRepository.findAll().size();
        services.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(services)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Services in the database
        List<Services> servicesList = servicesRepository.findAll();
        assertThat(servicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServices() throws Exception {
        // Initialize the database
        servicesRepository.saveAndFlush(services);

        int databaseSizeBeforeDelete = servicesRepository.findAll().size();

        // Delete the services
        restServicesMockMvc
            .perform(delete(ENTITY_API_URL_ID, services.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Services> servicesList = servicesRepository.findAll();
        assertThat(servicesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
