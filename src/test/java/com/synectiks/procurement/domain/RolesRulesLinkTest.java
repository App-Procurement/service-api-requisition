package com.synectiks.procurement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.synectiks.procurement.web.rest.TestUtil;

public class RolesRulesLinkTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RolesRulesLink.class);
        RolesRulesLink rolesRulesLink1 = new RolesRulesLink();
        rolesRulesLink1.setId(1L);
        RolesRulesLink rolesRulesLink2 = new RolesRulesLink();
        rolesRulesLink2.setId(rolesRulesLink1.getId());
        assertThat(rolesRulesLink1).isEqualTo(rolesRulesLink2);
        rolesRulesLink2.setId(2L);
        assertThat(rolesRulesLink1).isNotEqualTo(rolesRulesLink2);
        rolesRulesLink1.setId(null);
        assertThat(rolesRulesLink1).isNotEqualTo(rolesRulesLink2);
    }
}
