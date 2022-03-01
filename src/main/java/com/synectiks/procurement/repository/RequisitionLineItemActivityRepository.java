package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.RequisitionLineItemActivity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RequisitionLineItemActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequisitionLineItemActivityRepository extends JpaRepository<RequisitionLineItemActivity, Long> {
}
