package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.Rules;
import com.synectiks.procurement.repository.RulesRepository;

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
 * Integration tests for the {@link RulesResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RulesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_RULE = "AAAAAAAAAA";
    private static final String UPDATED_RULE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    @Autowired
    private RulesRepository rulesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRulesMockMvc;

    private Rules rules;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rules createEntity(EntityManager em) {
        Rules rules = new Rules()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .rule(DEFAULT_RULE)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY);
        return rules;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rules createUpdatedEntity(EntityManager em) {
        Rules rules = new Rules()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .rule(UPDATED_RULE)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY);
        return rules;
    }

    @BeforeEach
    public void initTest() {
        rules = createEntity(em);
    }

    @Test
    @Transactional
    public void createRules() throws Exception {
        int databaseSizeBeforeCreate = rulesRepository.findAll().size();
        // Create the Rules
        restRulesMockMvc.perform(post("/api/rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rules)))
            .andExpect(status().isCreated());

        // Validate the Rules in the database
        List<Rules> rulesList = rulesRepository.findAll();
        assertThat(rulesList).hasSize(databaseSizeBeforeCreate + 1);
        Rules testRules = rulesList.get(rulesList.size() - 1);
        assertThat(testRules.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRules.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRules.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRules.getRule()).isEqualTo(DEFAULT_RULE);
        assertThat(testRules.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRules.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRules.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testRules.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    public void createRulesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rulesRepository.findAll().size();

        // Create the Rules with an existing ID
        rules.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRulesMockMvc.perform(post("/api/rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rules)))
            .andExpect(status().isBadRequest());

        // Validate the Rules in the database
        List<Rules> rulesList = rulesRepository.findAll();
        assertThat(rulesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRules() throws Exception {
        // Initialize the database
        rulesRepository.saveAndFlush(rules);

        // Get all the rulesList
        restRulesMockMvc.perform(get("/api/rules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rules.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].rule").value(hasItem(DEFAULT_RULE)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }
    
    @Test
    @Transactional
    public void getRules() throws Exception {
        // Initialize the database
        rulesRepository.saveAndFlush(rules);

        // Get the rules
        restRulesMockMvc.perform(get("/api/rules/{id}", rules.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rules.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.rule").value(DEFAULT_RULE))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }
    @Test
    @Transactional
    public void getNonExistingRules() throws Exception {
        // Get the rules
        restRulesMockMvc.perform(get("/api/rules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRules() throws Exception {
        // Initialize the database
        rulesRepository.saveAndFlush(rules);

        int databaseSizeBeforeUpdate = rulesRepository.findAll().size();

        // Update the rules
        Rules updatedRules = rulesRepository.findById(rules.getId()).get();
        // Disconnect from session so that the updates on updatedRules are not directly saved in db
        em.detach(updatedRules);
        updatedRules
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .rule(UPDATED_RULE)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY);

        restRulesMockMvc.perform(put("/api/rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRules)))
            .andExpect(status().isOk());

        // Validate the Rules in the database
        List<Rules> rulesList = rulesRepository.findAll();
        assertThat(rulesList).hasSize(databaseSizeBeforeUpdate);
        Rules testRules = rulesList.get(rulesList.size() - 1);
        assertThat(testRules.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRules.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRules.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRules.getRule()).isEqualTo(UPDATED_RULE);
        assertThat(testRules.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRules.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRules.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRules.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingRules() throws Exception {
        int databaseSizeBeforeUpdate = rulesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRulesMockMvc.perform(put("/api/rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rules)))
            .andExpect(status().isBadRequest());

        // Validate the Rules in the database
        List<Rules> rulesList = rulesRepository.findAll();
        assertThat(rulesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRules() throws Exception {
        // Initialize the database
        rulesRepository.saveAndFlush(rules);

        int databaseSizeBeforeDelete = rulesRepository.findAll().size();

        // Delete the rules
        restRulesMockMvc.perform(delete("/api/rules/{id}", rules.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rules> rulesList = rulesRepository.findAll();
        assertThat(rulesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
