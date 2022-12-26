package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.Invoice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Invoice entity.
 *
 * When extending this class, extend InvoiceRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface InvoiceRepository extends InvoiceRepositoryWithBagRelationships, JpaRepository<Invoice, Long> {
    default Optional<Invoice> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Invoice> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Invoice> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct invoice from Invoice invoice left join fetch invoice.client",
        countQuery = "select count(distinct invoice) from Invoice invoice"
    )
    Page<Invoice> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct invoice from Invoice invoice left join fetch invoice.client")
    List<Invoice> findAllWithToOneRelationships();

    @Query("select invoice from Invoice invoice left join fetch invoice.client where invoice.id =:id")
    Optional<Invoice> findOneWithToOneRelationships(@Param("id") Long id);
}
