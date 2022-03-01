package com.synectiks.procurement.domain;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A RequisitionActivity.
 */
@Entity
@Table(name = "requisition_activity")
public class RequisitionActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "requisition_no")
    private String requisitionNo;

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

    /**
     * progressStage : ReadyToBuy, POGenerated, SendToVendors,\nQuotationReceived,QuotationApproved,InvoiceGenerated,\nPaymentCompleted
     */
    @ApiModelProperty(value = "progressStage : ReadyToBuy, POGenerated, SendToVendors,\nQuotationReceived,QuotationApproved,InvoiceGenerated,\nPaymentCompleted")
    @Column(name = "progress_stage")
    private String progressStage;

    @Column(name = "financial_year")
    private Integer financialYear;

    @Column(name = "type")
    private String type;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Size(max = 5000)
    @Column(name = "notes", length = 5000)
    private String notes;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "requisition_id")
    private Long requisitionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequisitionNo() {
        return requisitionNo;
    }

    public RequisitionActivity requisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
        return this;
    }

    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public RequisitionActivity createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public RequisitionActivity createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public RequisitionActivity updatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public RequisitionActivity updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getStatus() {
        return status;
    }

    public RequisitionActivity status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProgressStage() {
        return progressStage;
    }

    public RequisitionActivity progressStage(String progressStage) {
        this.progressStage = progressStage;
        return this;
    }

    public void setProgressStage(String progressStage) {
        this.progressStage = progressStage;
    }

    public Integer getFinancialYear() {
        return financialYear;
    }

    public RequisitionActivity financialYear(Integer financialYear) {
        this.financialYear = financialYear;
        return this;
    }

    public void setFinancialYear(Integer financialYear) {
        this.financialYear = financialYear;
    }

    public String getType() {
        return type;
    }

    public RequisitionActivity type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public RequisitionActivity totalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNotes() {
        return notes;
    }

    public RequisitionActivity notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public RequisitionActivity dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getRequisitionId() {
        return requisitionId;
    }

    public RequisitionActivity requisitionId(Long requisitionId) {
        this.requisitionId = requisitionId;
        return this;
    }

    public void setRequisitionId(Long requisitionId) {
        this.requisitionId = requisitionId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequisitionActivity)) {
            return false;
        }
        return id != null && id.equals(((RequisitionActivity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequisitionActivity{" +
            "id=" + getId() +
            ", requisitionNo='" + getRequisitionNo() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", status='" + getStatus() + "'" +
            ", progressStage='" + getProgressStage() + "'" +
            ", financialYear=" + getFinancialYear() +
            ", type='" + getType() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", notes='" + getNotes() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", requisitionId=" + getRequisitionId() +
            "}";
    }
}
