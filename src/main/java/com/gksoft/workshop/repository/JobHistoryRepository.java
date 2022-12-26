package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.JobHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the JobHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {}
