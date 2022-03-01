package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.RolesRulesLink;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RolesRulesLink entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RolesRulesLinkRepository extends JpaRepository<RolesRulesLink, Long> {
}
