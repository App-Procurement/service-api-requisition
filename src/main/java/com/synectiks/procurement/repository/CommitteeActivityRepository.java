package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.CommitteeActivity;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CommitteeActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommitteeActivityRepository extends JpaRepository<CommitteeActivity, Long> {
}
