package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.Appintment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Appintment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppintmentRepository extends JpaRepository<Appintment, Long> {}
