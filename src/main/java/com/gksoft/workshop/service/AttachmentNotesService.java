package com.gksoft.workshop.service;

import com.gksoft.workshop.domain.AttachmentNotes;
import com.gksoft.workshop.repository.AttachmentNotesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AttachmentNotes}.
 */
@Service
@Transactional
public class AttachmentNotesService {

    private final Logger log = LoggerFactory.getLogger(AttachmentNotesService.class);

    private final AttachmentNotesRepository attachmentNotesRepository;

    public AttachmentNotesService(AttachmentNotesRepository attachmentNotesRepository) {
        this.attachmentNotesRepository = attachmentNotesRepository;
    }

    /**
     * Save a attachmentNotes.
     *
     * @param attachmentNotes the entity to save.
     * @return the persisted entity.
     */
    public AttachmentNotes save(AttachmentNotes attachmentNotes) {
        log.debug("Request to save AttachmentNotes : {}", attachmentNotes);
        return attachmentNotesRepository.save(attachmentNotes);
    }

    /**
     * Update a attachmentNotes.
     *
     * @param attachmentNotes the entity to save.
     * @return the persisted entity.
     */
    public AttachmentNotes update(AttachmentNotes attachmentNotes) {
        log.debug("Request to update AttachmentNotes : {}", attachmentNotes);
        return attachmentNotesRepository.save(attachmentNotes);
    }

    /**
     * Partially update a attachmentNotes.
     *
     * @param attachmentNotes the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AttachmentNotes> partialUpdate(AttachmentNotes attachmentNotes) {
        log.debug("Request to partially update AttachmentNotes : {}", attachmentNotes);

        return attachmentNotesRepository
            .findById(attachmentNotes.getId())
            .map(existingAttachmentNotes -> {
                if (attachmentNotes.getIsShared() != null) {
                    existingAttachmentNotes.setIsShared(attachmentNotes.getIsShared());
                }
                if (attachmentNotes.getActionDate() != null) {
                    existingAttachmentNotes.setActionDate(attachmentNotes.getActionDate());
                }
                if (attachmentNotes.getNote() != null) {
                    existingAttachmentNotes.setNote(attachmentNotes.getNote());
                }

                return existingAttachmentNotes;
            })
            .map(attachmentNotesRepository::save);
    }

    /**
     * Get all the attachmentNotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AttachmentNotes> findAll(Pageable pageable) {
        log.debug("Request to get all AttachmentNotes");
        return attachmentNotesRepository.findAll(pageable);
    }

    /**
     * Get all the attachmentNotes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AttachmentNotes> findAllWithEagerRelationships(Pageable pageable) {
        return attachmentNotesRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one attachmentNotes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AttachmentNotes> findOne(Long id) {
        log.debug("Request to get AttachmentNotes : {}", id);
        return attachmentNotesRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the attachmentNotes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AttachmentNotes : {}", id);
        attachmentNotesRepository.deleteById(id);
    }
}
