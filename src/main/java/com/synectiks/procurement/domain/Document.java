package com.synectiks.procurement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_ext")
    private String fileExt;

    @Column(name = "file_size")
    private Long fileSize;

    /**
     * LOCAL or S3 or AZURE
     */
    @ApiModelProperty(value = "LOCAL or S3 or AZURE")
    @Column(name = "storage_location")
    private String storageLocation;

    @Column(name = "local_file_path")
    private String localFilePath;

    /**
     * S3 bucket name. image file stored on aws s3 bucket
     */
    @ApiModelProperty(value = "S3 bucket name. image file stored on aws s3 bucket")
    @Column(name = "s_3_bucket")
    private String s3Bucket;

    /**
     * S3 url of image file
     */
    @ApiModelProperty(value = "S3 url of image file")
    @Column(name = "s_3_url")
    private String s3Url;

    /**
     * AZURE url of image file
     */
    @ApiModelProperty(value = "AZURE url of image file")
    @Column(name = "azure_url")
    private String azureUrl;

    @Column(name = "source_of_origin")
    private String sourceOfOrigin;

    /**
     * DB id of an object like invoice, committee member id etc..
     */
    @ApiModelProperty(value = "DB id of an object like invoice, committee member id etc..")
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * PROFILE_IMAGE, DEGREE, CURRENCY_SYMBOL etc..
     */
    @ApiModelProperty(value = "PROFILE_IMAGE, DEGREE, CURRENCY_SYMBOL etc..")
    @Column(name = "identifier")
    private String identifier;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = "documentLists", allowSetters = true)
    private RequisitionLineItem requisitionLineItem;

    @ManyToOne
    @JsonIgnoreProperties(value = "documentLists", allowSetters = true)
    private Contact contact;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public Document fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public Document fileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileExt() {
        return fileExt;
    }

    public Document fileExt(String fileExt) {
        this.fileExt = fileExt;
        return this;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public Document fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public Document storageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
        return this;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public Document localFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
        return this;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String gets3Bucket() {
        return s3Bucket;
    }

    public Document s3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
        return this;
    }

    public void sets3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String gets3Url() {
        return s3Url;
    }

    public Document s3Url(String s3Url) {
        this.s3Url = s3Url;
        return this;
    }

    public void sets3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public String getAzureUrl() {
        return azureUrl;
    }

    public Document azureUrl(String azureUrl) {
        this.azureUrl = azureUrl;
        return this;
    }

    public void setAzureUrl(String azureUrl) {
        this.azureUrl = azureUrl;
    }

    public String getSourceOfOrigin() {
        return sourceOfOrigin;
    }

    public Document sourceOfOrigin(String sourceOfOrigin) {
        this.sourceOfOrigin = sourceOfOrigin;
        return this;
    }

    public void setSourceOfOrigin(String sourceOfOrigin) {
        this.sourceOfOrigin = sourceOfOrigin;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Document sourceId(Long sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Document identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Document createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Document createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public Document updatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Document updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public RequisitionLineItem getRequisitionLineItem() {
        return requisitionLineItem;
    }

    public Document requisitionLineItem(RequisitionLineItem requisitionLineItem) {
        this.requisitionLineItem = requisitionLineItem;
        return this;
    }

    public void setRequisitionLineItem(RequisitionLineItem requisitionLineItem) {
        this.requisitionLineItem = requisitionLineItem;
    }

    public Contact getContact() {
        return contact;
    }

    public Document contact(Contact contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        return id != null && id.equals(((Document) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", fileExt='" + getFileExt() + "'" +
            ", fileSize=" + getFileSize() +
            ", storageLocation='" + getStorageLocation() + "'" +
            ", localFilePath='" + getLocalFilePath() + "'" +
            ", s3Bucket='" + gets3Bucket() + "'" +
            ", s3Url='" + gets3Url() + "'" +
            ", azureUrl='" + getAzureUrl() + "'" +
            ", sourceOfOrigin='" + getSourceOfOrigin() + "'" +
            ", sourceId=" + getSourceId() +
            ", identifier='" + getIdentifier() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
