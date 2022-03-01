package com.synectiks.procurement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.synectiks.procurement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommitteeActivityTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(CommitteeActivity.class);
    CommitteeActivity committeeActivity1 = new CommitteeActivity();
    committeeActivity1.setId(1L);
    CommitteeActivity committeeActivity2 = new CommitteeActivity();
    committeeActivity2.setId(committeeActivity1.getId());
    assertThat(committeeActivity1).isEqualTo(committeeActivity2);
    committeeActivity2.setId(2L);
    assertThat(committeeActivity1).isNotEqualTo(committeeActivity2);
    committeeActivity1.setId(null);
    assertThat(committeeActivity1).isNotEqualTo(committeeActivity2);
  }
}
