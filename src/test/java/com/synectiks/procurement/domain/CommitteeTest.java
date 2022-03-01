package com.synectiks.procurement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.synectiks.procurement.web.rest.TestUtil;

public class CommitteeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Committee.class);
        Committee committee1 = new Committee();
        committee1.setId(1L);
        Committee committee2 = new Committee();
        committee2.setId(committee1.getId());
        assertThat(committee1).isEqualTo(committee2);
        committee2.setId(2L);
        assertThat(committee1).isNotEqualTo(committee2);
        committee1.setId(null);
        assertThat(committee1).isNotEqualTo(committee2);
    }
}
