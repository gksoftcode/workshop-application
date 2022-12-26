package com.gksoft.workshop.service;

import com.gksoft.workshop.domain.WorkOrders;
import com.gksoft.workshop.repository.WorkOrdersRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkOrders}.
 */
@Service
@Transactional
public class WorkOrdersService {

    private final Logger log = LoggerFactory.getLogger(WorkOrdersService.class);

    private final WorkOrdersRepository workOrdersRepository;

    public WorkOrdersService(WorkOrdersRepository workOrdersRepository) {
        this.workOrdersRepository = workOrdersRepository;
    }

    /**
     * Save a workOrders.
     *
     * @param workOrders the entity to save.
     * @return the persisted entity.
     */
    public WorkOrders save(WorkOrders workOrders) {
        log.debug("Request to save WorkOrders : {}", workOrders);
        return workOrdersRepository.save(workOrders);
    }

    /**
     * Update a workOrders.
     *
     * @param workOrders the entity to save.
     * @return the persisted entity.
     */
    public WorkOrders update(WorkOrders workOrders) {
        log.debug("Request to update WorkOrders : {}", workOrders);
        return workOrdersRepository.save(workOrders);
    }

    /**
     * Partially update a workOrders.
     *
     * @param workOrders the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WorkOrders> partialUpdate(WorkOrders workOrders) {
        log.debug("Request to partially update WorkOrders : {}", workOrders);

        return workOrdersRepository
            .findById(workOrders.getId())
            .map(existingWorkOrders -> {
                if (workOrders.getTitle() != null) {
                    existingWorkOrders.setTitle(workOrders.getTitle());
                }
                if (workOrders.getDescription() != null) {
                    existingWorkOrders.setDescription(workOrders.getDescription());
                }
                if (workOrders.getStartDate() != null) {
                    existingWorkOrders.setStartDate(workOrders.getStartDate());
                }
                if (workOrders.getEndDate() != null) {
                    existingWorkOrders.setEndDate(workOrders.getEndDate());
                }
                if (workOrders.getBudget() != null) {
                    existingWorkOrders.setBudget(workOrders.getBudget());
                }
                if (workOrders.getItemSerial() != null) {
                    existingWorkOrders.setItemSerial(workOrders.getItemSerial());
                }
                if (workOrders.getIsWaranty() != null) {
                    existingWorkOrders.setIsWaranty(workOrders.getIsWaranty());
                }
                if (workOrders.getNotes() != null) {
                    existingWorkOrders.setNotes(workOrders.getNotes());
                }

                return existingWorkOrders;
            })
            .map(workOrdersRepository::save);
    }

    /**
     * Get all the workOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkOrders> findAll(Pageable pageable) {
        log.debug("Request to get all WorkOrders");
        return workOrdersRepository.findAll(pageable);
    }

    /**
     * Get all the workOrders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<WorkOrders> findAllWithEagerRelationships(Pageable pageable) {
        return workOrdersRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one workOrders by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkOrders> findOne(Long id) {
        log.debug("Request to get WorkOrders : {}", id);
        return workOrdersRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the workOrders by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkOrders : {}", id);
        workOrdersRepository.deleteById(id);
    }
}
