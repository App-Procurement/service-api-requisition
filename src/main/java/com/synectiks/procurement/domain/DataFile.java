package com.synectiks.procurement.domain;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DataFile.
 */
@Entity
@Table(name = "data_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataFile implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
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

  @Column(name = "cloud_name")
  private String cloudName;

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

  @Column(name = "source_of_origin")
  private String sourceOfOrigin;

  @Column(name = "created_on")
  private Instant createdOn;

  @Column(name = "created_by")
  private String createdBy;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public DataFile id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFileName() {
    return this.fileName;
  }

  public DataFile fileName(String fileName) {
    this.setFileName(fileName);
    return this;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileType() {
    return this.fileType;
  }

  public DataFile fileType(String fileType) {
    this.setFileType(fileType);
    return this;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getFileExt() {
    return this.fileExt;
  }

  public DataFile fileExt(String fileExt) {
    this.setFileExt(fileExt);
    return this;
  }

  public void setFileExt(String fileExt) {
    this.fileExt = fileExt;
  }

  public Long getFileSize() {
    return this.fileSize;
  }

  public DataFile fileSize(Long fileSize) {
    this.setFileSize(fileSize);
    return this;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  public String getStorageLocation() {
    return this.storageLocation;
  }

  public DataFile storageLocation(String storageLocation) {
    this.setStorageLocation(storageLocation);
    return this;
  }

  public void setStorageLocation(String storageLocation) {
    this.storageLocation = storageLocation;
  }

  public String getCloudName() {
    return this.cloudName;
  }

  public DataFile cloudName(String cloudName) {
    this.setCloudName(cloudName);
    return this;
  }

  public void setCloudName(String cloudName) {
    this.cloudName = cloudName;
  }

  public String gets3Bucket() {
    return this.s3Bucket;
  }

  public DataFile s3Bucket(String s3Bucket) {
    this.sets3Bucket(s3Bucket);
    return this;
  }

  public void sets3Bucket(String s3Bucket) {
    this.s3Bucket = s3Bucket;
  }

  public String gets3Url() {
    return this.s3Url;
  }

  public DataFile s3Url(String s3Url) {
    this.sets3Url(s3Url);
    return this;
  }

  public void sets3Url(String s3Url) {
    this.s3Url = s3Url;
  }

  public String getSourceOfOrigin() {
    return this.sourceOfOrigin;
  }

  public DataFile sourceOfOrigin(String sourceOfOrigin) {
    this.setSourceOfOrigin(sourceOfOrigin);
    return this;
  }

  public void setSourceOfOrigin(String sourceOfOrigin) {
    this.sourceOfOrigin = sourceOfOrigin;
  }

  public Instant getCreatedOn() {
    return this.createdOn;
  }

  public DataFile createdOn(Instant createdOn) {
    this.setCreatedOn(createdOn);
    return this;
  }

  public void setCreatedOn(Instant createdOn) {
    this.createdOn = createdOn;
  }

  public String getCreatedBy() {
    return this.createdBy;
  }

  public DataFile createdBy(String createdBy) {
    this.setCreatedBy(createdBy);
    return this;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DataFile)) {
      return false;
    }
    return id != null && id.equals(((DataFile) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "DataFile{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", fileExt='" + getFileExt() + "'" +
            ", fileSize=" + getFileSize() +
            ", storageLocation='" + getStorageLocation() + "'" +
            ", cloudName='" + getCloudName() + "'" +
            ", s3Bucket='" + gets3Bucket() + "'" +
            ", s3Url='" + gets3Url() + "'" +
            ", sourceOfOrigin='" + getSourceOfOrigin() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
