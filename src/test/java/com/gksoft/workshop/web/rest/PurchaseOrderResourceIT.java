package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.PurchaseOrder;
import com.gksoft.workshop.domain.enumeration.DiscountType;
import com.gksoft.workshop.domain.enumeration.PaymentMethod;
import com.gksoft.workshop.domain.enumeration.PaymentMethod;
import com.gksoft.workshop.repository.PurchaseOrderRepository;
import com.gksoft.workshop.service.PurchaseOrderService;
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
 * Integration tests for the {@link PurchaseOrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PurchaseOrderResourceIT {

    private static final Instant DEFAULT_INVOICE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INVOICE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_PAYMENT_TERMS = 1L;
    private static final Long UPDATED_PAYMENT_TERMS = 2L;

    private static final Long DEFAULT_DISCOUNT = 1L;
    private static final Long UPDATED_DISCOUNT = 2L;

    private static final Long DEFAULT_NOTES = 1L;
    private static final Long UPDATED_NOTES = 2L;

    private static final DiscountType DEFAULT_DISCOUNT_TYPE = DiscountType.PERCENTAGE;
    private static final DiscountType UPDATED_DISCOUNT_TYPE = DiscountType.AMOUNT;

    private static final Long DEFAULT_DEPOSIT_AMOUNT = 1L;
    private static final Long UPDATED_DEPOSIT_AMOUNT = 2L;

    private static final Boolean DEFAULT_IS_DEPOSIT_PAIED = false;
    private static final Boolean UPDATED_IS_DEPOSIT_PAIED = true;

    private static final PaymentMethod DEFAULT_DEPOSIT_METHOD = PaymentMethod.Cash;
    private static final PaymentMethod UPDATED_DEPOSIT_METHOD = PaymentMethod.BankTransfer;

    private static final String DEFAULT_DEPOSIT_PAY_REF = "AAAAAAAAAA";
    private static final String UPDATED_DEPOSIT_PAY_REF = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ALREADY_PAIED = false;
    private static final Boolean UPDATED_IS_ALREADY_PAIED = true;

    private static final PaymentMethod DEFAULT_PAYMENT_METHOD = PaymentMethod.Cash;
    private static final PaymentMethod UPDATED_PAYMENT_METHOD = PaymentMethod.BankTransfer;

    private static final String DEFAULT_PAYMENT_REF = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_REF = "BBBBBBBBBB";

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;

    private static final Long DEFAULT_LAST_AMOUNT = 1L;
    private static final Long UPDATED_LAST_AMOUNT = 2L;

    private static final Long DEFAULT_SHIPPING_FEES = 1L;
    private static final Long UPDATED_SHIPPING_FEES = 2L;

    private static final String ENTITY_API_URL = "/api/purchase-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private PurchaseOrderRepository purchaseOrderRepositoryMock;

    @Mock
    private PurchaseOrderService purchaseOrderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchaseOrderMockMvc;

    private PurchaseOrder purchaseOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrder createEntity(EntityManager em) {
        PurchaseOrder purchaseOrder = new PurchaseOrder()
            .invoiceDate(DEFAULT_INVOICE_DATE)
            .issueDate(DEFAULT_ISSUE_DATE)
            .paymentTerms(DEFAULT_PAYMENT_TERMS)
            .discount(DEFAULT_DISCOUNT)
            .notes(DEFAULT_NOTES)
            .discountType(DEFAULT_DISCOUNT_TYPE)
            .depositAmount(DEFAULT_DEPOSIT_AMOUNT)
            .isDepositPaied(DEFAULT_IS_DEPOSIT_PAIED)
            .depositMethod(DEFAULT_DEPOSIT_METHOD)
            .depositPayRef(DEFAULT_DEPOSIT_PAY_REF)
            .isAlreadyPaied(DEFAULT_IS_ALREADY_PAIED)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .paymentRef(DEFAULT_PAYMENT_REF)
            .amount(DEFAULT_AMOUNT)
            .lastAmount(DEFAULT_LAST_AMOUNT)
            .shippingFees(DEFAULT_SHIPPING_FEES);
        return purchaseOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrder createUpdatedEntity(EntityManager em) {
        PurchaseOrder purchaseOrder = new PurchaseOrder()
            .invoiceDate(UPDATED_INVOICE_DATE)
            .issueDate(UPDATED_ISSUE_DATE)
            .paymentTerms(UPDATED_PAYMENT_TERMS)
            .discount(UPDATED_DISCOUNT)
            .notes(UPDATED_NOTES)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .depositAmount(UPDATED_DEPOSIT_AMOUNT)
            .isDepositPaied(UPDATED_IS_DEPOSIT_PAIED)
            .depositMethod(UPDATED_DEPOSIT_METHOD)
            .depositPayRef(UPDATED_DEPOSIT_PAY_REF)
            .isAlreadyPaied(UPDATED_IS_ALREADY_PAIED)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paymentRef(UPDATED_PAYMENT_REF)
            .amount(UPDATED_AMOUNT)
            .lastAmount(UPDATED_LAST_AMOUNT)
            .shippingFees(UPDATED_SHIPPING_FEES);
        return purchaseOrder;
    }

    @BeforeEach
    public void initTest() {
        purchaseOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchaseOrder() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderRepository.findAll().size();
        // Create the PurchaseOrder
        restPurchaseOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseOrder)))
            .andExpect(status().isCreated());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseOrder testPurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
        assertThat(testPurchaseOrder.getInvoiceDate()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testPurchaseOrder.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
        assertThat(testPurchaseOrder.getPaymentTerms()).isEqualTo(DEFAULT_PAYMENT_TERMS);
        assertThat(testPurchaseOrder.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testPurchaseOrder.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testPurchaseOrder.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testPurchaseOrder.getDepositAmount()).isEqualTo(DEFAULT_DEPOSIT_AMOUNT);
        assertThat(testPurchaseOrder.getIsDepositPaied()).isEqualTo(DEFAULT_IS_DEPOSIT_PAIED);
        assertThat(testPurchaseOrder.getDepositMethod()).isEqualTo(DEFAULT_DEPOSIT_METHOD);
        assertThat(testPurchaseOrder.getDepositPayRef()).isEqualTo(DEFAULT_DEPOSIT_PAY_REF);
        assertThat(testPurchaseOrder.getIsAlreadyPaied()).isEqualTo(DEFAULT_IS_ALREADY_PAIED);
        assertThat(testPurchaseOrder.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testPurchaseOrder.getPaymentRef()).isEqualTo(DEFAULT_PAYMENT_REF);
        assertThat(testPurchaseOrder.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPurchaseOrder.getLastAmount()).isEqualTo(DEFAULT_LAST_AMOUNT);
        assertThat(testPurchaseOrder.getShippingFees()).isEqualTo(DEFAULT_SHIPPING_FEES);
    }

    @Test
    @Transactional
    void createPurchaseOrderWithExistingId() throws Exception {
        // Create the PurchaseOrder with an existing ID
        purchaseOrder.setId(1L);

        int databaseSizeBeforeCreate = purchaseOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseOrder)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPurchaseOrders() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentTerms").value(hasItem(DEFAULT_PAYMENT_TERMS.intValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.intValue())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].depositAmount").value(hasItem(DEFAULT_DEPOSIT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].isDepositPaied").value(hasItem(DEFAULT_IS_DEPOSIT_PAIED.booleanValue())))
            .andExpect(jsonPath("$.[*].depositMethod").value(hasItem(DEFAULT_DEPOSIT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].depositPayRef").value(hasItem(DEFAULT_DEPOSIT_PAY_REF)))
            .andExpect(jsonPath("$.[*].isAlreadyPaied").value(hasItem(DEFAULT_IS_ALREADY_PAIED.booleanValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].paymentRef").value(hasItem(DEFAULT_PAYMENT_REF)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].lastAmount").value(hasItem(DEFAULT_LAST_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].shippingFees").value(hasItem(DEFAULT_SHIPPING_FEES.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchaseOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(purchaseOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchaseOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(purchaseOrderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchaseOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(purchaseOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchaseOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(purchaseOrderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get the purchaseOrder
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, purchaseOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseOrder.getId().intValue()))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.paymentTerms").value(DEFAULT_PAYMENT_TERMS.intValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.intValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.intValue()))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.depositAmount").value(DEFAULT_DEPOSIT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.isDepositPaied").value(DEFAULT_IS_DEPOSIT_PAIED.booleanValue()))
            .andExpect(jsonPath("$.depositMethod").value(DEFAULT_DEPOSIT_METHOD.toString()))
            .andExpect(jsonPath("$.depositPayRef").value(DEFAULT_DEPOSIT_PAY_REF))
            .andExpect(jsonPath("$.isAlreadyPaied").value(DEFAULT_IS_ALREADY_PAIED.booleanValue()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.paymentRef").value(DEFAULT_PAYMENT_REF))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.lastAmount").value(DEFAULT_LAST_AMOUNT.intValue()))
            .andExpect(jsonPath("$.shippingFees").value(DEFAULT_SHIPPING_FEES.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPurchaseOrder() throws Exception {
        // Get the purchaseOrder
        restPurchaseOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

        // Update the purchaseOrder
        PurchaseOrder updatedPurchaseOrder = purchaseOrderRepository.findById(purchaseOrder.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseOrder are not directly saved in db
        em.detach(updatedPurchaseOrder);
        updatedPurchaseOrder
            .invoiceDate(UPDATED_INVOICE_DATE)
            .issueDate(UPDATED_ISSUE_DATE)
            .paymentTerms(UPDATED_PAYMENT_TERMS)
            .discount(UPDATED_DISCOUNT)
            .notes(UPDATED_NOTES)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .depositAmount(UPDATED_DEPOSIT_AMOUNT)
            .isDepositPaied(UPDATED_IS_DEPOSIT_PAIED)
            .depositMethod(UPDATED_DEPOSIT_METHOD)
            .depositPayRef(UPDATED_DEPOSIT_PAY_REF)
            .isAlreadyPaied(UPDATED_IS_ALREADY_PAIED)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paymentRef(UPDATED_PAYMENT_REF)
            .amount(UPDATED_AMOUNT)
            .lastAmount(UPDATED_LAST_AMOUNT)
            .shippingFees(UPDATED_SHIPPING_FEES);

        restPurchaseOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPurchaseOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPurchaseOrder))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrder testPurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
        assertThat(testPurchaseOrder.getInvoiceDate()).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testPurchaseOrder.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testPurchaseOrder.getPaymentTerms()).isEqualTo(UPDATED_PAYMENT_TERMS);
        assertThat(testPurchaseOrder.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testPurchaseOrder.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPurchaseOrder.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testPurchaseOrder.getDepositAmount()).isEqualTo(UPDATED_DEPOSIT_AMOUNT);
        assertThat(testPurchaseOrder.getIsDepositPaied()).isEqualTo(UPDATED_IS_DEPOSIT_PAIED);
        assertThat(testPurchaseOrder.getDepositMethod()).isEqualTo(UPDATED_DEPOSIT_METHOD);
        assertThat(testPurchaseOrder.getDepositPayRef()).isEqualTo(UPDATED_DEPOSIT_PAY_REF);
        assertThat(testPurchaseOrder.getIsAlreadyPaied()).isEqualTo(UPDATED_IS_ALREADY_PAIED);
        assertThat(testPurchaseOrder.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPurchaseOrder.getPaymentRef()).isEqualTo(UPDATED_PAYMENT_REF);
        assertThat(testPurchaseOrder.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPurchaseOrder.getLastAmount()).isEqualTo(UPDATED_LAST_AMOUNT);
        assertThat(testPurchaseOrder.getShippingFees()).isEqualTo(UPDATED_SHIPPING_FEES);
    }

    @Test
    @Transactional
    void putNonExistingPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseOrder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchaseOrderWithPatch() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

        // Update the purchaseOrder using partial update
        PurchaseOrder partialUpdatedPurchaseOrder = new PurchaseOrder();
        partialUpdatedPurchaseOrder.setId(purchaseOrder.getId());

        partialUpdatedPurchaseOrder
            .paymentTerms(UPDATED_PAYMENT_TERMS)
            .notes(UPDATED_NOTES)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .depositMethod(UPDATED_DEPOSIT_METHOD)
            .depositPayRef(UPDATED_DEPOSIT_PAY_REF)
            .paymentRef(UPDATED_PAYMENT_REF);

        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseOrder))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrder testPurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
        assertThat(testPurchaseOrder.getInvoiceDate()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testPurchaseOrder.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
        assertThat(testPurchaseOrder.getPaymentTerms()).isEqualTo(UPDATED_PAYMENT_TERMS);
        assertThat(testPurchaseOrder.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testPurchaseOrder.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPurchaseOrder.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testPurchaseOrder.getDepositAmount()).isEqualTo(DEFAULT_DEPOSIT_AMOUNT);
        assertThat(testPurchaseOrder.getIsDepositPaied()).isEqualTo(DEFAULT_IS_DEPOSIT_PAIED);
        assertThat(testPurchaseOrder.getDepositMethod()).isEqualTo(UPDATED_DEPOSIT_METHOD);
        assertThat(testPurchaseOrder.getDepositPayRef()).isEqualTo(UPDATED_DEPOSIT_PAY_REF);
        assertThat(testPurchaseOrder.getIsAlreadyPaied()).isEqualTo(DEFAULT_IS_ALREADY_PAIED);
        assertThat(testPurchaseOrder.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testPurchaseOrder.getPaymentRef()).isEqualTo(UPDATED_PAYMENT_REF);
        assertThat(testPurchaseOrder.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPurchaseOrder.getLastAmount()).isEqualTo(DEFAULT_LAST_AMOUNT);
        assertThat(testPurchaseOrder.getShippingFees()).isEqualTo(DEFAULT_SHIPPING_FEES);
    }

    @Test
    @Transactional
    void fullUpdatePurchaseOrderWithPatch() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

        // Update the purchaseOrder using partial update
        PurchaseOrder partialUpdatedPurchaseOrder = new PurchaseOrder();
        partialUpdatedPurchaseOrder.setId(purchaseOrder.getId());

        partialUpdatedPurchaseOrder
            .invoiceDate(UPDATED_INVOICE_DATE)
            .issueDate(UPDATED_ISSUE_DATE)
            .paymentTerms(UPDATED_PAYMENT_TERMS)
            .discount(UPDATED_DISCOUNT)
            .notes(UPDATED_NOTES)
            .discountType(UPDATED_DISCOUNT_TYPE)
            .depositAmount(UPDATED_DEPOSIT_AMOUNT)
            .isDepositPaied(UPDATED_IS_DEPOSIT_PAIED)
            .depositMethod(UPDATED_DEPOSIT_METHOD)
            .depositPayRef(UPDATED_DEPOSIT_PAY_REF)
            .isAlreadyPaied(UPDATED_IS_ALREADY_PAIED)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paymentRef(UPDATED_PAYMENT_REF)
            .amount(UPDATED_AMOUNT)
            .lastAmount(UPDATED_LAST_AMOUNT)
            .shippingFees(UPDATED_SHIPPING_FEES);

        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseOrder))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrder testPurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
        assertThat(testPurchaseOrder.getInvoiceDate()).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testPurchaseOrder.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testPurchaseOrder.getPaymentTerms()).isEqualTo(UPDATED_PAYMENT_TERMS);
        assertThat(testPurchaseOrder.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testPurchaseOrder.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPurchaseOrder.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testPurchaseOrder.getDepositAmount()).isEqualTo(UPDATED_DEPOSIT_AMOUNT);
        assertThat(testPurchaseOrder.getIsDepositPaied()).isEqualTo(UPDATED_IS_DEPOSIT_PAIED);
        assertThat(testPurchaseOrder.getDepositMethod()).isEqualTo(UPDATED_DEPOSIT_METHOD);
        assertThat(testPurchaseOrder.getDepositPayRef()).isEqualTo(UPDATED_DEPOSIT_PAY_REF);
        assertThat(testPurchaseOrder.getIsAlreadyPaied()).isEqualTo(UPDATED_IS_ALREADY_PAIED);
        assertThat(testPurchaseOrder.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPurchaseOrder.getPaymentRef()).isEqualTo(UPDATED_PAYMENT_REF);
        assertThat(testPurchaseOrder.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPurchaseOrder.getLastAmount()).isEqualTo(UPDATED_LAST_AMOUNT);
        assertThat(testPurchaseOrder.getShippingFees()).isEqualTo(UPDATED_SHIPPING_FEES);
    }

    @Test
    @Transactional
    void patchNonExistingPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchaseOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(purchaseOrder))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        int databaseSizeBeforeDelete = purchaseOrderRepository.findAll().size();

        // Delete the purchaseOrder
        restPurchaseOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchaseOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
