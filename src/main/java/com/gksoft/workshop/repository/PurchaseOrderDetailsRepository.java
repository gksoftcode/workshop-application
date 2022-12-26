package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.PurchaseOrderDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchaseOrderDetails entity.
 */
@Repository
public interface PurchaseOrderDetailsRepository extends JpaRepository<PurchaseOrderDetails, Long> {
    default Optional<PurchaseOrderDetails> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PurchaseOrderDetails> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PurchaseOrderDetails> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct purchaseOrderDetails from PurchaseOrderDetails purchaseOrderDetails left join fetch purchaseOrderDetails.tax",
        countQuery = "select count(distinct purchaseOrderDetails) from PurchaseOrderDetails purchaseOrderDetails"
    )
    Page<PurchaseOrderDetails> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct purchaseOrderDetails from PurchaseOrderDetails purchaseOrderDetails left join fetch purchaseOrderDetails.tax")
    List<PurchaseOrderDetails> findAllWithToOneRelationships();

    @Query(
        "select purchaseOrderDetails from PurchaseOrderDetails purchaseOrderDetails left join fetch purchaseOrderDetails.tax where purchaseOrderDetails.id =:id"
    )
    Optional<PurchaseOrderDetails> findOneWithToOneRelationships(@Param("id") Long id);
}
