package com.synectiks.procurement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.synectiks.procurement.domain.DataFile;
import com.synectiks.procurement.repository.DataFileRepository;

/**
 * Integration tests for the {@link DataFileResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class DataFileResourceIT {

  private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
  private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";
  private static final String UPDATED_FILE_TYPE = "BBBBBBBBBB";

  private static final String DEFAULT_FILE_EXT = "AAAAAAAAAA";
  private static final String UPDATED_FILE_EXT = "BBBBBBBBBB";

  private static final Long DEFAULT_FILE_SIZE = 1L;
  private static final Long UPDATED_FILE_SIZE = 2L;

  private static final String DEFAULT_STORAGE_LOCATION = "AAAAAAAAAA";
  private static final String UPDATED_STORAGE_LOCATION = "BBBBBBBBBB";

  private static final String DEFAULT_CLOUD_NAME = "AAAAAAAAAA";
  private static final String UPDATED_CLOUD_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_S_3_BUCKET = "AAAAAAAAAA";
  private static final String UPDATED_S_3_BUCKET = "BBBBBBBBBB";

  private static final String DEFAULT_S_3_URL = "AAAAAAAAAA";
  private static final String UPDATED_S_3_URL = "BBBBBBBBBB";

  private static final String DEFAULT_SOURCE_OF_ORIGIN = "AAAAAAAAAA";
  private static final String UPDATED_SOURCE_OF_ORIGIN = "BBBBBBBBBB";

  private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
  private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

  private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
  private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

  private static final String ENTITY_API_URL = "/api/data-files";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private DataFileRepository dataFileRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restDataFileMockMvc;

  private DataFile dataFile;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static DataFile createEntity(EntityManager em) {
    DataFile dataFile = new DataFile()
      .fileName(DEFAULT_FILE_NAME)
      .fileType(DEFAULT_FILE_TYPE)
      .fileExt(DEFAULT_FILE_EXT)
      .fileSize(DEFAULT_FILE_SIZE)
      .storageLocation(DEFAULT_STORAGE_LOCATION)
      .cloudName(DEFAULT_CLOUD_NAME)
      .s3Bucket(DEFAULT_S_3_BUCKET)
      .s3Url(DEFAULT_S_3_URL)
      .sourceOfOrigin(DEFAULT_SOURCE_OF_ORIGIN)
      .createdOn(DEFAULT_CREATED_ON)
      .createdBy(DEFAULT_CREATED_BY);
    return dataFile;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static DataFile createUpdatedEntity(EntityManager em) {
    DataFile dataFile = new DataFile()
      .fileName(UPDATED_FILE_NAME)
      .fileType(UPDATED_FILE_TYPE)
      .fileExt(UPDATED_FILE_EXT)
      .fileSize(UPDATED_FILE_SIZE)
      .storageLocation(UPDATED_STORAGE_LOCATION)
      .cloudName(UPDATED_CLOUD_NAME)
      .s3Bucket(UPDATED_S_3_BUCKET)
      .s3Url(UPDATED_S_3_URL)
      .sourceOfOrigin(UPDATED_SOURCE_OF_ORIGIN)
      .createdOn(UPDATED_CREATED_ON)
      .createdBy(UPDATED_CREATED_BY);
    return dataFile;
  }

  @BeforeEach
  public void initTest() {
    dataFile = createEntity(em);
  }

  @Test
  @Transactional
  void createDataFile() throws Exception {
    int databaseSizeBeforeCreate = dataFileRepository.findAll().size();
    // Create the DataFile
    restDataFileMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFile)))
      .andExpect(status().isCreated());

    // Validate the DataFile in the database
    List<DataFile> dataFileList = dataFileRepository.findAll();
    assertThat(dataFileList).hasSize(databaseSizeBeforeCreate + 1);
    DataFile testDataFile = dataFileList.get(dataFileList.size() - 1);
    assertThat(testDataFile.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
    assertThat(testDataFile.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);
    assertThat(testDataFile.getFileExt()).isEqualTo(DEFAULT_FILE_EXT);
    assertThat(testDataFile.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
    assertThat(testDataFile.getStorageLocation()).isEqualTo(DEFAULT_STORAGE_LOCATION);
    assertThat(testDataFile.getCloudName()).isEqualTo(DEFAULT_CLOUD_NAME);
    assertThat(testDataFile.gets3Bucket()).isEqualTo(DEFAULT_S_3_BUCKET);
    assertThat(testDataFile.gets3Url()).isEqualTo(DEFAULT_S_3_URL);
    assertThat(testDataFile.getSourceOfOrigin()).isEqualTo(DEFAULT_SOURCE_OF_ORIGIN);
    assertThat(testDataFile.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
    assertThat(testDataFile.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
  }

  @Test
  @Transactional
  void createDataFileWithExistingId() throws Exception {
    // Create the DataFile with an existing ID
    dataFile.setId(1L);

    int databaseSizeBeforeCreate = dataFileRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restDataFileMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFile)))
      .andExpect(status().isBadRequest());

    // Validate the DataFile in the database
    List<DataFile> dataFileList = dataFileRepository.findAll();
    assertThat(dataFileList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void getAllDataFiles() throws Exception {
    // Initialize the database
    dataFileRepository.saveAndFlush(dataFile);

    // Get all the dataFileList
    restDataFileMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(dataFile.getId().intValue())))
      .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
      .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE)))
      .andExpect(jsonPath("$.[*].fileExt").value(hasItem(DEFAULT_FILE_EXT)))
      .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
      .andExpect(jsonPath("$.[*].storageLocation").value(hasItem(DEFAULT_STORAGE_LOCATION)))
      .andExpect(jsonPath("$.[*].cloudName").value(hasItem(DEFAULT_CLOUD_NAME)))
      .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
      .andExpect(jsonPath("$.[*].s3Url").value(hasItem(DEFAULT_S_3_URL)))
      .andExpect(jsonPath("$.[*].sourceOfOrigin").value(hasItem(DEFAULT_SOURCE_OF_ORIGIN)))
      .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
      .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
  }

  @Test
  @Transactional
  void getDataFile() throws Exception {
    // Initialize the database
    dataFileRepository.saveAndFlush(dataFile);

    // Get the dataFile
    restDataFileMockMvc
      .perform(get(ENTITY_API_URL_ID, dataFile.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(dataFile.getId().intValue()))
      .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
      .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE))
      .andExpect(jsonPath("$.fileExt").value(DEFAULT_FILE_EXT))
      .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
      .andExpect(jsonPath("$.storageLocation").value(DEFAULT_STORAGE_LOCATION))
      .andExpect(jsonPath("$.cloudName").value(DEFAULT_CLOUD_NAME))
      .andExpect(jsonPath("$.s3Bucket").value(DEFAULT_S_3_BUCKET))
      .andExpect(jsonPath("$.s3Url").value(DEFAULT_S_3_URL))
      .andExpect(jsonPath("$.sourceOfOrigin").value(DEFAULT_SOURCE_OF_ORIGIN))
      .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
      .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
  }

  @Test
  @Transactional
  void getNonExistingDataFile() throws Exception {
    // Get the dataFile
    restDataFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewDataFile() throws Exception {
    // Initialize the database
    dataFileRepository.saveAndFlush(dataFile);

    int databaseSizeBeforeUpdate = dataFileRepository.findAll().size();

    // Update the dataFile
    DataFile updatedDataFile = dataFileRepository.findById(dataFile.getId()).get();
    // Disconnect from session so that the updates on updatedDataFile are not directly saved in db
    em.detach(updatedDataFile);
    updatedDataFile
      .fileName(UPDATED_FILE_NAME)
      .fileType(UPDATED_FILE_TYPE)
      .fileExt(UPDATED_FILE_EXT)
      .fileSize(UPDATED_FILE_SIZE)
      .storageLocation(UPDATED_STORAGE_LOCATION)
      .cloudName(UPDATED_CLOUD_NAME)
      .s3Bucket(UPDATED_S_3_BUCKET)
      .s3Url(UPDATED_S_3_URL)
      .sourceOfOrigin(UPDATED_SOURCE_OF_ORIGIN)
      .createdOn(UPDATED_CREATED_ON)
      .createdBy(UPDATED_CREATED_BY);

    restDataFileMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedDataFile.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedDataFile))
      )
      .andExpect(status().isOk());

    // Validate the DataFile in the database
    List<DataFile> dataFileList = dataFileRepository.findAll();
    assertThat(dataFileList).hasSize(databaseSizeBeforeUpdate);
    DataFile testDataFile = dataFileList.get(dataFileList.size() - 1);
    assertThat(testDataFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
    assertThat(testDataFile.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
    assertThat(testDataFile.getFileExt()).isEqualTo(UPDATED_FILE_EXT);
    assertThat(testDataFile.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
    assertThat(testDataFile.getStorageLocation()).isEqualTo(UPDATED_STORAGE_LOCATION);
    assertThat(testDataFile.getCloudName()).isEqualTo(UPDATED_CLOUD_NAME);
    assertThat(testDataFile.gets3Bucket()).isEqualTo(UPDATED_S_3_BUCKET);
    assertThat(testDataFile.gets3Url()).isEqualTo(UPDATED_S_3_URL);
    assertThat(testDataFile.getSourceOfOrigin()).isEqualTo(UPDATED_SOURCE_OF_ORIGIN);
    assertThat(testDataFile.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    assertThat(testDataFile.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
  }

  @Test
  @Transactional
  void putNonExistingDataFile() throws Exception {
    int databaseSizeBeforeUpdate = dataFileRepository.findAll().size();
    dataFile.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restDataFileMockMvc
      .perform(
        put(ENTITY_API_URL_ID, dataFile.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(dataFile))
      )
      .andExpect(status().isBadRequest());

    // Validate the DataFile in the database
    List<DataFile> dataFileList = dataFileRepository.findAll();
    assertThat(dataFileList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchDataFile() throws Exception {
    int databaseSizeBeforeUpdate = dataFileRepository.findAll().size();
    dataFile.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restDataFileMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(dataFile))
      )
      .andExpect(status().isBadRequest());

    // Validate the DataFile in the database
    List<DataFile> dataFileList = dataFileRepository.findAll();
    assertThat(dataFileList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamDataFile() throws Exception {
    int databaseSizeBeforeUpdate = dataFileRepository.findAll().size();
    dataFile.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restDataFileMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataFile)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the DataFile in the database
    List<DataFile> dataFileList = dataFileRepository.findAll();
    assertThat(dataFileList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateDataFileWithPatch() throws Exception {
    // Initialize the database
    dataFileRepository.saveAndFlush(dataFile);

    int databaseSizeBeforeUpdate = dataFileRepository.findAll().size();

    // Update the dataFile using partial update
    DataFile partialUpdatedDataFile = new DataFile();
    partialUpdatedDataFile.setId(dataFile.getId());

    partialUpdatedDataFile
      .fileName(UPDATED_FILE_NAME)
      .fileType(UPDATED_FILE_TYPE)
      .fileExt(UPDATED_FILE_EXT)
      .fileSize(UPDATED_FILE_SIZE)
      .s3Url(UPDATED_S_3_URL)
      .sourceOfOrigin(UPDATED_SOURCE_OF_ORIGIN)
      .createdBy(UPDATED_CREATED_BY);

    restDataFileMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedDataFile.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataFile))
      )
      .andExpect(status().isOk());

    // Validate the DataFile in the database
    List<DataFile> dataFileList = dataFileRepository.findAll();
    assertThat(dataFileList).hasSize(databaseSizeBeforeUpdate);
    DataFile testDataFile = dataFileList.get(dataFileList.size() - 1);
    assertThat(testDataFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
    assertThat(testDataFile.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
    assertThat(testDataFile.getFileExt()).isEqualTo(UPDATED_FILE_EXT);
    assertThat(testDataFile.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
    assertThat(testDataFile.getStorageLocation()).isEqualTo(DEFAULT_STORAGE_LOCATION);
    assertThat(testDataFile.getCloudName()).isEqualTo(DEFAULT_CLOUD_NAME);
    assertThat(testDataFile.gets3Bucket()).isEqualTo(DEFAULT_S_3_BUCKET);
    assertThat(testDataFile.gets3Url()).isEqualTo(UPDATED_S_3_URL);
    assertThat(testDataFile.getSourceOfOrigin()).isEqualTo(UPDATED_SOURCE_OF_ORIGIN);
    assertThat(testDataFile.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
    assertThat(testDataFile.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
  }

  @Test
  @Transactional
  void fullUpdateDataFileWithPatch() throws Exception {
    // Initialize the database
    dataFileRepository.saveAndFlush(dataFile);

    int databaseSizeBeforeUpdate = dataFileRepository.findAll().size();

    // Update the dataFile using partial update
    DataFile partialUpdatedDataFile = new DataFile();
    partialUpdatedDataFile.setId(dataFile.getId());

    partialUpdatedDataFile
      .fileName(UPDATED_FILE_NAME)
      .fileType(UPDATED_FILE_TYPE)
      .fileExt(UPDATED_FILE_EXT)
      .fileSize(UPDATED_FILE_SIZE)
      .storageLocation(UPDATED_STORAGE_LOCATION)
      .cloudName(UPDATED_CLOUD_NAME)
      .s3Bucket(UPDATED_S_3_BUCKET)
      .s3Url(UPDATED_S_3_URL)
      .sourceOfOrigin(UPDATED_SOURCE_OF_ORIGIN)
      .createdOn(UPDATED_CREATED_ON)
      .createdBy(UPDATED_CREATED_BY);

    restDataFileMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedDataFile.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataFile))
      )
      .andExpect(status().isOk());

    // Validate the DataFile in the database
    List<DataFile> dataFileList = dataFileRepository.findAll();
    assertThat(dataFileList).hasSize(databaseSizeBeforeUpdate);
    DataFile testDataFile = dataFileList.get(dataFileList.size() - 1);
    assertThat(testDataFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
    assertThat(testDataFile.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
    assertThat(testDataFile.getFileExt()).isEqualTo(UPDATED_FILE_EXT);
    assertThat(testDataFile.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
    assertThat(testDataFile.getStorageLocation()).isEqualTo(UPDATED_STORAGE_LOCATION);
    assertThat(testDataFile.getCloudName()).isEqualTo(UPDATED_CLOUD_NAME);
    assertThat(testDataFile.gets3Bucket()).isEqualTo(UPDATED_S_3_BUCKET);
    assertThat(testDataFile.gets3Url()).isEqualTo(UPDATED_S_3_URL);
    assertThat(testDataFile.getSourceOfOrigin()).isEqualTo(UPDATED_SOURCE_OF_ORIGIN);
    assertThat(testDataFile.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    assertThat(testDataFile.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
  }

  @Test
  @Transactional
  void patchNonExistingDataFile() throws Exception {
    int databaseSizeBeforeUpdate = dataFileRepository.findAll().size();
    dataFile.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restDataFileMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, dataFile.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(dataFile))
      )
      .andExpect(status().isBadRequest());

    // Validate the DataFile in the database
    List<DataFile> dataFileList = dataFileRepository.findAll();
    assertThat(dataFileList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchDataFile() throws Exception {
    int databaseSizeBeforeUpdate = dataFileRepository.findAll().size();
    dataFile.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restDataFileMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(dataFile))
      )
      .andExpect(status().isBadRequest());

    // Validate the DataFile in the database
    List<DataFile> dataFileList = dataFileRepository.findAll();
    assertThat(dataFileList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamDataFile() throws Exception {
    int databaseSizeBeforeUpdate = dataFileRepository.findAll().size();
    dataFile.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restDataFileMockMvc
      .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dataFile)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the DataFile in the database
    List<DataFile> dataFileList = dataFileRepository.findAll();
    assertThat(dataFileList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteDataFile() throws Exception {
    // Initialize the database
    dataFileRepository.saveAndFlush(dataFile);

    int databaseSizeBeforeDelete = dataFileRepository.findAll().size();

    // Delete the dataFile
    restDataFileMockMvc
      .perform(delete(ENTITY_API_URL_ID, dataFile.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<DataFile> dataFileList = dataFileRepository.findAll();
    assertThat(dataFileList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
