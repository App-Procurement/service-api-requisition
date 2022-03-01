package com.synectiks.procurement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.synectiks.procurement.web.rest.TestUtil;

public class CommitteeMemberTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommitteeMember.class);
        CommitteeMember committeeMember1 = new CommitteeMember();
        committeeMember1.setId(1L);
        CommitteeMember committeeMember2 = new CommitteeMember();
        committeeMember2.setId(committeeMember1.getId());
        assertThat(committeeMember1).isEqualTo(committeeMember2);
        committeeMember2.setId(2L);
        assertThat(committeeMember1).isNotEqualTo(committeeMember2);
        committeeMember1.setId(null);
        assertThat(committeeMember1).isNotEqualTo(committeeMember2);
    }
}
