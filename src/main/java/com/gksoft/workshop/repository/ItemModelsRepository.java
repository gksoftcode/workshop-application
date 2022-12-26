package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.ItemModels;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ItemModels entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemModelsRepository extends JpaRepository<ItemModels, Long> {}
