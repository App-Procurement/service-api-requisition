package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.BuyerRequisitionLink;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BuyerRequisitionLink entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BuyerRequisitionLinkRepository extends JpaRepository<BuyerRequisitionLink, Long> {}
