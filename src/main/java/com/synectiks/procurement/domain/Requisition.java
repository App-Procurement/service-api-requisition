package com.synectiks.procurement.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * A Requisition.
 */
@Entity
@Table(name = "requisition")
public class Requisition implements Serializable {

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

    @OneToOne
    @JoinColumn(unique = true)
    private Department department;

    @OneToOne
    @JoinColumn(unique = true)
    private Currency currency;

    @OneToMany(mappedBy = "requisition")
    private Set<RequisitionLineItem> requisitionLineItemLists = new HashSet<>();

    @OneToMany(mappedBy = "requisition")
    private Set<VendorRequisitionBucket> requisitions = new HashSet<>();

    @Transient
    @JsonProperty
    private List<RequisitionActivity> activityList;
    
//    @Transient
//    @JsonProperty
//    private byte[] extraBudgetoryFile;
    
    @Transient
    @JsonProperty
    private List<Document> documentList = new ArrayList<Document>();
    
    @Transient
    @JsonProperty
    private List<RequisitionLineItem> lineItemList;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequisitionNo() {
        return requisitionNo;
    }

    public Requisition requisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
        return this;
    }

    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Requisition createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Requisition createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public Requisition updatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Requisition updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getStatus() {
        return status;
    }

    public Requisition status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProgressStage() {
        return progressStage;
    }

    public Requisition progressStage(String progressStage) {
        this.progressStage = progressStage;
        return this;
    }

    public void setProgressStage(String progressStage) {
        this.progressStage = progressStage;
    }

    public Integer getFinancialYear() {
        return financialYear;
    }

    public Requisition financialYear(Integer financialYear) {
        this.financialYear = financialYear;
        return this;
    }

    public void setFinancialYear(Integer financialYear) {
        this.financialYear = financialYear;
    }

    public String getType() {
        return type;
    }

    public Requisition type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Requisition totalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNotes() {
        return notes;
    }

    public Requisition notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Requisition dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Department getDepartment() {
        return department;
    }

    public Requisition department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Requisition currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Set<RequisitionLineItem> getRequisitionLineItemLists() {
        return requisitionLineItemLists;
    }

    public Requisition requisitionLineItemLists(Set<RequisitionLineItem> requisitionLineItems) {
        this.requisitionLineItemLists = requisitionLineItems;
        return this;
    }

    public Requisition addRequisitionLineItemList(RequisitionLineItem requisitionLineItem) {
        this.requisitionLineItemLists.add(requisitionLineItem);
        requisitionLineItem.setRequisition(this);
        return this;
    }

    public Requisition removeRequisitionLineItemList(RequisitionLineItem requisitionLineItem) {
        this.requisitionLineItemLists.remove(requisitionLineItem);
        requisitionLineItem.setRequisition(null);
        return this;
    }

    public void setRequisitionLineItemLists(Set<RequisitionLineItem> requisitionLineItems) {
        this.requisitionLineItemLists = requisitionLineItems;
    }

    public Set<VendorRequisitionBucket> getRequisitions() {
        return requisitions;
    }

    public Requisition requisitions(Set<VendorRequisitionBucket> vendorRequisitionBuckets) {
        this.requisitions = vendorRequisitionBuckets;
        return this;
    }

    public Requisition addRequisition(VendorRequisitionBucket vendorRequisitionBucket) {
        this.requisitions.add(vendorRequisitionBucket);
        vendorRequisitionBucket.setRequisition(this);
        return this;
    }

    public Requisition removeRequisition(VendorRequisitionBucket vendorRequisitionBucket) {
        this.requisitions.remove(vendorRequisitionBucket);
        vendorRequisitionBucket.setRequisition(null);
        return this;
    }

    public void setRequisitions(Set<VendorRequisitionBucket> vendorRequisitionBuckets) {
        this.requisitions = vendorRequisitionBuckets;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Requisition)) {
            return false;
        }
        return id != null && id.equals(((Requisition) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Requisition{" +
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
            "}";
    }

	public List<RequisitionActivity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<RequisitionActivity> activityList) {
		this.activityList = activityList;
	}

	

	public List<Document> getDocumentList() {
		return documentList;
	}

	public void setDocumentList(List<Document> documentList) {
		this.documentList = documentList;
	}

	public List<RequisitionLineItem> getLineItemList() {
		return lineItemList;
	}

	public void setLineItemList(List<RequisitionLineItem> lineItemList) {
		this.lineItemList = lineItemList;
	}
}
