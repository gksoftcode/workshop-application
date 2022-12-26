package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.PurchaseOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchaseOrder entity.
 *
 * When extending this class, extend PurchaseOrderRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface PurchaseOrderRepository extends PurchaseOrderRepositoryWithBagRelationships, JpaRepository<PurchaseOrder, Long> {
    default Optional<PurchaseOrder> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<PurchaseOrder> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<PurchaseOrder> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct purchaseOrder from PurchaseOrder purchaseOrder left join fetch purchaseOrder.supplier",
        countQuery = "select count(distinct purchaseOrder) from PurchaseOrder purchaseOrder"
    )
    Page<PurchaseOrder> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct purchaseOrder from PurchaseOrder purchaseOrder left join fetch purchaseOrder.supplier")
    List<PurchaseOrder> findAllWithToOneRelationships();

    @Query("select purchaseOrder from PurchaseOrder purchaseOrder left join fetch purchaseOrder.supplier where purchaseOrder.id =:id")
    Optional<PurchaseOrder> findOneWithToOneRelationships(@Param("id") Long id);
}
