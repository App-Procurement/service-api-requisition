package com.synectiks.procurement.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApprovalRules.
 */
@Entity
@Table(name = "approval_rules")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApprovalRules implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

  @Column(name = "role")
  private String role;

  @Column(name = "min_limit")
  private Integer minLimit;

  @Column(name = "max_limit")
  private Integer maxLimit;

  @Column(name = "created_on")
  private Instant createdOn;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_on")
  private Instant updatedOn;

  @Column(name = "updated_by")
  private String updatedBy;

  @Column(name = "status")
  private String status;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public ApprovalRules id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRole() {
    return this.role;
  }

  public ApprovalRules role(String role) {
    this.setRole(role);
    return this;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Integer getMinLimit() {
    return this.minLimit;
  }

  public ApprovalRules minLimit(Integer minLimit) {
    this.setMinLimit(minLimit);
    return this;
  }

  public void setMinLimit(Integer minLimit) {
    this.minLimit = minLimit;
  }

  public Integer getMaxLimit() {
    return this.maxLimit;
  }

  public ApprovalRules maxLimit(Integer maxLimit) {
    this.setMaxLimit(maxLimit);
    return this;
  }

  public void setMaxLimit(Integer maxLimit) {
    this.maxLimit = maxLimit;
  }

  public Instant getCreatedOn() {
    return this.createdOn;
  }

  public ApprovalRules createdOn(Instant createdOn) {
    this.setCreatedOn(createdOn);
    return this;
  }

  public void setCreatedOn(Instant createdOn) {
    this.createdOn = createdOn;
  }

  public String getCreatedBy() {
    return this.createdBy;
  }

  public ApprovalRules createdBy(String createdBy) {
    this.setCreatedBy(createdBy);
    return this;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Instant getUpdatedOn() {
    return this.updatedOn;
  }

  public ApprovalRules updatedOn(Instant updatedOn) {
    this.setUpdatedOn(updatedOn);
    return this;
  }

  public void setUpdatedOn(Instant updatedOn) {
    this.updatedOn = updatedOn;
  }

  public String getUpdatedBy() {
    return this.updatedBy;
  }

  public ApprovalRules updatedBy(String updatedBy) {
    this.setUpdatedBy(updatedBy);
    return this;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public String getStatus() {
    return this.status;
  }

  public ApprovalRules status(String status) {
    this.setStatus(status);
    return this;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ApprovalRules)) {
      return false;
    }
    return id != null && id.equals(((ApprovalRules) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "ApprovalRules{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            ", minLimit=" + getMinLimit() +
            ", maxLimit=" + getMaxLimit() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
