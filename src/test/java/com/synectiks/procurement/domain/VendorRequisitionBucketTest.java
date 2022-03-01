package com.synectiks.procurement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.synectiks.procurement.web.rest.TestUtil;

public class VendorRequisitionBucketTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VendorRequisitionBucket.class);
        VendorRequisitionBucket vendorRequisitionBucket1 = new VendorRequisitionBucket();
        vendorRequisitionBucket1.setId(1L);
        VendorRequisitionBucket vendorRequisitionBucket2 = new VendorRequisitionBucket();
        vendorRequisitionBucket2.setId(vendorRequisitionBucket1.getId());
        assertThat(vendorRequisitionBucket1).isEqualTo(vendorRequisitionBucket2);
        vendorRequisitionBucket2.setId(2L);
        assertThat(vendorRequisitionBucket1).isNotEqualTo(vendorRequisitionBucket2);
        vendorRequisitionBucket1.setId(null);
        assertThat(vendorRequisitionBucket1).isNotEqualTo(vendorRequisitionBucket2);
    }
}
