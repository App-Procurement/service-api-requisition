package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.InvoiceActivity;
import com.synectiks.procurement.repository.InvoiceActivityRepository;

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
 * Integration tests for the {@link InvoiceActivityResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InvoiceActivityResourceIT {

    private static final String DEFAULT_INVOICE_NO = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    private static final String DEFAULT_MODE_OF_PAYMENT = "AAAAAAAAAA";
    private static final String UPDATED_MODE_OF_PAYMENT = "BBBBBBBBBB";

    private static final String DEFAULT_TXN_REF_NO = "AAAAAAAAAA";
    private static final String UPDATED_TXN_REF_NO = "BBBBBBBBBB";

    private static final String DEFAULT_CHEQUE_OR_DD_NO = "AAAAAAAAAA";
    private static final String UPDATED_CHEQUE_OR_DD_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUER_BANK = "AAAAAAAAAA";
    private static final String UPDATED_ISSUER_BANK = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Long DEFAULT_INVOICE_ID = 1L;
    private static final Long UPDATED_INVOICE_ID = 2L;

    @Autowired
    private InvoiceActivityRepository invoiceActivityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceActivityMockMvc;

    private InvoiceActivity invoiceActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceActivity createEntity(EntityManager em) {
        InvoiceActivity invoiceActivity = new InvoiceActivity()
            .invoiceNo(DEFAULT_INVOICE_NO)
            .amount(DEFAULT_AMOUNT)
            .modeOfPayment(DEFAULT_MODE_OF_PAYMENT)
            .txnRefNo(DEFAULT_TXN_REF_NO)
            .chequeOrDdNo(DEFAULT_CHEQUE_OR_DD_NO)
            .issuerBank(DEFAULT_ISSUER_BANK)
            .status(DEFAULT_STATUS)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .dueDate(DEFAULT_DUE_DATE)
            .notes(DEFAULT_NOTES)
            .invoiceId(DEFAULT_INVOICE_ID);
        return invoiceActivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceActivity createUpdatedEntity(EntityManager em) {
        InvoiceActivity invoiceActivity = new InvoiceActivity()
            .invoiceNo(UPDATED_INVOICE_NO)
            .amount(UPDATED_AMOUNT)
            .modeOfPayment(UPDATED_MODE_OF_PAYMENT)
            .txnRefNo(UPDATED_TXN_REF_NO)
            .chequeOrDdNo(UPDATED_CHEQUE_OR_DD_NO)
            .issuerBank(UPDATED_ISSUER_BANK)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .dueDate(UPDATED_DUE_DATE)
            .notes(UPDATED_NOTES)
            .invoiceId(UPDATED_INVOICE_ID);
        return invoiceActivity;
    }

    @BeforeEach
    public void initTest() {
        invoiceActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceActivity() throws Exception {
        int databaseSizeBeforeCreate = invoiceActivityRepository.findAll().size();
        // Create the InvoiceActivity
        restInvoiceActivityMockMvc.perform(post("/api/invoice-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invoiceActivity)))
            .andExpect(status().isCreated());

        // Validate the InvoiceActivity in the database
        List<InvoiceActivity> invoiceActivityList = invoiceActivityRepository.findAll();
        assertThat(invoiceActivityList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceActivity testInvoiceActivity = invoiceActivityList.get(invoiceActivityList.size() - 1);
        assertThat(testInvoiceActivity.getInvoiceNo()).isEqualTo(DEFAULT_INVOICE_NO);
        assertThat(testInvoiceActivity.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testInvoiceActivity.getModeOfPayment()).isEqualTo(DEFAULT_MODE_OF_PAYMENT);
        assertThat(testInvoiceActivity.getTxnRefNo()).isEqualTo(DEFAULT_TXN_REF_NO);
        assertThat(testInvoiceActivity.getChequeOrDdNo()).isEqualTo(DEFAULT_CHEQUE_OR_DD_NO);
        assertThat(testInvoiceActivity.getIssuerBank()).isEqualTo(DEFAULT_ISSUER_BANK);
        assertThat(testInvoiceActivity.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testInvoiceActivity.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testInvoiceActivity.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testInvoiceActivity.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testInvoiceActivity.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testInvoiceActivity.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testInvoiceActivity.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testInvoiceActivity.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
    }

    @Test
    @Transactional
    public void createInvoiceActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceActivityRepository.findAll().size();

        // Create the InvoiceActivity with an existing ID
        invoiceActivity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceActivityMockMvc.perform(post("/api/invoice-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invoiceActivity)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceActivity in the database
        List<InvoiceActivity> invoiceActivityList = invoiceActivityRepository.findAll();
        assertThat(invoiceActivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInvoiceActivities() throws Exception {
        // Initialize the database
        invoiceActivityRepository.saveAndFlush(invoiceActivity);

        // Get all the invoiceActivityList
        restInvoiceActivityMockMvc.perform(get("/api/invoice-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNo").value(hasItem(DEFAULT_INVOICE_NO)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].modeOfPayment").value(hasItem(DEFAULT_MODE_OF_PAYMENT)))
            .andExpect(jsonPath("$.[*].txnRefNo").value(hasItem(DEFAULT_TXN_REF_NO)))
            .andExpect(jsonPath("$.[*].chequeOrDdNo").value(hasItem(DEFAULT_CHEQUE_OR_DD_NO)))
            .andExpect(jsonPath("$.[*].issuerBank").value(hasItem(DEFAULT_ISSUER_BANK)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getInvoiceActivity() throws Exception {
        // Initialize the database
        invoiceActivityRepository.saveAndFlush(invoiceActivity);

        // Get the invoiceActivity
        restInvoiceActivityMockMvc.perform(get("/api/invoice-activities/{id}", invoiceActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceActivity.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNo").value(DEFAULT_INVOICE_NO))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.modeOfPayment").value(DEFAULT_MODE_OF_PAYMENT))
            .andExpect(jsonPath("$.txnRefNo").value(DEFAULT_TXN_REF_NO))
            .andExpect(jsonPath("$.chequeOrDdNo").value(DEFAULT_CHEQUE_OR_DD_NO))
            .andExpect(jsonPath("$.issuerBank").value(DEFAULT_ISSUER_BANK))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.invoiceId").value(DEFAULT_INVOICE_ID.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingInvoiceActivity() throws Exception {
        // Get the invoiceActivity
        restInvoiceActivityMockMvc.perform(get("/api/invoice-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceActivity() throws Exception {
        // Initialize the database
        invoiceActivityRepository.saveAndFlush(invoiceActivity);

        int databaseSizeBeforeUpdate = invoiceActivityRepository.findAll().size();

        // Update the invoiceActivity
        InvoiceActivity updatedInvoiceActivity = invoiceActivityRepository.findById(invoiceActivity.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceActivity are not directly saved in db
        em.detach(updatedInvoiceActivity);
        updatedInvoiceActivity
            .invoiceNo(UPDATED_INVOICE_NO)
            .amount(UPDATED_AMOUNT)
            .modeOfPayment(UPDATED_MODE_OF_PAYMENT)
            .txnRefNo(UPDATED_TXN_REF_NO)
            .chequeOrDdNo(UPDATED_CHEQUE_OR_DD_NO)
            .issuerBank(UPDATED_ISSUER_BANK)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .dueDate(UPDATED_DUE_DATE)
            .notes(UPDATED_NOTES)
            .invoiceId(UPDATED_INVOICE_ID);

        restInvoiceActivityMockMvc.perform(put("/api/invoice-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvoiceActivity)))
            .andExpect(status().isOk());

        // Validate the InvoiceActivity in the database
        List<InvoiceActivity> invoiceActivityList = invoiceActivityRepository.findAll();
        assertThat(invoiceActivityList).hasSize(databaseSizeBeforeUpdate);
        InvoiceActivity testInvoiceActivity = invoiceActivityList.get(invoiceActivityList.size() - 1);
        assertThat(testInvoiceActivity.getInvoiceNo()).isEqualTo(UPDATED_INVOICE_NO);
        assertThat(testInvoiceActivity.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInvoiceActivity.getModeOfPayment()).isEqualTo(UPDATED_MODE_OF_PAYMENT);
        assertThat(testInvoiceActivity.getTxnRefNo()).isEqualTo(UPDATED_TXN_REF_NO);
        assertThat(testInvoiceActivity.getChequeOrDdNo()).isEqualTo(UPDATED_CHEQUE_OR_DD_NO);
        assertThat(testInvoiceActivity.getIssuerBank()).isEqualTo(UPDATED_ISSUER_BANK);
        assertThat(testInvoiceActivity.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoiceActivity.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInvoiceActivity.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInvoiceActivity.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testInvoiceActivity.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testInvoiceActivity.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testInvoiceActivity.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testInvoiceActivity.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceActivity() throws Exception {
        int databaseSizeBeforeUpdate = invoiceActivityRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceActivityMockMvc.perform(put("/api/invoice-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invoiceActivity)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceActivity in the database
        List<InvoiceActivity> invoiceActivityList = invoiceActivityRepository.findAll();
        assertThat(invoiceActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvoiceActivity() throws Exception {
        // Initialize the database
        invoiceActivityRepository.saveAndFlush(invoiceActivity);

        int databaseSizeBeforeDelete = invoiceActivityRepository.findAll().size();

        // Delete the invoiceActivity
        restInvoiceActivityMockMvc.perform(delete("/api/invoice-activities/{id}", invoiceActivity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceActivity> invoiceActivityList = invoiceActivityRepository.findAll();
        assertThat(invoiceActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
