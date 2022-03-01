package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.QuotationActivity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the QuotationActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuotationActivityRepository extends JpaRepository<QuotationActivity, Long> {
}
