package com.synectiks.procurement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.synectiks.procurement.web.rest.TestUtil;

public class RequisitionLineItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequisitionLineItem.class);
        RequisitionLineItem requisitionLineItem1 = new RequisitionLineItem();
        requisitionLineItem1.setId(1L);
        RequisitionLineItem requisitionLineItem2 = new RequisitionLineItem();
        requisitionLineItem2.setId(requisitionLineItem1.getId());
        assertThat(requisitionLineItem1).isEqualTo(requisitionLineItem2);
        requisitionLineItem2.setId(2L);
        assertThat(requisitionLineItem1).isNotEqualTo(requisitionLineItem2);
        requisitionLineItem1.setId(null);
        assertThat(requisitionLineItem1).isNotEqualTo(requisitionLineItem2);
    }
}
