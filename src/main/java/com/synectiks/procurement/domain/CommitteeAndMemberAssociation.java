package com.synectiks.procurement.domain;

import java.io.Serializable;

public class CommitteeAndMemberAssociation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String memberStatus;
    private Committee committee;
	public String getMemberStatus() {
		return memberStatus;
	}
	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}
	public Committee getCommittee() {
		return committee;
	}
	public void setCommittee(Committee committee) {
		this.committee = committee;
	}


}
