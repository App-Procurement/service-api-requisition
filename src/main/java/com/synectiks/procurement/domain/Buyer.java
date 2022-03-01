package com.synectiks.procurement.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Buyer.
 */
@Entity
@Table(name = "buyer")
public class Buyer implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "middle_name")
  private String middleName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "email")
  private String email;

  @Column(name = "address")
  private String address;

  @Column(name = "zip_code")
  private String zipCode;

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

  // jhipster-needle-entity-add-field - JHipster will add fields here
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Buyer id(Long id) {
    this.id = id;
    return this;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public Buyer firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return this.middleName;
  }

  public Buyer middleName(String middleName) {
    this.middleName = middleName;
    return this;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public Buyer lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public Buyer phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getEmail() {
    return this.email;
  }

  public Buyer email(String email) {
    this.email = email;
    return this;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return this.address;
  }

  public Buyer address(String address) {
    this.address = address;
    return this;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getZipCode() {
    return this.zipCode;
  }

  public Buyer zipCode(String zipCode) {
    this.zipCode = zipCode;
    return this;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getStatus() {
    return this.status;
  }

  public Buyer status(String status) {
    this.status = status;
    return this;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Instant getCreatedOn() {
    return this.createdOn;
  }

  public Buyer createdOn(Instant createdOn) {
    this.createdOn = createdOn;
    return this;
  }

  public void setCreatedOn(Instant createdOn) {
    this.createdOn = createdOn;
  }

  public String getCreatedBy() {
    return this.createdBy;
  }

  public Buyer createdBy(String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Instant getUpdatedOn() {
    return this.updatedOn;
  }

  public Buyer updatedOn(Instant updatedOn) {
    this.updatedOn = updatedOn;
    return this;
  }

  public void setUpdatedOn(Instant updatedOn) {
    this.updatedOn = updatedOn;
  }

  public String getUpdatedBy() {
    return this.updatedBy;
  }

  public Buyer updatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
    return this;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Buyer)) {
      return false;
    }
    return id != null && id.equals(((Buyer) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "Buyer{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", address='" + getAddress() + "'" +
            ", zipCode='" + getZipCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
