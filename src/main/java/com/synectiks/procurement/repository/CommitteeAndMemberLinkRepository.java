package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.CommitteeAndMemberLink;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CommitteeAndMemberLink entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommitteeAndMemberLinkRepository extends JpaRepository<CommitteeAndMemberLink, Long> {
}
