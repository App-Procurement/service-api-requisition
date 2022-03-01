package com.synectiks.procurement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.synectiks.procurement.web.rest.TestUtil;

public class CommitteeAndMemberLinkTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommitteeAndMemberLink.class);
        CommitteeAndMemberLink committeeAndMemberLink1 = new CommitteeAndMemberLink();
        committeeAndMemberLink1.setId(1L);
        CommitteeAndMemberLink committeeAndMemberLink2 = new CommitteeAndMemberLink();
        committeeAndMemberLink2.setId(committeeAndMemberLink1.getId());
        assertThat(committeeAndMemberLink1).isEqualTo(committeeAndMemberLink2);
        committeeAndMemberLink2.setId(2L);
        assertThat(committeeAndMemberLink1).isNotEqualTo(committeeAndMemberLink2);
        committeeAndMemberLink1.setId(null);
        assertThat(committeeAndMemberLink1).isNotEqualTo(committeeAndMemberLink2);
    }
}
