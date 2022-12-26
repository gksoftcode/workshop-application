package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.Invoice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface InvoiceRepositoryWithBagRelationships {
    Optional<Invoice> fetchBagRelationships(Optional<Invoice> invoice);

    List<Invoice> fetchBagRelationships(List<Invoice> invoices);

    Page<Invoice> fetchBagRelationships(Page<Invoice> invoices);
}
