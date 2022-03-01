package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.VendorRequisitionBucket;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the VendorRequisitionBucket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VendorRequisitionBucketRepository extends JpaRepository<VendorRequisitionBucket, Long> {
}
