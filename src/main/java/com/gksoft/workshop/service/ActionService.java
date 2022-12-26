package com.gksoft.workshop.service;

import com.gksoft.workshop.domain.Action;
import com.gksoft.workshop.repository.ActionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Action}.
 */
@Service
@Transactional
public class ActionService {

    private final Logger log = LoggerFactory.getLogger(ActionService.class);

    private final ActionRepository actionRepository;

    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    /**
     * Save a action.
     *
     * @param action the entity to save.
     * @return the persisted entity.
     */
    public Action save(Action action) {
        log.debug("Request to save Action : {}", action);
        return actionRepository.save(action);
    }

    /**
     * Update a action.
     *
     * @param action the entity to save.
     * @return the persisted entity.
     */
    public Action update(Action action) {
        log.debug("Request to update Action : {}", action);
        return actionRepository.save(action);
    }

    /**
     * Partially update a action.
     *
     * @param action the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Action> partialUpdate(Action action) {
        log.debug("Request to partially update Action : {}", action);

        return actionRepository
            .findById(action.getId())
            .map(existingAction -> {
                if (action.getName() != null) {
                    existingAction.setName(action.getName());
                }

                return existingAction;
            })
            .map(actionRepository::save);
    }

    /**
     * Get all the actions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Action> findAll(Pageable pageable) {
        log.debug("Request to get all Actions");
        return actionRepository.findAll(pageable);
    }

    /**
     * Get one action by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Action> findOne(Long id) {
        log.debug("Request to get Action : {}", id);
        return actionRepository.findById(id);
    }

    /**
     * Delete the action by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Action : {}", id);
        actionRepository.deleteById(id);
    }
}
