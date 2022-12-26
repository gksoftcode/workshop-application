package com.gksoft.workshop.service;

import com.gksoft.workshop.domain.ItemModels;
import com.gksoft.workshop.repository.ItemModelsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ItemModels}.
 */
@Service
@Transactional
public class ItemModelsService {

    private final Logger log = LoggerFactory.getLogger(ItemModelsService.class);

    private final ItemModelsRepository itemModelsRepository;

    public ItemModelsService(ItemModelsRepository itemModelsRepository) {
        this.itemModelsRepository = itemModelsRepository;
    }

    /**
     * Save a itemModels.
     *
     * @param itemModels the entity to save.
     * @return the persisted entity.
     */
    public ItemModels save(ItemModels itemModels) {
        log.debug("Request to save ItemModels : {}", itemModels);
        return itemModelsRepository.save(itemModels);
    }

    /**
     * Update a itemModels.
     *
     * @param itemModels the entity to save.
     * @return the persisted entity.
     */
    public ItemModels update(ItemModels itemModels) {
        log.debug("Request to update ItemModels : {}", itemModels);
        return itemModelsRepository.save(itemModels);
    }

    /**
     * Partially update a itemModels.
     *
     * @param itemModels the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ItemModels> partialUpdate(ItemModels itemModels) {
        log.debug("Request to partially update ItemModels : {}", itemModels);

        return itemModelsRepository
            .findById(itemModels.getId())
            .map(existingItemModels -> {
                if (itemModels.getName() != null) {
                    existingItemModels.setName(itemModels.getName());
                }

                return existingItemModels;
            })
            .map(itemModelsRepository::save);
    }

    /**
     * Get all the itemModels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemModels> findAll(Pageable pageable) {
        log.debug("Request to get all ItemModels");
        return itemModelsRepository.findAll(pageable);
    }

    /**
     * Get one itemModels by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ItemModels> findOne(Long id) {
        log.debug("Request to get ItemModels : {}", id);
        return itemModelsRepository.findById(id);
    }

    /**
     * Delete the itemModels by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ItemModels : {}", id);
        itemModelsRepository.deleteById(id);
    }
}
