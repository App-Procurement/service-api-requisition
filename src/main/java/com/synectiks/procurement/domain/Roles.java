package com.synectiks.procurement.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Roles.
 */
@Entity
@Table(name = "roles")
public class Roles implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "status")
	private String status;

	@Column(name = "created_on")
	private Instant createdOn;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_on")
	private Instant updatedOn;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "securityservice_id")
	private Long securityserviceId;

	@Transient
	@JsonIgnore
	private boolean isGroup;

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Roles id(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return this.name;
	}

	public Roles name(String name) {
		this.name = name;
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public Roles description(String description) {
		this.description = description;
		return this;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return this.status;
	}

	public Roles status(String status) {
		this.status = status;
		return this;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Instant getCreatedOn() {
		return this.createdOn;
	}

	public Roles createdOn(Instant createdOn) {
		this.createdOn = createdOn;
		return this;
	}

	public void setCreatedOn(Instant createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public Roles createdBy(String createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Instant getUpdatedOn() {
		return this.updatedOn;
	}

	public Roles updatedOn(Instant updatedOn) {
		this.updatedOn = updatedOn;
		return this;
	}

	public void setUpdatedOn(Instant updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public Roles updatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
		return this;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getSecurityserviceId() {
		return this.securityserviceId;
	}

	public Roles securityserviceId(Long securityserviceId) {
		this.securityserviceId = securityserviceId;
		return this;
	}

	public void setSecurityserviceId(Long securityserviceId) {
		this.securityserviceId = securityserviceId;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Roles)) {
			return false;
		}
		return id != null && id.equals(((Roles) o).id);
	}

	@Override
	public int hashCode() {
		// see
		// https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "Roles{" + "id=" + getId() + ", name='" + getName() + "'" + ", description='" + getDescription() + "'"
				+ ", status='" + getStatus() + "'" + ", createdOn='" + getCreatedOn() + "'" + ", createdBy='"
				+ getCreatedBy() + "'" + ", updatedOn='" + getUpdatedOn() + "'" + ", updatedBy='" + getUpdatedBy() + "'"
				+ ", securityserviceId=" + getSecurityserviceId() + "}";
	}
}
