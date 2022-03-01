package com.synectiks.procurement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.synectiks.procurement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceActivityTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(InvoiceActivity.class);
    InvoiceActivity invoiceActivity1 = new InvoiceActivity();
    invoiceActivity1.setId(1L);
    InvoiceActivity invoiceActivity2 = new InvoiceActivity();
    invoiceActivity2.setId(invoiceActivity1.getId());
    assertThat(invoiceActivity1).isEqualTo(invoiceActivity2);
    invoiceActivity2.setId(2L);
    assertThat(invoiceActivity1).isNotEqualTo(invoiceActivity2);
    invoiceActivity1.setId(null);
    assertThat(invoiceActivity1).isNotEqualTo(invoiceActivity2);
  }
}
