package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.ContactActivity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ContactActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactActivityRepository extends JpaRepository<ContactActivity, Long> {
}
