package com.synectiks.procurement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.synectiks.procurement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuotationActivityTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(QuotationActivity.class);
    QuotationActivity quotationActivity1 = new QuotationActivity();
    quotationActivity1.setId(1L);
    QuotationActivity quotationActivity2 = new QuotationActivity();
    quotationActivity2.setId(quotationActivity1.getId());
    assertThat(quotationActivity1).isEqualTo(quotationActivity2);
    quotationActivity2.setId(2L);
    assertThat(quotationActivity1).isNotEqualTo(quotationActivity2);
    quotationActivity1.setId(null);
    assertThat(quotationActivity1).isNotEqualTo(quotationActivity2);
  }
}
