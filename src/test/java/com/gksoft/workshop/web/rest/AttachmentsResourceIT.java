package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.Attachments;
import com.gksoft.workshop.repository.AttachmentsRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AttachmentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttachmentsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ATTACH = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ATTACH = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ATTACH_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ATTACH_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttachmentsRepository attachmentsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttachmentsMockMvc;

    private Attachments attachments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attachments createEntity(EntityManager em) {
        Attachments attachments = new Attachments()
            .name(DEFAULT_NAME)
            .attach(DEFAULT_ATTACH)
            .attachContentType(DEFAULT_ATTACH_CONTENT_TYPE);
        return attachments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attachments createUpdatedEntity(EntityManager em) {
        Attachments attachments = new Attachments()
            .name(UPDATED_NAME)
            .attach(UPDATED_ATTACH)
            .attachContentType(UPDATED_ATTACH_CONTENT_TYPE);
        return attachments;
    }

    @BeforeEach
    public void initTest() {
        attachments = createEntity(em);
    }

    @Test
    @Transactional
    void createAttachments() throws Exception {
        int databaseSizeBeforeCreate = attachmentsRepository.findAll().size();
        // Create the Attachments
        restAttachmentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachments)))
            .andExpect(status().isCreated());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeCreate + 1);
        Attachments testAttachments = attachmentsList.get(attachmentsList.size() - 1);
        assertThat(testAttachments.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttachments.getAttach()).isEqualTo(DEFAULT_ATTACH);
        assertThat(testAttachments.getAttachContentType()).isEqualTo(DEFAULT_ATTACH_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createAttachmentsWithExistingId() throws Exception {
        // Create the Attachments with an existing ID
        attachments.setId(1L);

        int databaseSizeBeforeCreate = attachmentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachmentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachments)))
            .andExpect(status().isBadRequest());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAttachments() throws Exception {
        // Initialize the database
        attachmentsRepository.saveAndFlush(attachments);

        // Get all the attachmentsList
        restAttachmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachments.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].attachContentType").value(hasItem(DEFAULT_ATTACH_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attach").value(hasItem(Base64Utils.encodeToString(DEFAULT_ATTACH))));
    }

    @Test
    @Transactional
    void getAttachments() throws Exception {
        // Initialize the database
        attachmentsRepository.saveAndFlush(attachments);

        // Get the attachments
        restAttachmentsMockMvc
            .perform(get(ENTITY_API_URL_ID, attachments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attachments.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.attachContentType").value(DEFAULT_ATTACH_CONTENT_TYPE))
            .andExpect(jsonPath("$.attach").value(Base64Utils.encodeToString(DEFAULT_ATTACH)));
    }

    @Test
    @Transactional
    void getNonExistingAttachments() throws Exception {
        // Get the attachments
        restAttachmentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttachments() throws Exception {
        // Initialize the database
        attachmentsRepository.saveAndFlush(attachments);

        int databaseSizeBeforeUpdate = attachmentsRepository.findAll().size();

        // Update the attachments
        Attachments updatedAttachments = attachmentsRepository.findById(attachments.getId()).get();
        // Disconnect from session so that the updates on updatedAttachments are not directly saved in db
        em.detach(updatedAttachments);
        updatedAttachments.name(UPDATED_NAME).attach(UPDATED_ATTACH).attachContentType(UPDATED_ATTACH_CONTENT_TYPE);

        restAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttachments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAttachments))
            )
            .andExpect(status().isOk());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeUpdate);
        Attachments testAttachments = attachmentsList.get(attachmentsList.size() - 1);
        assertThat(testAttachments.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttachments.getAttach()).isEqualTo(UPDATED_ATTACH);
        assertThat(testAttachments.getAttachContentType()).isEqualTo(UPDATED_ATTACH_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAttachments() throws Exception {
        int databaseSizeBeforeUpdate = attachmentsRepository.findAll().size();
        attachments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attachments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttachments() throws Exception {
        int databaseSizeBeforeUpdate = attachmentsRepository.findAll().size();
        attachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttachments() throws Exception {
        int databaseSizeBeforeUpdate = attachmentsRepository.findAll().size();
        attachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachments)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttachmentsWithPatch() throws Exception {
        // Initialize the database
        attachmentsRepository.saveAndFlush(attachments);

        int databaseSizeBeforeUpdate = attachmentsRepository.findAll().size();

        // Update the attachments using partial update
        Attachments partialUpdatedAttachments = new Attachments();
        partialUpdatedAttachments.setId(attachments.getId());

        restAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachments))
            )
            .andExpect(status().isOk());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeUpdate);
        Attachments testAttachments = attachmentsList.get(attachmentsList.size() - 1);
        assertThat(testAttachments.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttachments.getAttach()).isEqualTo(DEFAULT_ATTACH);
        assertThat(testAttachments.getAttachContentType()).isEqualTo(DEFAULT_ATTACH_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAttachmentsWithPatch() throws Exception {
        // Initialize the database
        attachmentsRepository.saveAndFlush(attachments);

        int databaseSizeBeforeUpdate = attachmentsRepository.findAll().size();

        // Update the attachments using partial update
        Attachments partialUpdatedAttachments = new Attachments();
        partialUpdatedAttachments.setId(attachments.getId());

        partialUpdatedAttachments.name(UPDATED_NAME).attach(UPDATED_ATTACH).attachContentType(UPDATED_ATTACH_CONTENT_TYPE);

        restAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachments))
            )
            .andExpect(status().isOk());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeUpdate);
        Attachments testAttachments = attachmentsList.get(attachmentsList.size() - 1);
        assertThat(testAttachments.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttachments.getAttach()).isEqualTo(UPDATED_ATTACH);
        assertThat(testAttachments.getAttachContentType()).isEqualTo(UPDATED_ATTACH_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAttachments() throws Exception {
        int databaseSizeBeforeUpdate = attachmentsRepository.findAll().size();
        attachments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attachments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttachments() throws Exception {
        int databaseSizeBeforeUpdate = attachmentsRepository.findAll().size();
        attachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachments))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttachments() throws Exception {
        int databaseSizeBeforeUpdate = attachmentsRepository.findAll().size();
        attachments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(attachments))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attachments in the database
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttachments() throws Exception {
        // Initialize the database
        attachmentsRepository.saveAndFlush(attachments);

        int databaseSizeBeforeDelete = attachmentsRepository.findAll().size();

        // Delete the attachments
        restAttachmentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, attachments.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Attachments> attachmentsList = attachmentsRepository.findAll();
        assertThat(attachmentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
