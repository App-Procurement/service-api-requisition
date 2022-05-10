package com.synectiks.procurement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BuyerRequisitionLink.
 */
@Entity
@Table(name = "buyer_requisition_link")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BuyerRequisitionLink implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

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

  @Column(name = "notes")
  private String notes;

  @ManyToOne
  private Buyer buyer;

  @ManyToOne
  @JsonIgnoreProperties(value = { "department", "currency", "requisitionLineItemLists", "requisitions" }, allowSetters = true)
  private Requisition requisition;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public BuyerRequisitionLink id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatus() {
    return this.status;
  }

  public BuyerRequisitionLink status(String status) {
    this.setStatus(status);
    return this;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Instant getCreatedOn() {
    return this.createdOn;
  }

  public BuyerRequisitionLink createdOn(Instant createdOn) {
    this.setCreatedOn(createdOn);
    return this;
  }

  public void setCreatedOn(Instant createdOn) {
    this.createdOn = createdOn;
  }

  public String getCreatedBy() {
    return this.createdBy;
  }

  public BuyerRequisitionLink createdBy(String createdBy) {
    this.setCreatedBy(createdBy);
    return this;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Instant getUpdatedOn() {
    return this.updatedOn;
  }

  public BuyerRequisitionLink updatedOn(Instant updatedOn) {
    this.setUpdatedOn(updatedOn);
    return this;
  }

  public void setUpdatedOn(Instant updatedOn) {
    this.updatedOn = updatedOn;
  }

  public String getUpdatedBy() {
    return this.updatedBy;
  }

  public BuyerRequisitionLink updatedBy(String updatedBy) {
    this.setUpdatedBy(updatedBy);
    return this;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public String getNotes() {
    return this.notes;
  }

  public BuyerRequisitionLink notes(String notes) {
    this.setNotes(notes);
    return this;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Buyer getBuyer() {
    return this.buyer;
  }

  public void setBuyer(Buyer buyer) {
    this.buyer = buyer;
  }

  public BuyerRequisitionLink buyer(Buyer buyer) {
    this.setBuyer(buyer);
    return this;
  }

  public Requisition getRequisition() {
    return this.requisition;
  }

  public void setRequisition(Requisition requisition) {
    this.requisition = requisition;
  }

  public BuyerRequisitionLink requisition(Requisition requisition) {
    this.setRequisition(requisition);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BuyerRequisitionLink)) {
      return false;
    }
    return id != null && id.equals(((BuyerRequisitionLink) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "BuyerRequisitionLink{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
