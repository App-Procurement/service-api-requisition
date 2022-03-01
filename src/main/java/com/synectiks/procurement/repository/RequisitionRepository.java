package com.synectiks.procurement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.synectiks.procurement.domain.Requisition;

/**
 * Spring Data  repository for the Requisition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequisitionRepository extends JpaRepository<Requisition, Long> {
	
	@Query("select r FROM Requisition r WHERE UPPER(r.progressStage) != UPPER(?1) and UPPER(r.progressStage) != UPPER(?2) ")
	public List<Requisition> getAllRequisition(String rejected, String completed );

}
