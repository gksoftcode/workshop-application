package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.WorkOrders;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkOrders entity.
 */
@Repository
public interface WorkOrdersRepository extends JpaRepository<WorkOrders, Long> {
    default Optional<WorkOrders> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<WorkOrders> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<WorkOrders> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct workOrders from WorkOrders workOrders left join fetch workOrders.status left join fetch workOrders.client left join fetch workOrders.itemModels left join fetch workOrders.itemBrand",
        countQuery = "select count(distinct workOrders) from WorkOrders workOrders"
    )
    Page<WorkOrders> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct workOrders from WorkOrders workOrders left join fetch workOrders.status left join fetch workOrders.client left join fetch workOrders.itemModels left join fetch workOrders.itemBrand"
    )
    List<WorkOrders> findAllWithToOneRelationships();

    @Query(
        "select workOrders from WorkOrders workOrders left join fetch workOrders.status left join fetch workOrders.client left join fetch workOrders.itemModels left join fetch workOrders.itemBrand where workOrders.id =:id"
    )
    Optional<WorkOrders> findOneWithToOneRelationships(@Param("id") Long id);
}
