package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.Supplier;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SupplierRepositoryWithBagRelationshipsImpl implements SupplierRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Supplier> fetchBagRelationships(Optional<Supplier> supplier) {
        return supplier.map(this::fetchLocations);
    }

    @Override
    public Page<Supplier> fetchBagRelationships(Page<Supplier> suppliers) {
        return new PageImpl<>(fetchBagRelationships(suppliers.getContent()), suppliers.getPageable(), suppliers.getTotalElements());
    }

    @Override
    public List<Supplier> fetchBagRelationships(List<Supplier> suppliers) {
        return Optional.of(suppliers).map(this::fetchLocations).orElse(Collections.emptyList());
    }

    Supplier fetchLocations(Supplier result) {
        return entityManager
            .createQuery(
                "select supplier from Supplier supplier left join fetch supplier.locations where supplier is :supplier",
                Supplier.class
            )
            .setParameter("supplier", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Supplier> fetchLocations(List<Supplier> suppliers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, suppliers.size()).forEach(index -> order.put(suppliers.get(index).getId(), index));
        List<Supplier> result = entityManager
            .createQuery(
                "select distinct supplier from Supplier supplier left join fetch supplier.locations where supplier in :suppliers",
                Supplier.class
            )
            .setParameter("suppliers", suppliers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
