package com.synectiks.procurement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.synectiks.procurement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequisitionLineItemActivityTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(RequisitionLineItemActivity.class);
    RequisitionLineItemActivity requisitionLineItemActivity1 = new RequisitionLineItemActivity();
    requisitionLineItemActivity1.setId(1L);
    RequisitionLineItemActivity requisitionLineItemActivity2 = new RequisitionLineItemActivity();
    requisitionLineItemActivity2.setId(requisitionLineItemActivity1.getId());
    assertThat(requisitionLineItemActivity1).isEqualTo(requisitionLineItemActivity2);
    requisitionLineItemActivity2.setId(2L);
    assertThat(requisitionLineItemActivity1).isNotEqualTo(requisitionLineItemActivity2);
    requisitionLineItemActivity1.setId(null);
    assertThat(requisitionLineItemActivity1).isNotEqualTo(requisitionLineItemActivity2);
  }
}
