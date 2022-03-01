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

import com.synectiks.procurement.domain.Roles;
import com.synectiks.procurement.repository.RolesRepository;

/**
 * Integration tests for the {@link RolesResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class RolesResourceIT {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

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

  private static final Long DEFAULT_SECURITYSERVICE_ID = 1L;
  private static final Long UPDATED_SECURITYSERVICE_ID = 2L;

  private static final String ENTITY_API_URL = "/api/roles";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private RolesRepository rolesRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restRolesMockMvc;

  private Roles roles;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Roles createEntity(EntityManager em) {
    Roles roles = new Roles()
      .name(DEFAULT_NAME)
      .description(DEFAULT_DESCRIPTION)
      .status(DEFAULT_STATUS)
      .createdOn(DEFAULT_CREATED_ON)
      .createdBy(DEFAULT_CREATED_BY)
      .updatedOn(DEFAULT_UPDATED_ON)
      .updatedBy(DEFAULT_UPDATED_BY)
      .securityserviceId(DEFAULT_SECURITYSERVICE_ID);
    return roles;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Roles createUpdatedEntity(EntityManager em) {
    Roles roles = new Roles()
      .name(UPDATED_NAME)
      .description(UPDATED_DESCRIPTION)
      .status(UPDATED_STATUS)
      .createdOn(UPDATED_CREATED_ON)
      .createdBy(UPDATED_CREATED_BY)
      .updatedOn(UPDATED_UPDATED_ON)
      .updatedBy(UPDATED_UPDATED_BY)
      .securityserviceId(UPDATED_SECURITYSERVICE_ID);
    return roles;
  }

  @BeforeEach
  public void initTest() {
    roles = createEntity(em);
  }

  @Test
  @Transactional
  void createRoles() throws Exception {
    int databaseSizeBeforeCreate = rolesRepository.findAll().size();
    // Create the Roles
    restRolesMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roles)))
      .andExpect(status().isCreated());

    // Validate the Roles in the database
    List<Roles> rolesList = rolesRepository.findAll();
    assertThat(rolesList).hasSize(databaseSizeBeforeCreate + 1);
    Roles testRoles = rolesList.get(rolesList.size() - 1);
    assertThat(testRoles.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testRoles.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testRoles.getStatus()).isEqualTo(DEFAULT_STATUS);
    assertThat(testRoles.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
    assertThat(testRoles.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    assertThat(testRoles.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    assertThat(testRoles.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    assertThat(testRoles.getSecurityserviceId()).isEqualTo(DEFAULT_SECURITYSERVICE_ID);
  }

  @Test
  @Transactional
  void createRolesWithExistingId() throws Exception {
    // Create the Roles with an existing ID
    roles.setId(1L);

    int databaseSizeBeforeCreate = rolesRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restRolesMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roles)))
      .andExpect(status().isBadRequest());

    // Validate the Roles in the database
    List<Roles> rolesList = rolesRepository.findAll();
    assertThat(rolesList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void getAllRoles() throws Exception {
    // Initialize the database
    rolesRepository.saveAndFlush(roles);

    // Get all the rolesList
    restRolesMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(roles.getId().intValue())))
      .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
      .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
      .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
      .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
      .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
      .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
      .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
      .andExpect(jsonPath("$.[*].securityserviceId").value(hasItem(DEFAULT_SECURITYSERVICE_ID.intValue())));
  }

  @Test
  @Transactional
  void getRoles() throws Exception {
    // Initialize the database
    rolesRepository.saveAndFlush(roles);

    // Get the roles
    restRolesMockMvc
      .perform(get(ENTITY_API_URL_ID, roles.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(roles.getId().intValue()))
      .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
      .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
      .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
      .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
      .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
      .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
      .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
      .andExpect(jsonPath("$.securityserviceId").value(DEFAULT_SECURITYSERVICE_ID.intValue()));
  }

  @Test
  @Transactional
  void getNonExistingRoles() throws Exception {
    // Get the roles
    restRolesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewRoles() throws Exception {
    // Initialize the database
    rolesRepository.saveAndFlush(roles);

    int databaseSizeBeforeUpdate = rolesRepository.findAll().size();

    // Update the roles
    Roles updatedRoles = rolesRepository.findById(roles.getId()).get();
    // Disconnect from session so that the updates on updatedRoles are not directly saved in db
    em.detach(updatedRoles);
    updatedRoles
      .name(UPDATED_NAME)
      .description(UPDATED_DESCRIPTION)
      .status(UPDATED_STATUS)
      .createdOn(UPDATED_CREATED_ON)
      .createdBy(UPDATED_CREATED_BY)
      .updatedOn(UPDATED_UPDATED_ON)
      .updatedBy(UPDATED_UPDATED_BY)
      .securityserviceId(UPDATED_SECURITYSERVICE_ID);

    restRolesMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedRoles.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedRoles))
      )
      .andExpect(status().isOk());

    // Validate the Roles in the database
    List<Roles> rolesList = rolesRepository.findAll();
    assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
    Roles testRoles = rolesList.get(rolesList.size() - 1);
    assertThat(testRoles.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testRoles.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testRoles.getStatus()).isEqualTo(UPDATED_STATUS);
    assertThat(testRoles.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    assertThat(testRoles.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testRoles.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    assertThat(testRoles.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    assertThat(testRoles.getSecurityserviceId()).isEqualTo(UPDATED_SECURITYSERVICE_ID);
  }

  @Test
  @Transactional
  void putNonExistingRoles() throws Exception {
    int databaseSizeBeforeUpdate = rolesRepository.findAll().size();
    roles.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restRolesMockMvc
      .perform(
        put(ENTITY_API_URL_ID, roles.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roles))
      )
      .andExpect(status().isBadRequest());

    // Validate the Roles in the database
    List<Roles> rolesList = rolesRepository.findAll();
    assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchRoles() throws Exception {
    int databaseSizeBeforeUpdate = rolesRepository.findAll().size();
    roles.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restRolesMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(roles))
      )
      .andExpect(status().isBadRequest());

    // Validate the Roles in the database
    List<Roles> rolesList = rolesRepository.findAll();
    assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamRoles() throws Exception {
    int databaseSizeBeforeUpdate = rolesRepository.findAll().size();
    roles.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restRolesMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roles)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Roles in the database
    List<Roles> rolesList = rolesRepository.findAll();
    assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateRolesWithPatch() throws Exception {
    // Initialize the database
    rolesRepository.saveAndFlush(roles);

    int databaseSizeBeforeUpdate = rolesRepository.findAll().size();

    // Update the roles using partial update
    Roles partialUpdatedRoles = new Roles();
    partialUpdatedRoles.setId(roles.getId());

    partialUpdatedRoles
      .name(UPDATED_NAME)
      .description(UPDATED_DESCRIPTION)
      .createdBy(UPDATED_CREATED_BY)
      .updatedBy(UPDATED_UPDATED_BY)
      .securityserviceId(UPDATED_SECURITYSERVICE_ID);

    restRolesMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedRoles.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoles))
      )
      .andExpect(status().isOk());

    // Validate the Roles in the database
    List<Roles> rolesList = rolesRepository.findAll();
    assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
    Roles testRoles = rolesList.get(rolesList.size() - 1);
    assertThat(testRoles.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testRoles.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testRoles.getStatus()).isEqualTo(DEFAULT_STATUS);
    assertThat(testRoles.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
    assertThat(testRoles.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testRoles.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    assertThat(testRoles.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    assertThat(testRoles.getSecurityserviceId()).isEqualTo(UPDATED_SECURITYSERVICE_ID);
  }

  @Test
  @Transactional
  void fullUpdateRolesWithPatch() throws Exception {
    // Initialize the database
    rolesRepository.saveAndFlush(roles);

    int databaseSizeBeforeUpdate = rolesRepository.findAll().size();

    // Update the roles using partial update
    Roles partialUpdatedRoles = new Roles();
    partialUpdatedRoles.setId(roles.getId());

    partialUpdatedRoles
      .name(UPDATED_NAME)
      .description(UPDATED_DESCRIPTION)
      .status(UPDATED_STATUS)
      .createdOn(UPDATED_CREATED_ON)
      .createdBy(UPDATED_CREATED_BY)
      .updatedOn(UPDATED_UPDATED_ON)
      .updatedBy(UPDATED_UPDATED_BY)
      .securityserviceId(UPDATED_SECURITYSERVICE_ID);

    restRolesMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedRoles.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoles))
      )
      .andExpect(status().isOk());

    // Validate the Roles in the database
    List<Roles> rolesList = rolesRepository.findAll();
    assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
    Roles testRoles = rolesList.get(rolesList.size() - 1);
    assertThat(testRoles.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testRoles.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testRoles.getStatus()).isEqualTo(UPDATED_STATUS);
    assertThat(testRoles.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    assertThat(testRoles.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testRoles.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    assertThat(testRoles.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    assertThat(testRoles.getSecurityserviceId()).isEqualTo(UPDATED_SECURITYSERVICE_ID);
  }

  @Test
  @Transactional
  void patchNonExistingRoles() throws Exception {
    int databaseSizeBeforeUpdate = rolesRepository.findAll().size();
    roles.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restRolesMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, roles.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(roles))
      )
      .andExpect(status().isBadRequest());

    // Validate the Roles in the database
    List<Roles> rolesList = rolesRepository.findAll();
    assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchRoles() throws Exception {
    int databaseSizeBeforeUpdate = rolesRepository.findAll().size();
    roles.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restRolesMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(roles))
      )
      .andExpect(status().isBadRequest());

    // Validate the Roles in the database
    List<Roles> rolesList = rolesRepository.findAll();
    assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamRoles() throws Exception {
    int databaseSizeBeforeUpdate = rolesRepository.findAll().size();
    roles.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restRolesMockMvc
      .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roles)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Roles in the database
    List<Roles> rolesList = rolesRepository.findAll();
    assertThat(rolesList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteRoles() throws Exception {
    // Initialize the database
    rolesRepository.saveAndFlush(roles);

    int databaseSizeBeforeDelete = rolesRepository.findAll().size();

    // Delete the roles
    restRolesMockMvc.perform(delete(ENTITY_API_URL_ID, roles.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Roles> rolesList = rolesRepository.findAll();
    assertThat(rolesList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
