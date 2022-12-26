package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.AttachmentNotes;
import com.gksoft.workshop.repository.AttachmentNotesRepository;
import com.gksoft.workshop.service.AttachmentNotesService;
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
 * Integration tests for the {@link AttachmentNotesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AttachmentNotesResourceIT {

    private static final Boolean DEFAULT_IS_SHARED = false;
    private static final Boolean UPDATED_IS_SHARED = true;

    private static final Instant DEFAULT_ACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attachment-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttachmentNotesRepository attachmentNotesRepository;

    @Mock
    private AttachmentNotesRepository attachmentNotesRepositoryMock;

    @Mock
    private AttachmentNotesService attachmentNotesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttachmentNotesMockMvc;

    private AttachmentNotes attachmentNotes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttachmentNotes createEntity(EntityManager em) {
        AttachmentNotes attachmentNotes = new AttachmentNotes()
            .isShared(DEFAULT_IS_SHARED)
            .actionDate(DEFAULT_ACTION_DATE)
            .note(DEFAULT_NOTE);
        return attachmentNotes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttachmentNotes createUpdatedEntity(EntityManager em) {
        AttachmentNotes attachmentNotes = new AttachmentNotes()
            .isShared(UPDATED_IS_SHARED)
            .actionDate(UPDATED_ACTION_DATE)
            .note(UPDATED_NOTE);
        return attachmentNotes;
    }

    @BeforeEach
    public void initTest() {
        attachmentNotes = createEntity(em);
    }

    @Test
    @Transactional
    void createAttachmentNotes() throws Exception {
        int databaseSizeBeforeCreate = attachmentNotesRepository.findAll().size();
        // Create the AttachmentNotes
        restAttachmentNotesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachmentNotes))
            )
            .andExpect(status().isCreated());

        // Validate the AttachmentNotes in the database
        List<AttachmentNotes> attachmentNotesList = attachmentNotesRepository.findAll();
        assertThat(attachmentNotesList).hasSize(databaseSizeBeforeCreate + 1);
        AttachmentNotes testAttachmentNotes = attachmentNotesList.get(attachmentNotesList.size() - 1);
        assertThat(testAttachmentNotes.getIsShared()).isEqualTo(DEFAULT_IS_SHARED);
        assertThat(testAttachmentNotes.getActionDate()).isEqualTo(DEFAULT_ACTION_DATE);
        assertThat(testAttachmentNotes.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createAttachmentNotesWithExistingId() throws Exception {
        // Create the AttachmentNotes with an existing ID
        attachmentNotes.setId(1L);

        int databaseSizeBeforeCreate = attachmentNotesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachmentNotesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachmentNotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachmentNotes in the database
        List<AttachmentNotes> attachmentNotesList = attachmentNotesRepository.findAll();
        assertThat(attachmentNotesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAttachmentNotes() throws Exception {
        // Initialize the database
        attachmentNotesRepository.saveAndFlush(attachmentNotes);

        // Get all the attachmentNotesList
        restAttachmentNotesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachmentNotes.getId().intValue())))
            .andExpect(jsonPath("$.[*].isShared").value(hasItem(DEFAULT_IS_SHARED.booleanValue())))
            .andExpect(jsonPath("$.[*].actionDate").value(hasItem(DEFAULT_ACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttachmentNotesWithEagerRelationshipsIsEnabled() throws Exception {
        when(attachmentNotesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAttachmentNotesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(attachmentNotesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttachmentNotesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(attachmentNotesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAttachmentNotesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(attachmentNotesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAttachmentNotes() throws Exception {
        // Initialize the database
        attachmentNotesRepository.saveAndFlush(attachmentNotes);

        // Get the attachmentNotes
        restAttachmentNotesMockMvc
            .perform(get(ENTITY_API_URL_ID, attachmentNotes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attachmentNotes.getId().intValue()))
            .andExpect(jsonPath("$.isShared").value(DEFAULT_IS_SHARED.booleanValue()))
            .andExpect(jsonPath("$.actionDate").value(DEFAULT_ACTION_DATE.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingAttachmentNotes() throws Exception {
        // Get the attachmentNotes
        restAttachmentNotesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttachmentNotes() throws Exception {
        // Initialize the database
        attachmentNotesRepository.saveAndFlush(attachmentNotes);

        int databaseSizeBeforeUpdate = attachmentNotesRepository.findAll().size();

        // Update the attachmentNotes
        AttachmentNotes updatedAttachmentNotes = attachmentNotesRepository.findById(attachmentNotes.getId()).get();
        // Disconnect from session so that the updates on updatedAttachmentNotes are not directly saved in db
        em.detach(updatedAttachmentNotes);
        updatedAttachmentNotes.isShared(UPDATED_IS_SHARED).actionDate(UPDATED_ACTION_DATE).note(UPDATED_NOTE);

        restAttachmentNotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttachmentNotes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAttachmentNotes))
            )
            .andExpect(status().isOk());

        // Validate the AttachmentNotes in the database
        List<AttachmentNotes> attachmentNotesList = attachmentNotesRepository.findAll();
        assertThat(attachmentNotesList).hasSize(databaseSizeBeforeUpdate);
        AttachmentNotes testAttachmentNotes = attachmentNotesList.get(attachmentNotesList.size() - 1);
        assertThat(testAttachmentNotes.getIsShared()).isEqualTo(UPDATED_IS_SHARED);
        assertThat(testAttachmentNotes.getActionDate()).isEqualTo(UPDATED_ACTION_DATE);
        assertThat(testAttachmentNotes.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingAttachmentNotes() throws Exception {
        int databaseSizeBeforeUpdate = attachmentNotesRepository.findAll().size();
        attachmentNotes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentNotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attachmentNotes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentNotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachmentNotes in the database
        List<AttachmentNotes> attachmentNotesList = attachmentNotesRepository.findAll();
        assertThat(attachmentNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttachmentNotes() throws Exception {
        int databaseSizeBeforeUpdate = attachmentNotesRepository.findAll().size();
        attachmentNotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentNotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentNotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachmentNotes in the database
        List<AttachmentNotes> attachmentNotesList = attachmentNotesRepository.findAll();
        assertThat(attachmentNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttachmentNotes() throws Exception {
        int databaseSizeBeforeUpdate = attachmentNotesRepository.findAll().size();
        attachmentNotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentNotesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachmentNotes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttachmentNotes in the database
        List<AttachmentNotes> attachmentNotesList = attachmentNotesRepository.findAll();
        assertThat(attachmentNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttachmentNotesWithPatch() throws Exception {
        // Initialize the database
        attachmentNotesRepository.saveAndFlush(attachmentNotes);

        int databaseSizeBeforeUpdate = attachmentNotesRepository.findAll().size();

        // Update the attachmentNotes using partial update
        AttachmentNotes partialUpdatedAttachmentNotes = new AttachmentNotes();
        partialUpdatedAttachmentNotes.setId(attachmentNotes.getId());

        partialUpdatedAttachmentNotes.actionDate(UPDATED_ACTION_DATE).note(UPDATED_NOTE);

        restAttachmentNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachmentNotes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachmentNotes))
            )
            .andExpect(status().isOk());

        // Validate the AttachmentNotes in the database
        List<AttachmentNotes> attachmentNotesList = attachmentNotesRepository.findAll();
        assertThat(attachmentNotesList).hasSize(databaseSizeBeforeUpdate);
        AttachmentNotes testAttachmentNotes = attachmentNotesList.get(attachmentNotesList.size() - 1);
        assertThat(testAttachmentNotes.getIsShared()).isEqualTo(DEFAULT_IS_SHARED);
        assertThat(testAttachmentNotes.getActionDate()).isEqualTo(UPDATED_ACTION_DATE);
        assertThat(testAttachmentNotes.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateAttachmentNotesWithPatch() throws Exception {
        // Initialize the database
        attachmentNotesRepository.saveAndFlush(attachmentNotes);

        int databaseSizeBeforeUpdate = attachmentNotesRepository.findAll().size();

        // Update the attachmentNotes using partial update
        AttachmentNotes partialUpdatedAttachmentNotes = new AttachmentNotes();
        partialUpdatedAttachmentNotes.setId(attachmentNotes.getId());

        partialUpdatedAttachmentNotes.isShared(UPDATED_IS_SHARED).actionDate(UPDATED_ACTION_DATE).note(UPDATED_NOTE);

        restAttachmentNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachmentNotes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachmentNotes))
            )
            .andExpect(status().isOk());

        // Validate the AttachmentNotes in the database
        List<AttachmentNotes> attachmentNotesList = attachmentNotesRepository.findAll();
        assertThat(attachmentNotesList).hasSize(databaseSizeBeforeUpdate);
        AttachmentNotes testAttachmentNotes = attachmentNotesList.get(attachmentNotesList.size() - 1);
        assertThat(testAttachmentNotes.getIsShared()).isEqualTo(UPDATED_IS_SHARED);
        assertThat(testAttachmentNotes.getActionDate()).isEqualTo(UPDATED_ACTION_DATE);
        assertThat(testAttachmentNotes.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingAttachmentNotes() throws Exception {
        int databaseSizeBeforeUpdate = attachmentNotesRepository.findAll().size();
        attachmentNotes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attachmentNotes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachmentNotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachmentNotes in the database
        List<AttachmentNotes> attachmentNotesList = attachmentNotesRepository.findAll();
        assertThat(attachmentNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttachmentNotes() throws Exception {
        int databaseSizeBeforeUpdate = attachmentNotesRepository.findAll().size();
        attachmentNotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachmentNotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachmentNotes in the database
        List<AttachmentNotes> attachmentNotesList = attachmentNotesRepository.findAll();
        assertThat(attachmentNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttachmentNotes() throws Exception {
        int databaseSizeBeforeUpdate = attachmentNotesRepository.findAll().size();
        attachmentNotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentNotesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachmentNotes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttachmentNotes in the database
        List<AttachmentNotes> attachmentNotesList = attachmentNotesRepository.findAll();
        assertThat(attachmentNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttachmentNotes() throws Exception {
        // Initialize the database
        attachmentNotesRepository.saveAndFlush(attachmentNotes);

        int databaseSizeBeforeDelete = attachmentNotesRepository.findAll().size();

        // Delete the attachmentNotes
        restAttachmentNotesMockMvc
            .perform(delete(ENTITY_API_URL_ID, attachmentNotes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttachmentNotes> attachmentNotesList = attachmentNotesRepository.findAll();
        assertThat(attachmentNotesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
