package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.Supplier;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SupplierRepositoryWithBagRelationships {
    Optional<Supplier> fetchBagRelationships(Optional<Supplier> supplier);

    List<Supplier> fetchBagRelationships(List<Supplier> suppliers);

    Page<Supplier> fetchBagRelationships(Page<Supplier> suppliers);
}
