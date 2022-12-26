package com.gksoft.workshop.service;

import com.gksoft.workshop.domain.Appintment;
import com.gksoft.workshop.repository.AppintmentRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Appintment}.
 */
@Service
@Transactional
public class AppintmentService {

    private final Logger log = LoggerFactory.getLogger(AppintmentService.class);

    private final AppintmentRepository appintmentRepository;

    public AppintmentService(AppintmentRepository appintmentRepository) {
        this.appintmentRepository = appintmentRepository;
    }

    /**
     * Save a appintment.
     *
     * @param appintment the entity to save.
     * @return the persisted entity.
     */
    public Appintment save(Appintment appintment) {
        log.debug("Request to save Appintment : {}", appintment);
        return appintmentRepository.save(appintment);
    }

    /**
     * Update a appintment.
     *
     * @param appintment the entity to save.
     * @return the persisted entity.
     */
    public Appintment update(Appintment appintment) {
        log.debug("Request to update Appintment : {}", appintment);
        return appintmentRepository.save(appintment);
    }

    /**
     * Partially update a appintment.
     *
     * @param appintment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Appintment> partialUpdate(Appintment appintment) {
        log.debug("Request to partially update Appintment : {}", appintment);

        return appintmentRepository
            .findById(appintment.getId())
            .map(existingAppintment -> {
                if (appintment.getAppDate() != null) {
                    existingAppintment.setAppDate(appintment.getAppDate());
                }
                if (appintment.getNotes() != null) {
                    existingAppintment.setNotes(appintment.getNotes());
                }

                return existingAppintment;
            })
            .map(appintmentRepository::save);
    }

    /**
     * Get all the appintments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Appintment> findAll(Pageable pageable) {
        log.debug("Request to get all Appintments");
        return appintmentRepository.findAll(pageable);
    }

    /**
     * Get one appintment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Appintment> findOne(Long id) {
        log.debug("Request to get Appintment : {}", id);
        return appintmentRepository.findById(id);
    }

    /**
     * Delete the appintment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Appintment : {}", id);
        appintmentRepository.deleteById(id);
    }
}
