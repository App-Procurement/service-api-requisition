package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;

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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RequisitionLineItemResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RequisitionLineItemResourceIT {

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_PROGRESS_STAGE = "AAAAAAAAAA";
    private static final String UPDATED_PROGRESS_STAGE = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER_QUANTITY = 1;
    private static final Integer UPDATED_ORDER_QUANTITY = 2;

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    private static final String DEFAULT_PRIORITY = "AAAAAAAAAA";
    private static final String UPDATED_PRIORITY = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_RATE_PER_ITEM = 1;
    private static final Integer UPDATED_RATE_PER_ITEM = 2;

    @Autowired
    private RequisitionLineItemRepository requisitionLineItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequisitionLineItemMockMvc;

    private RequisitionLineItem requisitionLineItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequisitionLineItem createEntity(EntityManager em) {
        RequisitionLineItem requisitionLineItem = new RequisitionLineItem()
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .status(DEFAULT_STATUS)
            .progressStage(DEFAULT_PROGRESS_STAGE)
            .itemDescription(DEFAULT_ITEM_DESCRIPTION)
            .orderQuantity(DEFAULT_ORDER_QUANTITY)
            .price(DEFAULT_PRICE)
            .priority(DEFAULT_PRIORITY)
            .notes(DEFAULT_NOTES)
            .dueDate(DEFAULT_DUE_DATE)
            .ratePerItem(DEFAULT_RATE_PER_ITEM);
        return requisitionLineItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequisitionLineItem createUpdatedEntity(EntityManager em) {
        RequisitionLineItem requisitionLineItem = new RequisitionLineItem()
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .status(UPDATED_STATUS)
            .progressStage(UPDATED_PROGRESS_STAGE)
            .itemDescription(UPDATED_ITEM_DESCRIPTION)
            .orderQuantity(UPDATED_ORDER_QUANTITY)
            .price(UPDATED_PRICE)
            .priority(UPDATED_PRIORITY)
            .notes(UPDATED_NOTES)
            .dueDate(UPDATED_DUE_DATE)
            .ratePerItem(UPDATED_RATE_PER_ITEM);
        return requisitionLineItem;
    }

    @BeforeEach
    public void initTest() {
        requisitionLineItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequisitionLineItem() throws Exception {
        int databaseSizeBeforeCreate = requisitionLineItemRepository.findAll().size();
        // Create the RequisitionLineItem
        restRequisitionLineItemMockMvc.perform(post("/api/requisition-line-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requisitionLineItem)))
            .andExpect(status().isCreated());

        // Validate the RequisitionLineItem in the database
        List<RequisitionLineItem> requisitionLineItemList = requisitionLineItemRepository.findAll();
        assertThat(requisitionLineItemList).hasSize(databaseSizeBeforeCreate + 1);
        RequisitionLineItem testRequisitionLineItem = requisitionLineItemList.get(requisitionLineItemList.size() - 1);
        assertThat(testRequisitionLineItem.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRequisitionLineItem.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRequisitionLineItem.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testRequisitionLineItem.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testRequisitionLineItem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRequisitionLineItem.getProgressStage()).isEqualTo(DEFAULT_PROGRESS_STAGE);
        assertThat(testRequisitionLineItem.getItemDescription()).isEqualTo(DEFAULT_ITEM_DESCRIPTION);
        assertThat(testRequisitionLineItem.getOrderQuantity()).isEqualTo(DEFAULT_ORDER_QUANTITY);
        assertThat(testRequisitionLineItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testRequisitionLineItem.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testRequisitionLineItem.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testRequisitionLineItem.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testRequisitionLineItem.getRatePerItem()).isEqualTo(DEFAULT_RATE_PER_ITEM);
    }

    @Test
    @Transactional
    public void createRequisitionLineItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requisitionLineItemRepository.findAll().size();

        // Create the RequisitionLineItem with an existing ID
        requisitionLineItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequisitionLineItemMockMvc.perform(post("/api/requisition-line-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requisitionLineItem)))
            .andExpect(status().isBadRequest());

        // Validate the RequisitionLineItem in the database
        List<RequisitionLineItem> requisitionLineItemList = requisitionLineItemRepository.findAll();
        assertThat(requisitionLineItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRequisitionLineItems() throws Exception {
        // Initialize the database
        requisitionLineItemRepository.saveAndFlush(requisitionLineItem);

        // Get all the requisitionLineItemList
        restRequisitionLineItemMockMvc.perform(get("/api/requisition-line-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requisitionLineItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].progressStage").value(hasItem(DEFAULT_PROGRESS_STAGE)))
            .andExpect(jsonPath("$.[*].itemDescription").value(hasItem(DEFAULT_ITEM_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].orderQuantity").value(hasItem(DEFAULT_ORDER_QUANTITY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].ratePerItem").value(hasItem(DEFAULT_RATE_PER_ITEM)));
    }
    
    @Test
    @Transactional
    public void getRequisitionLineItem() throws Exception {
        // Initialize the database
        requisitionLineItemRepository.saveAndFlush(requisitionLineItem);

        // Get the requisitionLineItem
        restRequisitionLineItemMockMvc.perform(get("/api/requisition-line-items/{id}", requisitionLineItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requisitionLineItem.getId().intValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.progressStage").value(DEFAULT_PROGRESS_STAGE))
            .andExpect(jsonPath("$.itemDescription").value(DEFAULT_ITEM_DESCRIPTION))
            .andExpect(jsonPath("$.orderQuantity").value(DEFAULT_ORDER_QUANTITY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.ratePerItem").value(DEFAULT_RATE_PER_ITEM));
    }
    @Test
    @Transactional
    public void getNonExistingRequisitionLineItem() throws Exception {
        // Get the requisitionLineItem
        restRequisitionLineItemMockMvc.perform(get("/api/requisition-line-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequisitionLineItem() throws Exception {
        // Initialize the database
        requisitionLineItemRepository.saveAndFlush(requisitionLineItem);

        int databaseSizeBeforeUpdate = requisitionLineItemRepository.findAll().size();

        // Update the requisitionLineItem
        RequisitionLineItem updatedRequisitionLineItem = requisitionLineItemRepository.findById(requisitionLineItem.getId()).get();
        // Disconnect from session so that the updates on updatedRequisitionLineItem are not directly saved in db
        em.detach(updatedRequisitionLineItem);
        updatedRequisitionLineItem
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .status(UPDATED_STATUS)
            .progressStage(UPDATED_PROGRESS_STAGE)
            .itemDescription(UPDATED_ITEM_DESCRIPTION)
            .orderQuantity(UPDATED_ORDER_QUANTITY)
            .price(UPDATED_PRICE)
            .priority(UPDATED_PRIORITY)
            .notes(UPDATED_NOTES)
            .dueDate(UPDATED_DUE_DATE)
            .ratePerItem(UPDATED_RATE_PER_ITEM);

        restRequisitionLineItemMockMvc.perform(put("/api/requisition-line-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequisitionLineItem)))
            .andExpect(status().isOk());

        // Validate the RequisitionLineItem in the database
        List<RequisitionLineItem> requisitionLineItemList = requisitionLineItemRepository.findAll();
        assertThat(requisitionLineItemList).hasSize(databaseSizeBeforeUpdate);
        RequisitionLineItem testRequisitionLineItem = requisitionLineItemList.get(requisitionLineItemList.size() - 1);
        assertThat(testRequisitionLineItem.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRequisitionLineItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRequisitionLineItem.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRequisitionLineItem.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testRequisitionLineItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRequisitionLineItem.getProgressStage()).isEqualTo(UPDATED_PROGRESS_STAGE);
        assertThat(testRequisitionLineItem.getItemDescription()).isEqualTo(UPDATED_ITEM_DESCRIPTION);
        assertThat(testRequisitionLineItem.getOrderQuantity()).isEqualTo(UPDATED_ORDER_QUANTITY);
        assertThat(testRequisitionLineItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testRequisitionLineItem.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testRequisitionLineItem.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testRequisitionLineItem.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testRequisitionLineItem.getRatePerItem()).isEqualTo(UPDATED_RATE_PER_ITEM);
    }

    @Test
    @Transactional
    public void updateNonExistingRequisitionLineItem() throws Exception {
        int databaseSizeBeforeUpdate = requisitionLineItemRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequisitionLineItemMockMvc.perform(put("/api/requisition-line-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requisitionLineItem)))
            .andExpect(status().isBadRequest());

        // Validate the RequisitionLineItem in the database
        List<RequisitionLineItem> requisitionLineItemList = requisitionLineItemRepository.findAll();
        assertThat(requisitionLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRequisitionLineItem() throws Exception {
        // Initialize the database
        requisitionLineItemRepository.saveAndFlush(requisitionLineItem);

        int databaseSizeBeforeDelete = requisitionLineItemRepository.findAll().size();

        // Delete the requisitionLineItem
        restRequisitionLineItemMockMvc.perform(delete("/api/requisition-line-items/{id}", requisitionLineItem.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RequisitionLineItem> requisitionLineItemList = requisitionLineItemRepository.findAll();
        assertThat(requisitionLineItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
