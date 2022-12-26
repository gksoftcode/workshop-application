package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.PurchaseOrder;
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
public class PurchaseOrderRepositoryWithBagRelationshipsImpl implements PurchaseOrderRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PurchaseOrder> fetchBagRelationships(Optional<PurchaseOrder> purchaseOrder) {
        return purchaseOrder.map(this::fetchAttachments);
    }

    @Override
    public Page<PurchaseOrder> fetchBagRelationships(Page<PurchaseOrder> purchaseOrders) {
        return new PageImpl<>(
            fetchBagRelationships(purchaseOrders.getContent()),
            purchaseOrders.getPageable(),
            purchaseOrders.getTotalElements()
        );
    }

    @Override
    public List<PurchaseOrder> fetchBagRelationships(List<PurchaseOrder> purchaseOrders) {
        return Optional.of(purchaseOrders).map(this::fetchAttachments).orElse(Collections.emptyList());
    }

    PurchaseOrder fetchAttachments(PurchaseOrder result) {
        return entityManager
            .createQuery(
                "select purchaseOrder from PurchaseOrder purchaseOrder left join fetch purchaseOrder.attachments where purchaseOrder is :purchaseOrder",
                PurchaseOrder.class
            )
            .setParameter("purchaseOrder", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<PurchaseOrder> fetchAttachments(List<PurchaseOrder> purchaseOrders) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, purchaseOrders.size()).forEach(index -> order.put(purchaseOrders.get(index).getId(), index));
        List<PurchaseOrder> result = entityManager
            .createQuery(
                "select distinct purchaseOrder from PurchaseOrder purchaseOrder left join fetch purchaseOrder.attachments where purchaseOrder in :purchaseOrders",
                PurchaseOrder.class
            )
            .setParameter("purchaseOrders", purchaseOrders)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
