package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.PurchaseOrderDetails;
import com.gksoft.workshop.domain.enumeration.DiscountType;
import com.gksoft.workshop.repository.PurchaseOrderDetailsRepository;
import com.gksoft.workshop.service.PurchaseOrderDetailsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PurchaseOrderDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PurchaseOrderDetailsResourceIT {

    private static final Long DEFAULT_ITEM_NO = 1L;
    private static final Long UPDATED_ITEM_NO = 2L;

    private static final String DEFAULT_ITEM_DESC = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_DESC = "BBBBBBBBBB";

    private static final Long DEFAULT_ITEM_PRICE = 1L;
    private static final Long UPDATED_ITEM_PRICE = 2L;

    private static final Long DEFAULT_ITEM_QTY = 1L;
    private static final Long UPDATED_ITEM_QTY = 2L;

    private static final DiscountType DEFAULT_DISCOUNT_TYPE = DiscountType.PERCENTAGE;
    private static final DiscountType UPDATED_DISCOUNT_TYPE = DiscountType.AMOUNT;

    private static final Long DEFAULT_DISCOUNT = 1L;
    private static final Long UPDATED_DISCOUNT = 2L;

    private static final Long DEFAULT_TOTAL_AMOUNT = 1L;
    private static final Long UPDATED_TOTAL_AMOUNT = 2L;

    private static final String ENTITY_API_URL = "/api/purchase-order-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchaseOrderDetailsRepository purchaseOrderDetailsRepository;

    @Mock
    private PurchaseOrderDetailsRepository purchaseOrderDetailsRepositoryMock;

    @Mock
    private PurchaseOrderDetailsService purchaseOrderDetailsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchaseOrderDetailsMockMvc;

    private PurchaseOrderDetails purchaseOrderDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrderDetails createEntity(EntityManager em) {
        PurchaseOrderDetails purchaseOrderDetails = new PurchaseOrderDetails()
            .itemNo(DEFAULT_ITEM_NO)
            .itemDesc(DEFAULT_ITEM_DESC)
            .itemPrice(DEFAULT_ITEM_PRICE)
            .itemQty(DEFAULT_ITEM_QTY)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .discount(DEFAULT_DISCOUNT)
            .totalAmount(DEFAULT_TOTAL_AMOUNT);
        return purchaseOrderDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrderDetails createUpdatedEntity(EntityManager em) {
        PurchaseOrderDetails purchaseOrderDetails = new PurchaseOrderDetails()
            .itemNo(UPDATED_ITEM_NO)
            .itemDesc(UPDATED_ITEM_DESC)
            .itemPrice(UPDATED_ITEM_PRICE)
            .itemQty(UPDATED_ITEM_QTY)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discount(UPDATED_DISCOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT);
        return purchaseOrderDetails;
    }

    @BeforeEach
    public void initTest() {
        purchaseOrderDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderDetailsRepository.findAll().size();
        // Create the PurchaseOrderDetails
        restPurchaseOrderDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetails))
            )
            .andExpect(status().isCreated());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseOrderDetails testPurchaseOrderDetails = purchaseOrderDetailsList.get(purchaseOrderDetailsList.size() - 1);
        assertThat(testPurchaseOrderDetails.getItemNo()).isEqualTo(DEFAULT_ITEM_NO);
        assertThat(testPurchaseOrderDetails.getItemDesc()).isEqualTo(DEFAULT_ITEM_DESC);
        assertThat(testPurchaseOrderDetails.getItemPrice()).isEqualTo(DEFAULT_ITEM_PRICE);
        assertThat(testPurchaseOrderDetails.getItemQty()).isEqualTo(DEFAULT_ITEM_QTY);
        assertThat(testPurchaseOrderDetails.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testPurchaseOrderDetails.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testPurchaseOrderDetails.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void createPurchaseOrderDetailsWithExistingId() throws Exception {
        // Create the PurchaseOrderDetails with an existing ID
        purchaseOrderDetails.setId(1L);

        int databaseSizeBeforeCreate = purchaseOrderDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseOrderDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get all the purchaseOrderDetailsList
        restPurchaseOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrderDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemNo").value(hasItem(DEFAULT_ITEM_NO.intValue())))
            .andExpect(jsonPath("$.[*].itemDesc").value(hasItem(DEFAULT_ITEM_DESC)))
            .andExpect(jsonPath("$.[*].itemPrice").value(hasItem(DEFAULT_ITEM_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].itemQty").value(hasItem(DEFAULT_ITEM_QTY.intValue())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.intValue())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchaseOrderDetailsWithEagerRelationshipsIsEnabled() throws Exception {
        when(purchaseOrderDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchaseOrderDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(purchaseOrderDetailsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchaseOrderDetailsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(purchaseOrderDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchaseOrderDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(purchaseOrderDetailsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        // Get the purchaseOrderDetails
        restPurchaseOrderDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, purchaseOrderDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseOrderDetails.getId().intValue()))
            .andExpect(jsonPath("$.itemNo").value(DEFAULT_ITEM_NO.intValue()))
            .andExpect(jsonPath("$.itemDesc").value(DEFAULT_ITEM_DESC))
            .andExpect(jsonPath("$.itemPrice").value(DEFAULT_ITEM_PRICE.intValue()))
            .andExpect(jsonPath("$.itemQty").value(DEFAULT_ITEM_QTY.intValue()))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.intValue()))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPurchaseOrderDetails() throws Exception {
        // Get the purchaseOrderDetails
        restPurchaseOrderDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();

        // Update the purchaseOrderDetails
        PurchaseOrderDetails updatedPurchaseOrderDetails = purchaseOrderDetailsRepository.findById(purchaseOrderDetails.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseOrderDetails are not directly saved in db
        em.detach(updatedPurchaseOrderDetails);
        updatedPurchaseOrderDetails
            .itemNo(UPDATED_ITEM_NO)
            .itemDesc(UPDATED_ITEM_DESC)
            .itemPrice(UPDATED_ITEM_PRICE)
            .itemQty(UPDATED_ITEM_QTY)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discount(UPDATED_DISCOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restPurchaseOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPurchaseOrderDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPurchaseOrderDetails))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderDetails testPurchaseOrderDetails = purchaseOrderDetailsList.get(purchaseOrderDetailsList.size() - 1);
        assertThat(testPurchaseOrderDetails.getItemNo()).isEqualTo(UPDATED_ITEM_NO);
        assertThat(testPurchaseOrderDetails.getItemDesc()).isEqualTo(UPDATED_ITEM_DESC);
        assertThat(testPurchaseOrderDetails.getItemPrice()).isEqualTo(UPDATED_ITEM_PRICE);
        assertThat(testPurchaseOrderDetails.getItemQty()).isEqualTo(UPDATED_ITEM_QTY);
        assertThat(testPurchaseOrderDetails.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testPurchaseOrderDetails.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testPurchaseOrderDetails.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();
        purchaseOrderDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseOrderDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();
        purchaseOrderDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();
        purchaseOrderDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchaseOrderDetailsWithPatch() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();

        // Update the purchaseOrderDetails using partial update
        PurchaseOrderDetails partialUpdatedPurchaseOrderDetails = new PurchaseOrderDetails();
        partialUpdatedPurchaseOrderDetails.setId(purchaseOrderDetails.getId());

        partialUpdatedPurchaseOrderDetails.itemDesc(UPDATED_ITEM_DESC).discountType(UPDATED_DISCOUNT_TYPE).discount(UPDATED_DISCOUNT);

        restPurchaseOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrderDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseOrderDetails))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderDetails testPurchaseOrderDetails = purchaseOrderDetailsList.get(purchaseOrderDetailsList.size() - 1);
        assertThat(testPurchaseOrderDetails.getItemNo()).isEqualTo(DEFAULT_ITEM_NO);
        assertThat(testPurchaseOrderDetails.getItemDesc()).isEqualTo(UPDATED_ITEM_DESC);
        assertThat(testPurchaseOrderDetails.getItemPrice()).isEqualTo(DEFAULT_ITEM_PRICE);
        assertThat(testPurchaseOrderDetails.getItemQty()).isEqualTo(DEFAULT_ITEM_QTY);
        assertThat(testPurchaseOrderDetails.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testPurchaseOrderDetails.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testPurchaseOrderDetails.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdatePurchaseOrderDetailsWithPatch() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();

        // Update the purchaseOrderDetails using partial update
        PurchaseOrderDetails partialUpdatedPurchaseOrderDetails = new PurchaseOrderDetails();
        partialUpdatedPurchaseOrderDetails.setId(purchaseOrderDetails.getId());

        partialUpdatedPurchaseOrderDetails
            .itemNo(UPDATED_ITEM_NO)
            .itemDesc(UPDATED_ITEM_DESC)
            .itemPrice(UPDATED_ITEM_PRICE)
            .itemQty(UPDATED_ITEM_QTY)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discount(UPDATED_DISCOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restPurchaseOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrderDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseOrderDetails))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderDetails testPurchaseOrderDetails = purchaseOrderDetailsList.get(purchaseOrderDetailsList.size() - 1);
        assertThat(testPurchaseOrderDetails.getItemNo()).isEqualTo(UPDATED_ITEM_NO);
        assertThat(testPurchaseOrderDetails.getItemDesc()).isEqualTo(UPDATED_ITEM_DESC);
        assertThat(testPurchaseOrderDetails.getItemPrice()).isEqualTo(UPDATED_ITEM_PRICE);
        assertThat(testPurchaseOrderDetails.getItemQty()).isEqualTo(UPDATED_ITEM_QTY);
        assertThat(testPurchaseOrderDetails.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testPurchaseOrderDetails.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testPurchaseOrderDetails.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();
        purchaseOrderDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchaseOrderDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();
        purchaseOrderDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchaseOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderDetailsRepository.findAll().size();
        purchaseOrderDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrderDetails in the database
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchaseOrderDetails() throws Exception {
        // Initialize the database
        purchaseOrderDetailsRepository.saveAndFlush(purchaseOrderDetails);

        int databaseSizeBeforeDelete = purchaseOrderDetailsRepository.findAll().size();

        // Delete the purchaseOrderDetails
        restPurchaseOrderDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchaseOrderDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseOrderDetails> purchaseOrderDetailsList = purchaseOrderDetailsRepository.findAll();
        assertThat(purchaseOrderDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
