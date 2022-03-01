package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.ContactActivity;
import com.synectiks.procurement.repository.ContactActivityRepository;

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
 * Integration tests for the {@link ContactActivityResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ContactActivityResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_IS_ACTIVE = "AAAAAAAAAA";
    private static final String UPDATED_IS_ACTIVE = "BBBBBBBBBB";

    private static final String DEFAULT_INVITE_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_INVITE_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_INVITATION_LINK = "AAAAAAAAAA";
    private static final String UPDATED_INVITATION_LINK = "BBBBBBBBBB";

    private static final Instant DEFAULT_INVITE_SENT_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INVITE_SENT_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Long DEFAULT_CONTACT_ID = 1L;
    private static final Long UPDATED_CONTACT_ID = 2L;

    @Autowired
    private ContactActivityRepository contactActivityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactActivityMockMvc;

    private ContactActivity contactActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactActivity createEntity(EntityManager em) {
        ContactActivity contactActivity = new ContactActivity()
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .isActive(DEFAULT_IS_ACTIVE)
            .inviteStatus(DEFAULT_INVITE_STATUS)
            .invitationLink(DEFAULT_INVITATION_LINK)
            .inviteSentOn(DEFAULT_INVITE_SENT_ON)
            .designation(DEFAULT_DESIGNATION)
            .jobType(DEFAULT_JOB_TYPE)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .notes(DEFAULT_NOTES)
            .contactId(DEFAULT_CONTACT_ID);
        return contactActivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactActivity createUpdatedEntity(EntityManager em) {
        ContactActivity contactActivity = new ContactActivity()
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .inviteStatus(UPDATED_INVITE_STATUS)
            .invitationLink(UPDATED_INVITATION_LINK)
            .inviteSentOn(UPDATED_INVITE_SENT_ON)
            .designation(UPDATED_DESIGNATION)
            .jobType(UPDATED_JOB_TYPE)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .notes(UPDATED_NOTES)
            .contactId(UPDATED_CONTACT_ID);
        return contactActivity;
    }

    @BeforeEach
    public void initTest() {
        contactActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactActivity() throws Exception {
        int databaseSizeBeforeCreate = contactActivityRepository.findAll().size();
        // Create the ContactActivity
        restContactActivityMockMvc.perform(post("/api/contact-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactActivity)))
            .andExpect(status().isCreated());

        // Validate the ContactActivity in the database
        List<ContactActivity> contactActivityList = contactActivityRepository.findAll();
        assertThat(contactActivityList).hasSize(databaseSizeBeforeCreate + 1);
        ContactActivity testContactActivity = contactActivityList.get(contactActivityList.size() - 1);
        assertThat(testContactActivity.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testContactActivity.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testContactActivity.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testContactActivity.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testContactActivity.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContactActivity.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testContactActivity.getInviteStatus()).isEqualTo(DEFAULT_INVITE_STATUS);
        assertThat(testContactActivity.getInvitationLink()).isEqualTo(DEFAULT_INVITATION_LINK);
        assertThat(testContactActivity.getInviteSentOn()).isEqualTo(DEFAULT_INVITE_SENT_ON);
        assertThat(testContactActivity.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testContactActivity.getJobType()).isEqualTo(DEFAULT_JOB_TYPE);
        assertThat(testContactActivity.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testContactActivity.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testContactActivity.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testContactActivity.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testContactActivity.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testContactActivity.getContactId()).isEqualTo(DEFAULT_CONTACT_ID);
    }

    @Test
    @Transactional
    public void createContactActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactActivityRepository.findAll().size();

        // Create the ContactActivity with an existing ID
        contactActivity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactActivityMockMvc.perform(post("/api/contact-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactActivity)))
            .andExpect(status().isBadRequest());

        // Validate the ContactActivity in the database
        List<ContactActivity> contactActivityList = contactActivityRepository.findAll();
        assertThat(contactActivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllContactActivities() throws Exception {
        // Initialize the database
        contactActivityRepository.saveAndFlush(contactActivity);

        // Get all the contactActivityList
        restContactActivityMockMvc.perform(get("/api/contact-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].inviteStatus").value(hasItem(DEFAULT_INVITE_STATUS)))
            .andExpect(jsonPath("$.[*].invitationLink").value(hasItem(DEFAULT_INVITATION_LINK)))
            .andExpect(jsonPath("$.[*].inviteSentOn").value(hasItem(DEFAULT_INVITE_SENT_ON.toString())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].jobType").value(hasItem(DEFAULT_JOB_TYPE)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].contactId").value(hasItem(DEFAULT_CONTACT_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getContactActivity() throws Exception {
        // Initialize the database
        contactActivityRepository.saveAndFlush(contactActivity);

        // Get the contactActivity
        restContactActivityMockMvc.perform(get("/api/contact-activities/{id}", contactActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactActivity.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.inviteStatus").value(DEFAULT_INVITE_STATUS))
            .andExpect(jsonPath("$.invitationLink").value(DEFAULT_INVITATION_LINK))
            .andExpect(jsonPath("$.inviteSentOn").value(DEFAULT_INVITE_SENT_ON.toString()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.jobType").value(DEFAULT_JOB_TYPE))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.contactId").value(DEFAULT_CONTACT_ID.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingContactActivity() throws Exception {
        // Get the contactActivity
        restContactActivityMockMvc.perform(get("/api/contact-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactActivity() throws Exception {
        // Initialize the database
        contactActivityRepository.saveAndFlush(contactActivity);

        int databaseSizeBeforeUpdate = contactActivityRepository.findAll().size();

        // Update the contactActivity
        ContactActivity updatedContactActivity = contactActivityRepository.findById(contactActivity.getId()).get();
        // Disconnect from session so that the updates on updatedContactActivity are not directly saved in db
        em.detach(updatedContactActivity);
        updatedContactActivity
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .inviteStatus(UPDATED_INVITE_STATUS)
            .invitationLink(UPDATED_INVITATION_LINK)
            .inviteSentOn(UPDATED_INVITE_SENT_ON)
            .designation(UPDATED_DESIGNATION)
            .jobType(UPDATED_JOB_TYPE)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .notes(UPDATED_NOTES)
            .contactId(UPDATED_CONTACT_ID);

        restContactActivityMockMvc.perform(put("/api/contact-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedContactActivity)))
            .andExpect(status().isOk());

        // Validate the ContactActivity in the database
        List<ContactActivity> contactActivityList = contactActivityRepository.findAll();
        assertThat(contactActivityList).hasSize(databaseSizeBeforeUpdate);
        ContactActivity testContactActivity = contactActivityList.get(contactActivityList.size() - 1);
        assertThat(testContactActivity.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testContactActivity.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testContactActivity.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testContactActivity.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testContactActivity.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContactActivity.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testContactActivity.getInviteStatus()).isEqualTo(UPDATED_INVITE_STATUS);
        assertThat(testContactActivity.getInvitationLink()).isEqualTo(UPDATED_INVITATION_LINK);
        assertThat(testContactActivity.getInviteSentOn()).isEqualTo(UPDATED_INVITE_SENT_ON);
        assertThat(testContactActivity.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testContactActivity.getJobType()).isEqualTo(UPDATED_JOB_TYPE);
        assertThat(testContactActivity.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testContactActivity.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testContactActivity.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testContactActivity.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testContactActivity.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testContactActivity.getContactId()).isEqualTo(UPDATED_CONTACT_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingContactActivity() throws Exception {
        int databaseSizeBeforeUpdate = contactActivityRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactActivityMockMvc.perform(put("/api/contact-activities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactActivity)))
            .andExpect(status().isBadRequest());

        // Validate the ContactActivity in the database
        List<ContactActivity> contactActivityList = contactActivityRepository.findAll();
        assertThat(contactActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContactActivity() throws Exception {
        // Initialize the database
        contactActivityRepository.saveAndFlush(contactActivity);

        int databaseSizeBeforeDelete = contactActivityRepository.findAll().size();

        // Delete the contactActivity
        restContactActivityMockMvc.perform(delete("/api/contact-activities/{id}", contactActivity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactActivity> contactActivityList = contactActivityRepository.findAll();
        assertThat(contactActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
