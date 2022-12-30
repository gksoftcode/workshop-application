package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.PaymentCredit;
import com.gksoft.workshop.domain.enumeration.PaymentMethod;
import com.gksoft.workshop.domain.enumeration.PaymentStatus;
import com.gksoft.workshop.repository.PaymentCreditRepository;
import com.gksoft.workshop.service.PaymentCreditService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PaymentCreditResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaymentCreditResourceIT {

    private static final PaymentMethod DEFAULT_PAYMENT_METHOD = PaymentMethod.Cash;
    private static final PaymentMethod UPDATED_PAYMENT_METHOD = PaymentMethod.BankTransfer;

    private static final String DEFAULT_PAYMENT_REF = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_REF = "BBBBBBBBBB";

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;

    private static final Instant DEFAULT_ADD_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADD_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PaymentStatus DEFAULT_PAYMENT_STATUS = PaymentStatus.Complete;
    private static final PaymentStatus UPDATED_PAYMENT_STATUS = PaymentStatus.Incomplete;

    private static final String DEFAULT_PAYMENT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIPT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_RECEIPT_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payment-credits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentCreditRepository paymentCreditRepository;

    @Mock
    private PaymentCreditRepository paymentCreditRepositoryMock;

    @Mock
    private PaymentCreditService paymentCreditServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentCreditMockMvc;

    private PaymentCredit paymentCredit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentCredit createEntity(EntityManager em) {
        PaymentCredit paymentCredit = new PaymentCredit()
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .paymentRef(DEFAULT_PAYMENT_REF)
            .amount(DEFAULT_AMOUNT)
            .addDate(DEFAULT_ADD_DATE)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .paymentDetails(DEFAULT_PAYMENT_DETAILS)
            .receiptNotes(DEFAULT_RECEIPT_NOTES);
        return paymentCredit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentCredit createUpdatedEntity(EntityManager em) {
        PaymentCredit paymentCredit = new PaymentCredit()
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paymentRef(UPDATED_PAYMENT_REF)
            .amount(UPDATED_AMOUNT)
            .addDate(UPDATED_ADD_DATE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentDetails(UPDATED_PAYMENT_DETAILS)
            .receiptNotes(UPDATED_RECEIPT_NOTES);
        return paymentCredit;
    }

    @BeforeEach
    public void initTest() {
        paymentCredit = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentCredit() throws Exception {
        int databaseSizeBeforeCreate = paymentCreditRepository.findAll().size();
        // Create the PaymentCredit
        restPaymentCreditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentCredit)))
            .andExpect(status().isCreated());

        // Validate the PaymentCredit in the database
        List<PaymentCredit> paymentCreditList = paymentCreditRepository.findAll();
        assertThat(paymentCreditList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentCredit testPaymentCredit = paymentCreditList.get(paymentCreditList.size() - 1);
        assertThat(testPaymentCredit.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testPaymentCredit.getPaymentRef()).isEqualTo(DEFAULT_PAYMENT_REF);
        assertThat(testPaymentCredit.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPaymentCredit.getAddDate()).isEqualTo(DEFAULT_ADD_DATE);
        assertThat(testPaymentCredit.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testPaymentCredit.getPaymentDetails()).isEqualTo(DEFAULT_PAYMENT_DETAILS);
        assertThat(testPaymentCredit.getReceiptNotes()).isEqualTo(DEFAULT_RECEIPT_NOTES);
    }

    @Test
    @Transactional
    void createPaymentCreditWithExistingId() throws Exception {
        // Create the PaymentCredit with an existing ID
        paymentCredit.setId(1L);

        int databaseSizeBeforeCreate = paymentCreditRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentCreditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentCredit)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentCredit in the database
        List<PaymentCredit> paymentCreditList = paymentCreditRepository.findAll();
        assertThat(paymentCreditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaymentCredits() throws Exception {
        // Initialize the database
        paymentCreditRepository.saveAndFlush(paymentCredit);

        // Get all the paymentCreditList
        restPaymentCreditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentCredit.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].paymentRef").value(hasItem(DEFAULT_PAYMENT_REF)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].addDate").value(hasItem(DEFAULT_ADD_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentDetails").value(hasItem(DEFAULT_PAYMENT_DETAILS)))
            .andExpect(jsonPath("$.[*].receiptNotes").value(hasItem(DEFAULT_RECEIPT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentCreditsWithEagerRelationshipsIsEnabled() throws Exception {
        when(paymentCreditServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentCreditMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paymentCreditServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentCreditsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paymentCreditServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentCreditMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(paymentCreditRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPaymentCredit() throws Exception {
        // Initialize the database
        paymentCreditRepository.saveAndFlush(paymentCredit);

        // Get the paymentCredit
        restPaymentCreditMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentCredit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentCredit.getId().intValue()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.paymentRef").value(DEFAULT_PAYMENT_REF))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.addDate").value(DEFAULT_ADD_DATE.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentDetails").value(DEFAULT_PAYMENT_DETAILS))
            .andExpect(jsonPath("$.receiptNotes").value(DEFAULT_RECEIPT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingPaymentCredit() throws Exception {
        // Get the paymentCredit
        restPaymentCreditMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentCredit() throws Exception {
        // Initialize the database
        paymentCreditRepository.saveAndFlush(paymentCredit);

        int databaseSizeBeforeUpdate = paymentCreditRepository.findAll().size();

        // Update the paymentCredit
        PaymentCredit updatedPaymentCredit = paymentCreditRepository.findById(paymentCredit.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentCredit are not directly saved in db
        em.detach(updatedPaymentCredit);
        updatedPaymentCredit
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paymentRef(UPDATED_PAYMENT_REF)
            .amount(UPDATED_AMOUNT)
            .addDate(UPDATED_ADD_DATE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentDetails(UPDATED_PAYMENT_DETAILS)
            .receiptNotes(UPDATED_RECEIPT_NOTES);

        restPaymentCreditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentCredit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPaymentCredit))
            )
            .andExpect(status().isOk());

        // Validate the PaymentCredit in the database
        List<PaymentCredit> paymentCreditList = paymentCreditRepository.findAll();
        assertThat(paymentCreditList).hasSize(databaseSizeBeforeUpdate);
        PaymentCredit testPaymentCredit = paymentCreditList.get(paymentCreditList.size() - 1);
        assertThat(testPaymentCredit.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPaymentCredit.getPaymentRef()).isEqualTo(UPDATED_PAYMENT_REF);
        assertThat(testPaymentCredit.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPaymentCredit.getAddDate()).isEqualTo(UPDATED_ADD_DATE);
        assertThat(testPaymentCredit.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPaymentCredit.getPaymentDetails()).isEqualTo(UPDATED_PAYMENT_DETAILS);
        assertThat(testPaymentCredit.getReceiptNotes()).isEqualTo(UPDATED_RECEIPT_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingPaymentCredit() throws Exception {
        int databaseSizeBeforeUpdate = paymentCreditRepository.findAll().size();
        paymentCredit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentCreditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentCredit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentCredit))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentCredit in the database
        List<PaymentCredit> paymentCreditList = paymentCreditRepository.findAll();
        assertThat(paymentCreditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentCredit() throws Exception {
        int databaseSizeBeforeUpdate = paymentCreditRepository.findAll().size();
        paymentCredit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentCreditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentCredit))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentCredit in the database
        List<PaymentCredit> paymentCreditList = paymentCreditRepository.findAll();
        assertThat(paymentCreditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentCredit() throws Exception {
        int databaseSizeBeforeUpdate = paymentCreditRepository.findAll().size();
        paymentCredit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentCreditMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentCredit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentCredit in the database
        List<PaymentCredit> paymentCreditList = paymentCreditRepository.findAll();
        assertThat(paymentCreditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentCreditWithPatch() throws Exception {
        // Initialize the database
        paymentCreditRepository.saveAndFlush(paymentCredit);

        int databaseSizeBeforeUpdate = paymentCreditRepository.findAll().size();

        // Update the paymentCredit using partial update
        PaymentCredit partialUpdatedPaymentCredit = new PaymentCredit();
        partialUpdatedPaymentCredit.setId(paymentCredit.getId());

        partialUpdatedPaymentCredit.paymentDetails(UPDATED_PAYMENT_DETAILS).receiptNotes(UPDATED_RECEIPT_NOTES);

        restPaymentCreditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentCredit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentCredit))
            )
            .andExpect(status().isOk());

        // Validate the PaymentCredit in the database
        List<PaymentCredit> paymentCreditList = paymentCreditRepository.findAll();
        assertThat(paymentCreditList).hasSize(databaseSizeBeforeUpdate);
        PaymentCredit testPaymentCredit = paymentCreditList.get(paymentCreditList.size() - 1);
        assertThat(testPaymentCredit.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testPaymentCredit.getPaymentRef()).isEqualTo(DEFAULT_PAYMENT_REF);
        assertThat(testPaymentCredit.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPaymentCredit.getAddDate()).isEqualTo(DEFAULT_ADD_DATE);
        assertThat(testPaymentCredit.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testPaymentCredit.getPaymentDetails()).isEqualTo(UPDATED_PAYMENT_DETAILS);
        assertThat(testPaymentCredit.getReceiptNotes()).isEqualTo(UPDATED_RECEIPT_NOTES);
    }

    @Test
    @Transactional
    void fullUpdatePaymentCreditWithPatch() throws Exception {
        // Initialize the database
        paymentCreditRepository.saveAndFlush(paymentCredit);

        int databaseSizeBeforeUpdate = paymentCreditRepository.findAll().size();

        // Update the paymentCredit using partial update
        PaymentCredit partialUpdatedPaymentCredit = new PaymentCredit();
        partialUpdatedPaymentCredit.setId(paymentCredit.getId());

        partialUpdatedPaymentCredit
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paymentRef(UPDATED_PAYMENT_REF)
            .amount(UPDATED_AMOUNT)
            .addDate(UPDATED_ADD_DATE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentDetails(UPDATED_PAYMENT_DETAILS)
            .receiptNotes(UPDATED_RECEIPT_NOTES);

        restPaymentCreditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentCredit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentCredit))
            )
            .andExpect(status().isOk());

        // Validate the PaymentCredit in the database
        List<PaymentCredit> paymentCreditList = paymentCreditRepository.findAll();
        assertThat(paymentCreditList).hasSize(databaseSizeBeforeUpdate);
        PaymentCredit testPaymentCredit = paymentCreditList.get(paymentCreditList.size() - 1);
        assertThat(testPaymentCredit.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPaymentCredit.getPaymentRef()).isEqualTo(UPDATED_PAYMENT_REF);
        assertThat(testPaymentCredit.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPaymentCredit.getAddDate()).isEqualTo(UPDATED_ADD_DATE);
        assertThat(testPaymentCredit.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPaymentCredit.getPaymentDetails()).isEqualTo(UPDATED_PAYMENT_DETAILS);
        assertThat(testPaymentCredit.getReceiptNotes()).isEqualTo(UPDATED_RECEIPT_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingPaymentCredit() throws Exception {
        int databaseSizeBeforeUpdate = paymentCreditRepository.findAll().size();
        paymentCredit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentCreditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentCredit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentCredit))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentCredit in the database
        List<PaymentCredit> paymentCreditList = paymentCreditRepository.findAll();
        assertThat(paymentCreditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentCredit() throws Exception {
        int databaseSizeBeforeUpdate = paymentCreditRepository.findAll().size();
        paymentCredit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentCreditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentCredit))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentCredit in the database
        List<PaymentCredit> paymentCreditList = paymentCreditRepository.findAll();
        assertThat(paymentCreditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentCredit() throws Exception {
        int databaseSizeBeforeUpdate = paymentCreditRepository.findAll().size();
        paymentCredit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentCreditMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paymentCredit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentCredit in the database
        List<PaymentCredit> paymentCreditList = paymentCreditRepository.findAll();
        assertThat(paymentCreditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentCredit() throws Exception {
        // Initialize the database
        paymentCreditRepository.saveAndFlush(paymentCredit);

        int databaseSizeBeforeDelete = paymentCreditRepository.findAll().size();

        // Delete the paymentCredit
        restPaymentCreditMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentCredit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentCredit> paymentCreditList = paymentCreditRepository.findAll();
        assertThat(paymentCreditList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
