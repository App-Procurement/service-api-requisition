package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.Buyer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Buyer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {}
