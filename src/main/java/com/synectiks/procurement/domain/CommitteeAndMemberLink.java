package com.synectiks.procurement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A CommitteeAndMemberLink.
 */
@Entity
@Table(name = "committee_and_member_link")
public class CommitteeAndMemberLink implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JsonIgnoreProperties(value = "committeeAndMemberLinks", allowSetters = true)
    private Committee committee;

    @ManyToOne
    @JsonIgnoreProperties(value = "committeeAndMemberLinks", allowSetters = true)
    private CommitteeMember committeeMember;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Committee getCommittee() {
        return committee;
    }

    public void setCommittee(Committee committee) {
        this.committee = committee;
    }

    public CommitteeMember getCommitteeMember() {
        return committeeMember;
    }

    public void setCommitteeMember(CommitteeMember committeeMember) {
        this.committeeMember = committeeMember;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommitteeAndMemberLink)) {
            return false;
        }
        return id != null && id.equals(((CommitteeAndMemberLink) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CommitteeAndMemberLink{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
