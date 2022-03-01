package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.InvoiceActivity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InvoiceActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceActivityRepository extends JpaRepository<InvoiceActivity, Long> {
}
