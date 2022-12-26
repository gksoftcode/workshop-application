package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.ServiceCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {}
