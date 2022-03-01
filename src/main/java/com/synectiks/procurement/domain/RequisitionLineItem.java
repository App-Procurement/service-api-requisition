package com.synectiks.procurement.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * A RequisitionLineItem.
 */
@Entity
@Table(name = "requisition_line_item")
public class RequisitionLineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

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

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "order_quantity")
    private Integer orderQuantity;

    @Column(name = "price")
    private Integer price;

    @Column(name = "priority")
    private String priority;

    @Size(max = 5000)
    @Column(name = "notes", length = 5000)
    private String notes;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "rate_per_item")
    private Integer ratePerItem;

    @OneToMany(mappedBy = "requisitionLineItem")
    private Set<Document> documentLists = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "requisitionLineItemLists", allowSetters = true)
    private Requisition requisition;

    public byte[] getDataFile() {
		return dataFile;
	}

	public void setDataFile(byte[] dataFile) {
		this.dataFile = dataFile;
	}

	@Transient
    @JsonProperty
    private byte[] dataFile;
    
    @Transient
    @JsonProperty
    private List<RequisitionLineItemActivity> activityList;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public RequisitionLineItem createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public RequisitionLineItem createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public RequisitionLineItem updatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public RequisitionLineItem updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getStatus() {
        return status;
    }

    public RequisitionLineItem status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProgressStage() {
        return progressStage;
    }

    public RequisitionLineItem progressStage(String progressStage) {
        this.progressStage = progressStage;
        return this;
    }

    public void setProgressStage(String progressStage) {
        this.progressStage = progressStage;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public RequisitionLineItem itemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
        return this;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public RequisitionLineItem orderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
        return this;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Integer getPrice() {
        return price;
    }

    public RequisitionLineItem price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPriority() {
        return priority;
    }

    public RequisitionLineItem priority(String priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getNotes() {
        return notes;
    }

    public RequisitionLineItem notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public RequisitionLineItem dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getRatePerItem() {
        return ratePerItem;
    }

    public RequisitionLineItem ratePerItem(Integer ratePerItem) {
        this.ratePerItem = ratePerItem;
        return this;
    }

    public void setRatePerItem(Integer ratePerItem) {
        this.ratePerItem = ratePerItem;
    }

    public Set<Document> getDocumentLists() {
        return documentLists;
    }

    public RequisitionLineItem documentLists(Set<Document> documents) {
        this.documentLists = documents;
        return this;
    }

    public RequisitionLineItem addDocumentList(Document document) {
        this.documentLists.add(document);
        document.setRequisitionLineItem(this);
        return this;
    }

    public RequisitionLineItem removeDocumentList(Document document) {
        this.documentLists.remove(document);
        document.setRequisitionLineItem(null);
        return this;
    }

    public void setDocumentLists(Set<Document> documents) {
        this.documentLists = documents;
    }

    public Requisition getRequisition() {
        return requisition;
    }

    public RequisitionLineItem requisition(Requisition requisition) {
        this.requisition = requisition;
        return this;
    }

    public void setRequisition(Requisition requisition) {
        this.requisition = requisition;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequisitionLineItem)) {
            return false;
        }
        return id != null && id.equals(((RequisitionLineItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequisitionLineItem{" +
            "id=" + getId() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", status='" + getStatus() + "'" +
            ", progressStage='" + getProgressStage() + "'" +
            ", itemDescription='" + getItemDescription() + "'" +
            ", orderQuantity=" + getOrderQuantity() +
            ", price=" + getPrice() +
            ", priority='" + getPriority() + "'" +
            ", notes='" + getNotes() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", ratePerItem=" + getRatePerItem() +
            "}";
    }

	public List<RequisitionLineItemActivity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<RequisitionLineItemActivity> activityList) {
		this.activityList = activityList;
	}
}
