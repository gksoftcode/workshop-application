package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.InvoiceDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InvoiceDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetails, Long> {}
