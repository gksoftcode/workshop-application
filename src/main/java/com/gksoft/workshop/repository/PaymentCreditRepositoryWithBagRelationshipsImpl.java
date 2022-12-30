package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.PaymentCredit;
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
public class PaymentCreditRepositoryWithBagRelationshipsImpl implements PaymentCreditRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PaymentCredit> fetchBagRelationships(Optional<PaymentCredit> paymentCredit) {
        return paymentCredit.map(this::fetchAttachments);
    }

    @Override
    public Page<PaymentCredit> fetchBagRelationships(Page<PaymentCredit> paymentCredits) {
        return new PageImpl<>(
            fetchBagRelationships(paymentCredits.getContent()),
            paymentCredits.getPageable(),
            paymentCredits.getTotalElements()
        );
    }

    @Override
    public List<PaymentCredit> fetchBagRelationships(List<PaymentCredit> paymentCredits) {
        return Optional.of(paymentCredits).map(this::fetchAttachments).orElse(Collections.emptyList());
    }

    PaymentCredit fetchAttachments(PaymentCredit result) {
        return entityManager
            .createQuery(
                "select paymentCredit from PaymentCredit paymentCredit left join fetch paymentCredit.attachments where paymentCredit is :paymentCredit",
                PaymentCredit.class
            )
            .setParameter("paymentCredit", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<PaymentCredit> fetchAttachments(List<PaymentCredit> paymentCredits) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, paymentCredits.size()).forEach(index -> order.put(paymentCredits.get(index).getId(), index));
        List<PaymentCredit> result = entityManager
            .createQuery(
                "select distinct paymentCredit from PaymentCredit paymentCredit left join fetch paymentCredit.attachments where paymentCredit in :paymentCredits",
                PaymentCredit.class
            )
            .setParameter("paymentCredits", paymentCredits)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
