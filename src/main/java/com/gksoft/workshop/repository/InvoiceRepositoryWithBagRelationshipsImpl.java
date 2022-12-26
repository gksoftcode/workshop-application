package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.Invoice;
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
public class InvoiceRepositoryWithBagRelationshipsImpl implements InvoiceRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Invoice> fetchBagRelationships(Optional<Invoice> invoice) {
        return invoice.map(this::fetchAttachments);
    }

    @Override
    public Page<Invoice> fetchBagRelationships(Page<Invoice> invoices) {
        return new PageImpl<>(fetchBagRelationships(invoices.getContent()), invoices.getPageable(), invoices.getTotalElements());
    }

    @Override
    public List<Invoice> fetchBagRelationships(List<Invoice> invoices) {
        return Optional.of(invoices).map(this::fetchAttachments).orElse(Collections.emptyList());
    }

    Invoice fetchAttachments(Invoice result) {
        return entityManager
            .createQuery("select invoice from Invoice invoice left join fetch invoice.attachments where invoice is :invoice", Invoice.class)
            .setParameter("invoice", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Invoice> fetchAttachments(List<Invoice> invoices) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, invoices.size()).forEach(index -> order.put(invoices.get(index).getId(), index));
        List<Invoice> result = entityManager
            .createQuery(
                "select distinct invoice from Invoice invoice left join fetch invoice.attachments where invoice in :invoices",
                Invoice.class
            )
            .setParameter("invoices", invoices)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
