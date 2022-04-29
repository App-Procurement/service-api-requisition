package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.ApprovalRules;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApprovalRules entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApprovalRulesRepository extends JpaRepository<ApprovalRules, Long> {
	
	public ApprovalRules findByRole(String role);
	 
}
