package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.Attachments;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Attachments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachmentsRepository extends JpaRepository<Attachments, Long> {}
