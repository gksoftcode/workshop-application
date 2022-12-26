package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.AttachmentNotes;
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
public class AttachmentNotesRepositoryWithBagRelationshipsImpl implements AttachmentNotesRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AttachmentNotes> fetchBagRelationships(Optional<AttachmentNotes> attachmentNotes) {
        return attachmentNotes.map(this::fetchAttachments);
    }

    @Override
    public Page<AttachmentNotes> fetchBagRelationships(Page<AttachmentNotes> attachmentNotes) {
        return new PageImpl<>(
            fetchBagRelationships(attachmentNotes.getContent()),
            attachmentNotes.getPageable(),
            attachmentNotes.getTotalElements()
        );
    }

    @Override
    public List<AttachmentNotes> fetchBagRelationships(List<AttachmentNotes> attachmentNotes) {
        return Optional.of(attachmentNotes).map(this::fetchAttachments).orElse(Collections.emptyList());
    }

    AttachmentNotes fetchAttachments(AttachmentNotes result) {
        return entityManager
            .createQuery(
                "select attachmentNotes from AttachmentNotes attachmentNotes left join fetch attachmentNotes.attachments where attachmentNotes is :attachmentNotes",
                AttachmentNotes.class
            )
            .setParameter("attachmentNotes", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<AttachmentNotes> fetchAttachments(List<AttachmentNotes> attachmentNotes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, attachmentNotes.size()).forEach(index -> order.put(attachmentNotes.get(index).getId(), index));
        List<AttachmentNotes> result = entityManager
            .createQuery(
                "select distinct attachmentNotes from AttachmentNotes attachmentNotes left join fetch attachmentNotes.attachments where attachmentNotes in :attachmentNotes",
                AttachmentNotes.class
            )
            .setParameter("attachmentNotes", attachmentNotes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
