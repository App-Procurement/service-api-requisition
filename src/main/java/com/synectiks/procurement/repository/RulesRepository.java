package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.Rules;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Rules entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RulesRepository extends JpaRepository<Rules, Long> {
}
