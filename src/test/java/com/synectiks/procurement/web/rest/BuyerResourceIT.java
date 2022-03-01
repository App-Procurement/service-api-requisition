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

import com.synectiks.procurement.domain.Buyer;
import com.synectiks.procurement.repository.BuyerRepository;

/**
 * Integration tests for the {@link BuyerResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class BuyerResourceIT {

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

  private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
  private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

  private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
  private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

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

  private static final String ENTITY_API_URL = "/api/buyers";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private BuyerRepository buyerRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restBuyerMockMvc;

  private Buyer buyer;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Buyer createEntity(EntityManager em) {
    Buyer buyer = new Buyer()
      .firstName(DEFAULT_FIRST_NAME)
      .middleName(DEFAULT_MIDDLE_NAME)
      .lastName(DEFAULT_LAST_NAME)
      .phoneNumber(DEFAULT_PHONE_NUMBER)
      .email(DEFAULT_EMAIL)
      .address(DEFAULT_ADDRESS)
      .zipCode(DEFAULT_ZIP_CODE)
      .status(DEFAULT_STATUS)
      .createdOn(DEFAULT_CREATED_ON)
      .createdBy(DEFAULT_CREATED_BY)
      .updatedOn(DEFAULT_UPDATED_ON)
      .updatedBy(DEFAULT_UPDATED_BY);
    return buyer;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Buyer createUpdatedEntity(EntityManager em) {
    Buyer buyer = new Buyer()
      .firstName(UPDATED_FIRST_NAME)
      .middleName(UPDATED_MIDDLE_NAME)
      .lastName(UPDATED_LAST_NAME)
      .phoneNumber(UPDATED_PHONE_NUMBER)
      .email(UPDATED_EMAIL)
      .address(UPDATED_ADDRESS)
      .zipCode(UPDATED_ZIP_CODE)
      .status(UPDATED_STATUS)
      .createdOn(UPDATED_CREATED_ON)
      .createdBy(UPDATED_CREATED_BY)
      .updatedOn(UPDATED_UPDATED_ON)
      .updatedBy(UPDATED_UPDATED_BY);
    return buyer;
  }

  @BeforeEach
  public void initTest() {
    buyer = createEntity(em);
  }

  @Test
  @Transactional
  void createBuyer() throws Exception {
    int databaseSizeBeforeCreate = buyerRepository.findAll().size();
    // Create the Buyer
    restBuyerMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buyer)))
      .andExpect(status().isCreated());

    // Validate the Buyer in the database
    List<Buyer> buyerList = buyerRepository.findAll();
    assertThat(buyerList).hasSize(databaseSizeBeforeCreate + 1);
    Buyer testBuyer = buyerList.get(buyerList.size() - 1);
    assertThat(testBuyer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
    assertThat(testBuyer.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
    assertThat(testBuyer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    assertThat(testBuyer.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    assertThat(testBuyer.getEmail()).isEqualTo(DEFAULT_EMAIL);
    assertThat(testBuyer.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    assertThat(testBuyer.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
    assertThat(testBuyer.getStatus()).isEqualTo(DEFAULT_STATUS);
    assertThat(testBuyer.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
    assertThat(testBuyer.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    assertThat(testBuyer.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    assertThat(testBuyer.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
  }

  @Test
  @Transactional
  void createBuyerWithExistingId() throws Exception {
    // Create the Buyer with an existing ID
    buyer.setId(1L);

    int databaseSizeBeforeCreate = buyerRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restBuyerMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buyer)))
      .andExpect(status().isBadRequest());

    // Validate the Buyer in the database
    List<Buyer> buyerList = buyerRepository.findAll();
    assertThat(buyerList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void getAllBuyers() throws Exception {
    // Initialize the database
    buyerRepository.saveAndFlush(buyer);

    // Get all the buyerList
    restBuyerMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(buyer.getId().intValue())))
      .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
      .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
      .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
      .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
      .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
      .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
      .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
      .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
      .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
      .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
      .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
      .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
  }

  @Test
  @Transactional
  void getBuyer() throws Exception {
    // Initialize the database
    buyerRepository.saveAndFlush(buyer);

    // Get the buyer
    restBuyerMockMvc
      .perform(get(ENTITY_API_URL_ID, buyer.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(buyer.getId().intValue()))
      .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
      .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
      .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
      .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
      .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
      .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
      .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
      .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
      .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
      .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
      .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
      .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
  }

  @Test
  @Transactional
  void getNonExistingBuyer() throws Exception {
    // Get the buyer
    restBuyerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewBuyer() throws Exception {
    // Initialize the database
    buyerRepository.saveAndFlush(buyer);

    int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

    // Update the buyer
    Buyer updatedBuyer = buyerRepository.findById(buyer.getId()).get();
    // Disconnect from session so that the updates on updatedBuyer are not directly saved in db
    em.detach(updatedBuyer);
    updatedBuyer
      .firstName(UPDATED_FIRST_NAME)
      .middleName(UPDATED_MIDDLE_NAME)
      .lastName(UPDATED_LAST_NAME)
      .phoneNumber(UPDATED_PHONE_NUMBER)
      .email(UPDATED_EMAIL)
      .address(UPDATED_ADDRESS)
      .zipCode(UPDATED_ZIP_CODE)
      .status(UPDATED_STATUS)
      .createdOn(UPDATED_CREATED_ON)
      .createdBy(UPDATED_CREATED_BY)
      .updatedOn(UPDATED_UPDATED_ON)
      .updatedBy(UPDATED_UPDATED_BY);

    restBuyerMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedBuyer.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedBuyer))
      )
      .andExpect(status().isOk());

    // Validate the Buyer in the database
    List<Buyer> buyerList = buyerRepository.findAll();
    assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
    Buyer testBuyer = buyerList.get(buyerList.size() - 1);
    assertThat(testBuyer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
    assertThat(testBuyer.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
    assertThat(testBuyer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    assertThat(testBuyer.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    assertThat(testBuyer.getEmail()).isEqualTo(UPDATED_EMAIL);
    assertThat(testBuyer.getAddress()).isEqualTo(UPDATED_ADDRESS);
    assertThat(testBuyer.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
    assertThat(testBuyer.getStatus()).isEqualTo(UPDATED_STATUS);
    assertThat(testBuyer.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    assertThat(testBuyer.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testBuyer.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    assertThat(testBuyer.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
  }

  @Test
  @Transactional
  void putNonExistingBuyer() throws Exception {
    int databaseSizeBeforeUpdate = buyerRepository.findAll().size();
    buyer.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restBuyerMockMvc
      .perform(
        put(ENTITY_API_URL_ID, buyer.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buyer))
      )
      .andExpect(status().isBadRequest());

    // Validate the Buyer in the database
    List<Buyer> buyerList = buyerRepository.findAll();
    assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchBuyer() throws Exception {
    int databaseSizeBeforeUpdate = buyerRepository.findAll().size();
    buyer.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restBuyerMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(buyer))
      )
      .andExpect(status().isBadRequest());

    // Validate the Buyer in the database
    List<Buyer> buyerList = buyerRepository.findAll();
    assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamBuyer() throws Exception {
    int databaseSizeBeforeUpdate = buyerRepository.findAll().size();
    buyer.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restBuyerMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buyer)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Buyer in the database
    List<Buyer> buyerList = buyerRepository.findAll();
    assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateBuyerWithPatch() throws Exception {
    // Initialize the database
    buyerRepository.saveAndFlush(buyer);

    int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

    // Update the buyer using partial update
    Buyer partialUpdatedBuyer = new Buyer();
    partialUpdatedBuyer.setId(buyer.getId());

    partialUpdatedBuyer
      .firstName(UPDATED_FIRST_NAME)
      .middleName(UPDATED_MIDDLE_NAME)
      .lastName(UPDATED_LAST_NAME)
      .phoneNumber(UPDATED_PHONE_NUMBER)
      .address(UPDATED_ADDRESS)
      .zipCode(UPDATED_ZIP_CODE)
      .createdOn(UPDATED_CREATED_ON)
      .createdBy(UPDATED_CREATED_BY)
      .updatedOn(UPDATED_UPDATED_ON);

    restBuyerMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedBuyer.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBuyer))
      )
      .andExpect(status().isOk());

    // Validate the Buyer in the database
    List<Buyer> buyerList = buyerRepository.findAll();
    assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
    Buyer testBuyer = buyerList.get(buyerList.size() - 1);
    assertThat(testBuyer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
    assertThat(testBuyer.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
    assertThat(testBuyer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    assertThat(testBuyer.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    assertThat(testBuyer.getEmail()).isEqualTo(DEFAULT_EMAIL);
    assertThat(testBuyer.getAddress()).isEqualTo(UPDATED_ADDRESS);
    assertThat(testBuyer.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
    assertThat(testBuyer.getStatus()).isEqualTo(DEFAULT_STATUS);
    assertThat(testBuyer.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    assertThat(testBuyer.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testBuyer.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    assertThat(testBuyer.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
  }

  @Test
  @Transactional
  void fullUpdateBuyerWithPatch() throws Exception {
    // Initialize the database
    buyerRepository.saveAndFlush(buyer);

    int databaseSizeBeforeUpdate = buyerRepository.findAll().size();

    // Update the buyer using partial update
    Buyer partialUpdatedBuyer = new Buyer();
    partialUpdatedBuyer.setId(buyer.getId());

    partialUpdatedBuyer
      .firstName(UPDATED_FIRST_NAME)
      .middleName(UPDATED_MIDDLE_NAME)
      .lastName(UPDATED_LAST_NAME)
      .phoneNumber(UPDATED_PHONE_NUMBER)
      .email(UPDATED_EMAIL)
      .address(UPDATED_ADDRESS)
      .zipCode(UPDATED_ZIP_CODE)
      .status(UPDATED_STATUS)
      .createdOn(UPDATED_CREATED_ON)
      .createdBy(UPDATED_CREATED_BY)
      .updatedOn(UPDATED_UPDATED_ON)
      .updatedBy(UPDATED_UPDATED_BY);

    restBuyerMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedBuyer.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBuyer))
      )
      .andExpect(status().isOk());

    // Validate the Buyer in the database
    List<Buyer> buyerList = buyerRepository.findAll();
    assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
    Buyer testBuyer = buyerList.get(buyerList.size() - 1);
    assertThat(testBuyer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
    assertThat(testBuyer.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
    assertThat(testBuyer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    assertThat(testBuyer.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    assertThat(testBuyer.getEmail()).isEqualTo(UPDATED_EMAIL);
    assertThat(testBuyer.getAddress()).isEqualTo(UPDATED_ADDRESS);
    assertThat(testBuyer.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
    assertThat(testBuyer.getStatus()).isEqualTo(UPDATED_STATUS);
    assertThat(testBuyer.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    assertThat(testBuyer.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    assertThat(testBuyer.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    assertThat(testBuyer.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
  }

  @Test
  @Transactional
  void patchNonExistingBuyer() throws Exception {
    int databaseSizeBeforeUpdate = buyerRepository.findAll().size();
    buyer.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restBuyerMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, buyer.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(buyer))
      )
      .andExpect(status().isBadRequest());

    // Validate the Buyer in the database
    List<Buyer> buyerList = buyerRepository.findAll();
    assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchBuyer() throws Exception {
    int databaseSizeBeforeUpdate = buyerRepository.findAll().size();
    buyer.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restBuyerMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(buyer))
      )
      .andExpect(status().isBadRequest());

    // Validate the Buyer in the database
    List<Buyer> buyerList = buyerRepository.findAll();
    assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamBuyer() throws Exception {
    int databaseSizeBeforeUpdate = buyerRepository.findAll().size();
    buyer.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restBuyerMockMvc
      .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(buyer)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the Buyer in the database
    List<Buyer> buyerList = buyerRepository.findAll();
    assertThat(buyerList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteBuyer() throws Exception {
    // Initialize the database
    buyerRepository.saveAndFlush(buyer);

    int databaseSizeBeforeDelete = buyerRepository.findAll().size();

    // Delete the buyer
    restBuyerMockMvc.perform(delete(ENTITY_API_URL_ID, buyer.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Buyer> buyerList = buyerRepository.findAll();
    assertThat(buyerList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
