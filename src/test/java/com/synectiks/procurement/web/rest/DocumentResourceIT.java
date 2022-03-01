package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.repository.DocumentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DocumentResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class DocumentResourceIT {

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

    private static final String DEFAULT_LOCAL_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_BUCKET = "AAAAAAAAAA";
    private static final String UPDATED_S_3_BUCKET = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_URL = "AAAAAAAAAA";
    private static final String UPDATED_S_3_URL = "BBBBBBBBBB";

    private static final String DEFAULT_AZURE_URL = "AAAAAAAAAA";
    private static final String UPDATED_AZURE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_OF_ORIGIN = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_OF_ORIGIN = "BBBBBBBBBB";

    private static final Long DEFAULT_SOURCE_ID = 1L;
    private static final Long UPDATED_SOURCE_ID = 2L;

    private static final String DEFAULT_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIER = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentMockMvc;

    private Document document;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createEntity(EntityManager em) {
        Document document = new Document()
            .fileName(DEFAULT_FILE_NAME)
            .fileType(DEFAULT_FILE_TYPE)
            .fileExt(DEFAULT_FILE_EXT)
            .fileSize(DEFAULT_FILE_SIZE)
            .storageLocation(DEFAULT_STORAGE_LOCATION)
            .localFilePath(DEFAULT_LOCAL_FILE_PATH)
            .s3Bucket(DEFAULT_S_3_BUCKET)
            .s3Url(DEFAULT_S_3_URL)
            .azureUrl(DEFAULT_AZURE_URL)
            .sourceOfOrigin(DEFAULT_SOURCE_OF_ORIGIN)
            .sourceId(DEFAULT_SOURCE_ID)
            .identifier(DEFAULT_IDENTIFIER)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY);
        return document;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createUpdatedEntity(EntityManager em) {
        Document document = new Document()
            .fileName(UPDATED_FILE_NAME)
            .fileType(UPDATED_FILE_TYPE)
            .fileExt(UPDATED_FILE_EXT)
            .fileSize(UPDATED_FILE_SIZE)
            .storageLocation(UPDATED_STORAGE_LOCATION)
            .localFilePath(UPDATED_LOCAL_FILE_PATH)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .s3Url(UPDATED_S_3_URL)
            .azureUrl(UPDATED_AZURE_URL)
            .sourceOfOrigin(UPDATED_SOURCE_OF_ORIGIN)
            .sourceId(UPDATED_SOURCE_ID)
            .identifier(UPDATED_IDENTIFIER)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY);
        return document;
    }

    @BeforeEach
    public void initTest() {
        document = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();
        // Create the Document
        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate + 1);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testDocument.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);
        assertThat(testDocument.getFileExt()).isEqualTo(DEFAULT_FILE_EXT);
        assertThat(testDocument.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testDocument.getStorageLocation()).isEqualTo(DEFAULT_STORAGE_LOCATION);
        assertThat(testDocument.getLocalFilePath()).isEqualTo(DEFAULT_LOCAL_FILE_PATH);
        assertThat(testDocument.gets3Bucket()).isEqualTo(DEFAULT_S_3_BUCKET);
        assertThat(testDocument.gets3Url()).isEqualTo(DEFAULT_S_3_URL);
        assertThat(testDocument.getAzureUrl()).isEqualTo(DEFAULT_AZURE_URL);
        assertThat(testDocument.getSourceOfOrigin()).isEqualTo(DEFAULT_SOURCE_OF_ORIGIN);
        assertThat(testDocument.getSourceId()).isEqualTo(DEFAULT_SOURCE_ID);
        assertThat(testDocument.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testDocument.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testDocument.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDocument.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testDocument.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createDocumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document with an existing ID
        document.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDocuments() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE)))
            .andExpect(jsonPath("$.[*].fileExt").value(hasItem(DEFAULT_FILE_EXT)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].storageLocation").value(hasItem(DEFAULT_STORAGE_LOCATION)))
            .andExpect(jsonPath("$.[*].localFilePath").value(hasItem(DEFAULT_LOCAL_FILE_PATH)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].s3Url").value(hasItem(DEFAULT_S_3_URL)))
            .andExpect(jsonPath("$.[*].azureUrl").value(hasItem(DEFAULT_AZURE_URL)))
            .andExpect(jsonPath("$.[*].sourceOfOrigin").value(hasItem(DEFAULT_SOURCE_OF_ORIGIN)))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.intValue())))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }
    
    @Test
    @Transactional
    public void getDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(document.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE))
            .andExpect(jsonPath("$.fileExt").value(DEFAULT_FILE_EXT))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.storageLocation").value(DEFAULT_STORAGE_LOCATION))
            .andExpect(jsonPath("$.localFilePath").value(DEFAULT_LOCAL_FILE_PATH))
            .andExpect(jsonPath("$.s3Bucket").value(DEFAULT_S_3_BUCKET))
            .andExpect(jsonPath("$.s3Url").value(DEFAULT_S_3_URL))
            .andExpect(jsonPath("$.azureUrl").value(DEFAULT_AZURE_URL))
            .andExpect(jsonPath("$.sourceOfOrigin").value(DEFAULT_SOURCE_OF_ORIGIN))
            .andExpect(jsonPath("$.sourceId").value(DEFAULT_SOURCE_ID.intValue()))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }
    @Test
    @Transactional
    public void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document
        Document updatedDocument = documentRepository.findById(document.getId()).get();
        // Disconnect from session so that the updates on updatedDocument are not directly saved in db
        em.detach(updatedDocument);
        updatedDocument
            .fileName(UPDATED_FILE_NAME)
            .fileType(UPDATED_FILE_TYPE)
            .fileExt(UPDATED_FILE_EXT)
            .fileSize(UPDATED_FILE_SIZE)
            .storageLocation(UPDATED_STORAGE_LOCATION)
            .localFilePath(UPDATED_LOCAL_FILE_PATH)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .s3Url(UPDATED_S_3_URL)
            .azureUrl(UPDATED_AZURE_URL)
            .sourceOfOrigin(UPDATED_SOURCE_OF_ORIGIN)
            .sourceId(UPDATED_SOURCE_ID)
            .identifier(UPDATED_IDENTIFIER)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY);

        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocument)))
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testDocument.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
        assertThat(testDocument.getFileExt()).isEqualTo(UPDATED_FILE_EXT);
        assertThat(testDocument.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testDocument.getStorageLocation()).isEqualTo(UPDATED_STORAGE_LOCATION);
        assertThat(testDocument.getLocalFilePath()).isEqualTo(UPDATED_LOCAL_FILE_PATH);
        assertThat(testDocument.gets3Bucket()).isEqualTo(UPDATED_S_3_BUCKET);
        assertThat(testDocument.gets3Url()).isEqualTo(UPDATED_S_3_URL);
        assertThat(testDocument.getAzureUrl()).isEqualTo(UPDATED_AZURE_URL);
        assertThat(testDocument.getSourceOfOrigin()).isEqualTo(UPDATED_SOURCE_OF_ORIGIN);
        assertThat(testDocument.getSourceId()).isEqualTo(UPDATED_SOURCE_ID);
        assertThat(testDocument.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testDocument.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testDocument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDocument.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testDocument.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeDelete = documentRepository.findAll().size();

        // Delete the document
        restDocumentMockMvc.perform(delete("/api/documents/{id}", document.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
