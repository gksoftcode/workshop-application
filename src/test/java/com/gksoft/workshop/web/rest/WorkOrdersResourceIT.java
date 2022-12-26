package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.WorkOrders;
import com.gksoft.workshop.repository.WorkOrdersRepository;
import com.gksoft.workshop.service.WorkOrdersService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link WorkOrdersResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkOrdersResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_BUDGET = 1L;
    private static final Long UPDATED_BUDGET = 2L;

    private static final String DEFAULT_ITEM_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_SERIAL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_WARANTY = false;
    private static final Boolean UPDATED_IS_WARANTY = true;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/work-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkOrdersRepository workOrdersRepository;

    @Mock
    private WorkOrdersRepository workOrdersRepositoryMock;

    @Mock
    private WorkOrdersService workOrdersServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkOrdersMockMvc;

    private WorkOrders workOrders;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkOrders createEntity(EntityManager em) {
        WorkOrders workOrders = new WorkOrders()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .budget(DEFAULT_BUDGET)
            .itemSerial(DEFAULT_ITEM_SERIAL)
            .isWaranty(DEFAULT_IS_WARANTY)
            .notes(DEFAULT_NOTES);
        return workOrders;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkOrders createUpdatedEntity(EntityManager em) {
        WorkOrders workOrders = new WorkOrders()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .budget(UPDATED_BUDGET)
            .itemSerial(UPDATED_ITEM_SERIAL)
            .isWaranty(UPDATED_IS_WARANTY)
            .notes(UPDATED_NOTES);
        return workOrders;
    }

    @BeforeEach
    public void initTest() {
        workOrders = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkOrders() throws Exception {
        int databaseSizeBeforeCreate = workOrdersRepository.findAll().size();
        // Create the WorkOrders
        restWorkOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workOrders)))
            .andExpect(status().isCreated());

        // Validate the WorkOrders in the database
        List<WorkOrders> workOrdersList = workOrdersRepository.findAll();
        assertThat(workOrdersList).hasSize(databaseSizeBeforeCreate + 1);
        WorkOrders testWorkOrders = workOrdersList.get(workOrdersList.size() - 1);
        assertThat(testWorkOrders.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkOrders.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkOrders.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testWorkOrders.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testWorkOrders.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testWorkOrders.getItemSerial()).isEqualTo(DEFAULT_ITEM_SERIAL);
        assertThat(testWorkOrders.getIsWaranty()).isEqualTo(DEFAULT_IS_WARANTY);
        assertThat(testWorkOrders.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createWorkOrdersWithExistingId() throws Exception {
        // Create the WorkOrders with an existing ID
        workOrders.setId(1L);

        int databaseSizeBeforeCreate = workOrdersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workOrders)))
            .andExpect(status().isBadRequest());

        // Validate the WorkOrders in the database
        List<WorkOrders> workOrdersList = workOrdersRepository.findAll();
        assertThat(workOrdersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWorkOrders() throws Exception {
        // Initialize the database
        workOrdersRepository.saveAndFlush(workOrders);

        // Get all the workOrdersList
        restWorkOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workOrders.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.intValue())))
            .andExpect(jsonPath("$.[*].itemSerial").value(hasItem(DEFAULT_ITEM_SERIAL)))
            .andExpect(jsonPath("$.[*].isWaranty").value(hasItem(DEFAULT_IS_WARANTY.booleanValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(workOrdersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkOrdersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workOrdersServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(workOrdersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkOrdersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(workOrdersRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWorkOrders() throws Exception {
        // Initialize the database
        workOrdersRepository.saveAndFlush(workOrders);

        // Get the workOrders
        restWorkOrdersMockMvc
            .perform(get(ENTITY_API_URL_ID, workOrders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workOrders.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET.intValue()))
            .andExpect(jsonPath("$.itemSerial").value(DEFAULT_ITEM_SERIAL))
            .andExpect(jsonPath("$.isWaranty").value(DEFAULT_IS_WARANTY.booleanValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingWorkOrders() throws Exception {
        // Get the workOrders
        restWorkOrdersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkOrders() throws Exception {
        // Initialize the database
        workOrdersRepository.saveAndFlush(workOrders);

        int databaseSizeBeforeUpdate = workOrdersRepository.findAll().size();

        // Update the workOrders
        WorkOrders updatedWorkOrders = workOrdersRepository.findById(workOrders.getId()).get();
        // Disconnect from session so that the updates on updatedWorkOrders are not directly saved in db
        em.detach(updatedWorkOrders);
        updatedWorkOrders
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .budget(UPDATED_BUDGET)
            .itemSerial(UPDATED_ITEM_SERIAL)
            .isWaranty(UPDATED_IS_WARANTY)
            .notes(UPDATED_NOTES);

        restWorkOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkOrders.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkOrders))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrders in the database
        List<WorkOrders> workOrdersList = workOrdersRepository.findAll();
        assertThat(workOrdersList).hasSize(databaseSizeBeforeUpdate);
        WorkOrders testWorkOrders = workOrdersList.get(workOrdersList.size() - 1);
        assertThat(testWorkOrders.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkOrders.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkOrders.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testWorkOrders.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testWorkOrders.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testWorkOrders.getItemSerial()).isEqualTo(UPDATED_ITEM_SERIAL);
        assertThat(testWorkOrders.getIsWaranty()).isEqualTo(UPDATED_IS_WARANTY);
        assertThat(testWorkOrders.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingWorkOrders() throws Exception {
        int databaseSizeBeforeUpdate = workOrdersRepository.findAll().size();
        workOrders.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workOrders.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrders))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrders in the database
        List<WorkOrders> workOrdersList = workOrdersRepository.findAll();
        assertThat(workOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkOrders() throws Exception {
        int databaseSizeBeforeUpdate = workOrdersRepository.findAll().size();
        workOrders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workOrders))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrders in the database
        List<WorkOrders> workOrdersList = workOrdersRepository.findAll();
        assertThat(workOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkOrders() throws Exception {
        int databaseSizeBeforeUpdate = workOrdersRepository.findAll().size();
        workOrders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrdersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workOrders)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkOrders in the database
        List<WorkOrders> workOrdersList = workOrdersRepository.findAll();
        assertThat(workOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkOrdersWithPatch() throws Exception {
        // Initialize the database
        workOrdersRepository.saveAndFlush(workOrders);

        int databaseSizeBeforeUpdate = workOrdersRepository.findAll().size();

        // Update the workOrders using partial update
        WorkOrders partialUpdatedWorkOrders = new WorkOrders();
        partialUpdatedWorkOrders.setId(workOrders.getId());

        partialUpdatedWorkOrders.endDate(UPDATED_END_DATE).budget(UPDATED_BUDGET);

        restWorkOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkOrders))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrders in the database
        List<WorkOrders> workOrdersList = workOrdersRepository.findAll();
        assertThat(workOrdersList).hasSize(databaseSizeBeforeUpdate);
        WorkOrders testWorkOrders = workOrdersList.get(workOrdersList.size() - 1);
        assertThat(testWorkOrders.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkOrders.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkOrders.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testWorkOrders.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testWorkOrders.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testWorkOrders.getItemSerial()).isEqualTo(DEFAULT_ITEM_SERIAL);
        assertThat(testWorkOrders.getIsWaranty()).isEqualTo(DEFAULT_IS_WARANTY);
        assertThat(testWorkOrders.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateWorkOrdersWithPatch() throws Exception {
        // Initialize the database
        workOrdersRepository.saveAndFlush(workOrders);

        int databaseSizeBeforeUpdate = workOrdersRepository.findAll().size();

        // Update the workOrders using partial update
        WorkOrders partialUpdatedWorkOrders = new WorkOrders();
        partialUpdatedWorkOrders.setId(workOrders.getId());

        partialUpdatedWorkOrders
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .budget(UPDATED_BUDGET)
            .itemSerial(UPDATED_ITEM_SERIAL)
            .isWaranty(UPDATED_IS_WARANTY)
            .notes(UPDATED_NOTES);

        restWorkOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkOrders))
            )
            .andExpect(status().isOk());

        // Validate the WorkOrders in the database
        List<WorkOrders> workOrdersList = workOrdersRepository.findAll();
        assertThat(workOrdersList).hasSize(databaseSizeBeforeUpdate);
        WorkOrders testWorkOrders = workOrdersList.get(workOrdersList.size() - 1);
        assertThat(testWorkOrders.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkOrders.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkOrders.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testWorkOrders.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testWorkOrders.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testWorkOrders.getItemSerial()).isEqualTo(UPDATED_ITEM_SERIAL);
        assertThat(testWorkOrders.getIsWaranty()).isEqualTo(UPDATED_IS_WARANTY);
        assertThat(testWorkOrders.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingWorkOrders() throws Exception {
        int databaseSizeBeforeUpdate = workOrdersRepository.findAll().size();
        workOrders.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workOrders))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrders in the database
        List<WorkOrders> workOrdersList = workOrdersRepository.findAll();
        assertThat(workOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkOrders() throws Exception {
        int databaseSizeBeforeUpdate = workOrdersRepository.findAll().size();
        workOrders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workOrders))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkOrders in the database
        List<WorkOrders> workOrdersList = workOrdersRepository.findAll();
        assertThat(workOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkOrders() throws Exception {
        int databaseSizeBeforeUpdate = workOrdersRepository.findAll().size();
        workOrders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(workOrders))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkOrders in the database
        List<WorkOrders> workOrdersList = workOrdersRepository.findAll();
        assertThat(workOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkOrders() throws Exception {
        // Initialize the database
        workOrdersRepository.saveAndFlush(workOrders);

        int databaseSizeBeforeDelete = workOrdersRepository.findAll().size();

        // Delete the workOrders
        restWorkOrdersMockMvc
            .perform(delete(ENTITY_API_URL_ID, workOrders.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkOrders> workOrdersList = workOrdersRepository.findAll();
        assertThat(workOrdersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
