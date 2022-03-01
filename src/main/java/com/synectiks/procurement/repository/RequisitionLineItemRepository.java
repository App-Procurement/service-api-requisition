package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.RequisitionLineItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RequisitionLineItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequisitionLineItemRepository extends JpaRepository<RequisitionLineItem, Long> {
}
