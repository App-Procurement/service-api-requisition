package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.Committee;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Committee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommitteeRepository extends JpaRepository<Committee, Long> {
}
