package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.RolesGroup;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RolesGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RolesGroupRepository extends JpaRepository<RolesGroup, Long> {
}
