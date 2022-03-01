package com.synectiks.procurement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.synectiks.procurement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequisitionActivityTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(RequisitionActivity.class);
    RequisitionActivity requisitionActivity1 = new RequisitionActivity();
    requisitionActivity1.setId(1L);
    RequisitionActivity requisitionActivity2 = new RequisitionActivity();
    requisitionActivity2.setId(requisitionActivity1.getId());
    assertThat(requisitionActivity1).isEqualTo(requisitionActivity2);
    requisitionActivity2.setId(2L);
    assertThat(requisitionActivity1).isNotEqualTo(requisitionActivity2);
    requisitionActivity1.setId(null);
    assertThat(requisitionActivity1).isNotEqualTo(requisitionActivity2);
  }
}
