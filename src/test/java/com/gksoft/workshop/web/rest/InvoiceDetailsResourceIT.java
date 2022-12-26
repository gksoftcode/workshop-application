package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.InvoiceDetails;
import com.gksoft.workshop.domain.enumeration.DiscountType;
import com.gksoft.workshop.repository.InvoiceDetailsRepository;
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

/**
 * Integration tests for the {@link InvoiceDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceDetailsResourceIT {

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

    private static final String ENTITY_API_URL = "/api/invoice-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceDetailsRepository invoiceDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceDetailsMockMvc;

    private InvoiceDetails invoiceDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceDetails createEntity(EntityManager em) {
        InvoiceDetails invoiceDetails = new InvoiceDetails()
            .itemNo(DEFAULT_ITEM_NO)
            .itemDesc(DEFAULT_ITEM_DESC)
            .itemPrice(DEFAULT_ITEM_PRICE)
            .itemQty(DEFAULT_ITEM_QTY)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .discount(DEFAULT_DISCOUNT)
            .totalAmount(DEFAULT_TOTAL_AMOUNT);
        return invoiceDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceDetails createUpdatedEntity(EntityManager em) {
        InvoiceDetails invoiceDetails = new InvoiceDetails()
            .itemNo(UPDATED_ITEM_NO)
            .itemDesc(UPDATED_ITEM_DESC)
            .itemPrice(UPDATED_ITEM_PRICE)
            .itemQty(UPDATED_ITEM_QTY)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discount(UPDATED_DISCOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT);
        return invoiceDetails;
    }

    @BeforeEach
    public void initTest() {
        invoiceDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoiceDetails() throws Exception {
        int databaseSizeBeforeCreate = invoiceDetailsRepository.findAll().size();
        // Create the InvoiceDetails
        restInvoiceDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDetails))
            )
            .andExpect(status().isCreated());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceDetails testInvoiceDetails = invoiceDetailsList.get(invoiceDetailsList.size() - 1);
        assertThat(testInvoiceDetails.getItemNo()).isEqualTo(DEFAULT_ITEM_NO);
        assertThat(testInvoiceDetails.getItemDesc()).isEqualTo(DEFAULT_ITEM_DESC);
        assertThat(testInvoiceDetails.getItemPrice()).isEqualTo(DEFAULT_ITEM_PRICE);
        assertThat(testInvoiceDetails.getItemQty()).isEqualTo(DEFAULT_ITEM_QTY);
        assertThat(testInvoiceDetails.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testInvoiceDetails.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testInvoiceDetails.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void createInvoiceDetailsWithExistingId() throws Exception {
        // Create the InvoiceDetails with an existing ID
        invoiceDetails.setId(1L);

        int databaseSizeBeforeCreate = invoiceDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvoiceDetails() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get all the invoiceDetailsList
        restInvoiceDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemNo").value(hasItem(DEFAULT_ITEM_NO.intValue())))
            .andExpect(jsonPath("$.[*].itemDesc").value(hasItem(DEFAULT_ITEM_DESC)))
            .andExpect(jsonPath("$.[*].itemPrice").value(hasItem(DEFAULT_ITEM_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].itemQty").value(hasItem(DEFAULT_ITEM_QTY.intValue())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.intValue())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    void getInvoiceDetails() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        // Get the invoiceDetails
        restInvoiceDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceDetails.getId().intValue()))
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
    void getNonExistingInvoiceDetails() throws Exception {
        // Get the invoiceDetails
        restInvoiceDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceDetails() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        int databaseSizeBeforeUpdate = invoiceDetailsRepository.findAll().size();

        // Update the invoiceDetails
        InvoiceDetails updatedInvoiceDetails = invoiceDetailsRepository.findById(invoiceDetails.getId()).get();
        // Disconnect from session so that the updates on updatedInvoiceDetails are not directly saved in db
        em.detach(updatedInvoiceDetails);
        updatedInvoiceDetails
            .itemNo(UPDATED_ITEM_NO)
            .itemDesc(UPDATED_ITEM_DESC)
            .itemPrice(UPDATED_ITEM_PRICE)
            .itemQty(UPDATED_ITEM_QTY)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discount(UPDATED_DISCOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restInvoiceDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoiceDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInvoiceDetails))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeUpdate);
        InvoiceDetails testInvoiceDetails = invoiceDetailsList.get(invoiceDetailsList.size() - 1);
        assertThat(testInvoiceDetails.getItemNo()).isEqualTo(UPDATED_ITEM_NO);
        assertThat(testInvoiceDetails.getItemDesc()).isEqualTo(UPDATED_ITEM_DESC);
        assertThat(testInvoiceDetails.getItemPrice()).isEqualTo(UPDATED_ITEM_PRICE);
        assertThat(testInvoiceDetails.getItemQty()).isEqualTo(UPDATED_ITEM_QTY);
        assertThat(testInvoiceDetails.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testInvoiceDetails.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testInvoiceDetails.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceDetails() throws Exception {
        int databaseSizeBeforeUpdate = invoiceDetailsRepository.findAll().size();
        invoiceDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceDetails() throws Exception {
        int databaseSizeBeforeUpdate = invoiceDetailsRepository.findAll().size();
        invoiceDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceDetails() throws Exception {
        int databaseSizeBeforeUpdate = invoiceDetailsRepository.findAll().size();
        invoiceDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceDetailsWithPatch() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        int databaseSizeBeforeUpdate = invoiceDetailsRepository.findAll().size();

        // Update the invoiceDetails using partial update
        InvoiceDetails partialUpdatedInvoiceDetails = new InvoiceDetails();
        partialUpdatedInvoiceDetails.setId(invoiceDetails.getId());

        partialUpdatedInvoiceDetails
            .itemNo(UPDATED_ITEM_NO)
            .itemPrice(UPDATED_ITEM_PRICE)
            .itemQty(UPDATED_ITEM_QTY)
            .discount(UPDATED_DISCOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restInvoiceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceDetails))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeUpdate);
        InvoiceDetails testInvoiceDetails = invoiceDetailsList.get(invoiceDetailsList.size() - 1);
        assertThat(testInvoiceDetails.getItemNo()).isEqualTo(UPDATED_ITEM_NO);
        assertThat(testInvoiceDetails.getItemDesc()).isEqualTo(DEFAULT_ITEM_DESC);
        assertThat(testInvoiceDetails.getItemPrice()).isEqualTo(UPDATED_ITEM_PRICE);
        assertThat(testInvoiceDetails.getItemQty()).isEqualTo(UPDATED_ITEM_QTY);
        assertThat(testInvoiceDetails.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testInvoiceDetails.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testInvoiceDetails.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceDetailsWithPatch() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        int databaseSizeBeforeUpdate = invoiceDetailsRepository.findAll().size();

        // Update the invoiceDetails using partial update
        InvoiceDetails partialUpdatedInvoiceDetails = new InvoiceDetails();
        partialUpdatedInvoiceDetails.setId(invoiceDetails.getId());

        partialUpdatedInvoiceDetails
            .itemNo(UPDATED_ITEM_NO)
            .itemDesc(UPDATED_ITEM_DESC)
            .itemPrice(UPDATED_ITEM_PRICE)
            .itemQty(UPDATED_ITEM_QTY)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .discount(UPDATED_DISCOUNT)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restInvoiceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceDetails))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeUpdate);
        InvoiceDetails testInvoiceDetails = invoiceDetailsList.get(invoiceDetailsList.size() - 1);
        assertThat(testInvoiceDetails.getItemNo()).isEqualTo(UPDATED_ITEM_NO);
        assertThat(testInvoiceDetails.getItemDesc()).isEqualTo(UPDATED_ITEM_DESC);
        assertThat(testInvoiceDetails.getItemPrice()).isEqualTo(UPDATED_ITEM_PRICE);
        assertThat(testInvoiceDetails.getItemQty()).isEqualTo(UPDATED_ITEM_QTY);
        assertThat(testInvoiceDetails.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testInvoiceDetails.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testInvoiceDetails.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceDetails() throws Exception {
        int databaseSizeBeforeUpdate = invoiceDetailsRepository.findAll().size();
        invoiceDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceDetails() throws Exception {
        int databaseSizeBeforeUpdate = invoiceDetailsRepository.findAll().size();
        invoiceDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceDetails() throws Exception {
        int databaseSizeBeforeUpdate = invoiceDetailsRepository.findAll().size();
        invoiceDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(invoiceDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceDetails in the database
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceDetails() throws Exception {
        // Initialize the database
        invoiceDetailsRepository.saveAndFlush(invoiceDetails);

        int databaseSizeBeforeDelete = invoiceDetailsRepository.findAll().size();

        // Delete the invoiceDetails
        restInvoiceDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceDetails> invoiceDetailsList = invoiceDetailsRepository.findAll();
        assertThat(invoiceDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
