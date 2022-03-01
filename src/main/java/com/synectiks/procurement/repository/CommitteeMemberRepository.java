package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.CommitteeMember;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CommitteeMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommitteeMemberRepository extends JpaRepository<CommitteeMember, Long> {
}
