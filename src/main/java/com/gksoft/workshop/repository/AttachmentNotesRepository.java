package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.AttachmentNotes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AttachmentNotes entity.
 *
 * When extending this class, extend AttachmentNotesRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AttachmentNotesRepository extends AttachmentNotesRepositoryWithBagRelationships, JpaRepository<AttachmentNotes, Long> {
    default Optional<AttachmentNotes> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<AttachmentNotes> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<AttachmentNotes> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct attachmentNotes from AttachmentNotes attachmentNotes left join fetch attachmentNotes.status left join fetch attachmentNotes.action",
        countQuery = "select count(distinct attachmentNotes) from AttachmentNotes attachmentNotes"
    )
    Page<AttachmentNotes> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct attachmentNotes from AttachmentNotes attachmentNotes left join fetch attachmentNotes.status left join fetch attachmentNotes.action"
    )
    List<AttachmentNotes> findAllWithToOneRelationships();

    @Query(
        "select attachmentNotes from AttachmentNotes attachmentNotes left join fetch attachmentNotes.status left join fetch attachmentNotes.action where attachmentNotes.id =:id"
    )
    Optional<AttachmentNotes> findOneWithToOneRelationships(@Param("id") Long id);
}
