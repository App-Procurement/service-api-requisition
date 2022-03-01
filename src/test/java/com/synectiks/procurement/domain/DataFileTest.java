package com.synectiks.procurement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.synectiks.procurement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DataFileTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(DataFile.class);
    DataFile dataFile1 = new DataFile();
    dataFile1.setId(1L);
    DataFile dataFile2 = new DataFile();
    dataFile2.setId(dataFile1.getId());
    assertThat(dataFile1).isEqualTo(dataFile2);
    dataFile2.setId(2L);
    assertThat(dataFile1).isNotEqualTo(dataFile2);
    dataFile1.setId(null);
    assertThat(dataFile1).isNotEqualTo(dataFile2);
  }
}
