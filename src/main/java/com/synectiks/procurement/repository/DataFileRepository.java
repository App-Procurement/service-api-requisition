package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.DataFile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DataFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataFileRepository extends JpaRepository<DataFile, Long> {}
