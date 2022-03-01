package com.synectiks.procurement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.synectiks.procurement.web.rest.TestUtil;

public class RequisitionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Requisition.class);
        Requisition requisition1 = new Requisition();
        requisition1.setId(1L);
        Requisition requisition2 = new Requisition();
        requisition2.setId(requisition1.getId());
        assertThat(requisition1).isEqualTo(requisition2);
        requisition2.setId(2L);
        assertThat(requisition1).isNotEqualTo(requisition2);
        requisition1.setId(null);
        assertThat(requisition1).isNotEqualTo(requisition2);
    }
}
