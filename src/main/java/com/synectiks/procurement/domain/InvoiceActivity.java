package com.synectiks.procurement.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A InvoiceActivity.
 */
@Entity
@Table(name = "invoice_activity")
public class InvoiceActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "invoice_no")
    private String invoiceNo;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "mode_of_payment")
    private String modeOfPayment;

    @Column(name = "txn_ref_no")
    private String txnRefNo;

    @Column(name = "cheque_or_dd_no")
    private String chequeOrDdNo;

    @Column(name = "issuer_bank")
    private String issuerBank;

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

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Size(max = 5000)
    @Column(name = "notes", length = 5000)
    private String notes;

    @Column(name = "invoice_id")
    private Long invoiceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public InvoiceActivity invoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        return this;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Integer getAmount() {
        return amount;
    }

    public InvoiceActivity amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public InvoiceActivity modeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
        return this;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getTxnRefNo() {
        return txnRefNo;
    }

    public InvoiceActivity txnRefNo(String txnRefNo) {
        this.txnRefNo = txnRefNo;
        return this;
    }

    public void setTxnRefNo(String txnRefNo) {
        this.txnRefNo = txnRefNo;
    }

    public String getChequeOrDdNo() {
        return chequeOrDdNo;
    }

    public InvoiceActivity chequeOrDdNo(String chequeOrDdNo) {
        this.chequeOrDdNo = chequeOrDdNo;
        return this;
    }

    public void setChequeOrDdNo(String chequeOrDdNo) {
        this.chequeOrDdNo = chequeOrDdNo;
    }

    public String getIssuerBank() {
        return issuerBank;
    }

    public InvoiceActivity issuerBank(String issuerBank) {
        this.issuerBank = issuerBank;
        return this;
    }

    public void setIssuerBank(String issuerBank) {
        this.issuerBank = issuerBank;
    }

    public String getStatus() {
        return status;
    }

    public InvoiceActivity status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public InvoiceActivity createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public InvoiceActivity createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public InvoiceActivity updatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public InvoiceActivity updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public InvoiceActivity dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getNotes() {
        return notes;
    }

    public InvoiceActivity notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public InvoiceActivity invoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceActivity)) {
            return false;
        }
        return id != null && id.equals(((InvoiceActivity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceActivity{" +
            "id=" + getId() +
            ", invoiceNo='" + getInvoiceNo() + "'" +
            ", amount=" + getAmount() +
            ", modeOfPayment='" + getModeOfPayment() + "'" +
            ", txnRefNo='" + getTxnRefNo() + "'" +
            ", chequeOrDdNo='" + getChequeOrDdNo() + "'" +
            ", issuerBank='" + getIssuerBank() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", notes='" + getNotes() + "'" +
            ", invoiceId=" + getInvoiceId() +
            "}";
    }
}
