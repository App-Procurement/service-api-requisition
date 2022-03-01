package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.RequisitionActivity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RequisitionActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequisitionActivityRepository extends JpaRepository<RequisitionActivity, Long> {
}
