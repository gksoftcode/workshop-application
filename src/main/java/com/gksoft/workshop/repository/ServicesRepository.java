package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.Services;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Services entity.
 */
@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {
    default Optional<Services> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Services> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Services> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct services from Services services left join fetch services.tax",
        countQuery = "select count(distinct services) from Services services"
    )
    Page<Services> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct services from Services services left join fetch services.tax")
    List<Services> findAllWithToOneRelationships();

    @Query("select services from Services services left join fetch services.tax where services.id =:id")
    Optional<Services> findOneWithToOneRelationships(@Param("id") Long id);
}
