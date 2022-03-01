package com.synectiks.procurement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.synectiks.procurement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactActivityTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ContactActivity.class);
    ContactActivity contactActivity1 = new ContactActivity();
    contactActivity1.setId(1L);
    ContactActivity contactActivity2 = new ContactActivity();
    contactActivity2.setId(contactActivity1.getId());
    assertThat(contactActivity1).isEqualTo(contactActivity2);
    contactActivity2.setId(2L);
    assertThat(contactActivity1).isNotEqualTo(contactActivity2);
    contactActivity1.setId(null);
    assertThat(contactActivity1).isNotEqualTo(contactActivity2);
  }
}
