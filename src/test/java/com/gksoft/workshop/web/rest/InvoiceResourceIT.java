package com.gksoft.workshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.workshop.IntegrationTest;
import com.gksoft.workshop.domain.Invoice;
import com.gksoft.workshop.domain.enumeration.DiscountType;
import com.gksoft.workshop.domain.enumeration.PaymentMethod;
import com.gksoft.workshop.domain.enumeration.PaymentMethod;
import com.gksoft.workshop.repository.InvoiceRepository;
import com.gksoft.workshop.service.InvoiceService;
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
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InvoiceResourceIT {

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

    private static final String ENTITY_API_URL = "/api/invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceRepository invoiceRepositoryMock;

    @Mock
    private InvoiceService invoiceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
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
        return invoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createUpdatedEntity(EntityManager em) {
        Invoice invoice = new Invoice()
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
        return invoice;
    }

    @BeforeEach
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();
        // Create the Invoice
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceDate()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testInvoice.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
        assertThat(testInvoice.getPaymentTerms()).isEqualTo(DEFAULT_PAYMENT_TERMS);
        assertThat(testInvoice.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testInvoice.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testInvoice.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testInvoice.getDepositAmount()).isEqualTo(DEFAULT_DEPOSIT_AMOUNT);
        assertThat(testInvoice.getIsDepositPaied()).isEqualTo(DEFAULT_IS_DEPOSIT_PAIED);
        assertThat(testInvoice.getDepositMethod()).isEqualTo(DEFAULT_DEPOSIT_METHOD);
        assertThat(testInvoice.getDepositPayRef()).isEqualTo(DEFAULT_DEPOSIT_PAY_REF);
        assertThat(testInvoice.getIsAlreadyPaied()).isEqualTo(DEFAULT_IS_ALREADY_PAIED);
        assertThat(testInvoice.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testInvoice.getPaymentRef()).isEqualTo(DEFAULT_PAYMENT_REF);
        assertThat(testInvoice.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testInvoice.getLastAmount()).isEqualTo(DEFAULT_LAST_AMOUNT);
        assertThat(testInvoice.getShippingFees()).isEqualTo(DEFAULT_SHIPPING_FEES);
    }

    @Test
    @Transactional
    void createInvoiceWithExistingId() throws Exception {
        // Create the Invoice with an existing ID
        invoice.setId(1L);

        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
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
    void getAllInvoicesWithEagerRelationshipsIsEnabled() throws Exception {
        when(invoiceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvoiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(invoiceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvoicesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(invoiceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvoiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(invoiceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL_ID, invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
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
    void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
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

        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceDate()).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testInvoice.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testInvoice.getPaymentTerms()).isEqualTo(UPDATED_PAYMENT_TERMS);
        assertThat(testInvoice.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testInvoice.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testInvoice.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testInvoice.getDepositAmount()).isEqualTo(UPDATED_DEPOSIT_AMOUNT);
        assertThat(testInvoice.getIsDepositPaied()).isEqualTo(UPDATED_IS_DEPOSIT_PAIED);
        assertThat(testInvoice.getDepositMethod()).isEqualTo(UPDATED_DEPOSIT_METHOD);
        assertThat(testInvoice.getDepositPayRef()).isEqualTo(UPDATED_DEPOSIT_PAY_REF);
        assertThat(testInvoice.getIsAlreadyPaied()).isEqualTo(UPDATED_IS_ALREADY_PAIED);
        assertThat(testInvoice.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testInvoice.getPaymentRef()).isEqualTo(UPDATED_PAYMENT_REF);
        assertThat(testInvoice.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInvoice.getLastAmount()).isEqualTo(UPDATED_LAST_AMOUNT);
        assertThat(testInvoice.getShippingFees()).isEqualTo(UPDATED_SHIPPING_FEES);
    }

    @Test
    @Transactional
    void putNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .paymentTerms(UPDATED_PAYMENT_TERMS)
            .discount(UPDATED_DISCOUNT)
            .notes(UPDATED_NOTES)
            .depositMethod(UPDATED_DEPOSIT_METHOD)
            .depositPayRef(UPDATED_DEPOSIT_PAY_REF)
            .isAlreadyPaied(UPDATED_IS_ALREADY_PAIED)
            .amount(UPDATED_AMOUNT);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceDate()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testInvoice.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
        assertThat(testInvoice.getPaymentTerms()).isEqualTo(UPDATED_PAYMENT_TERMS);
        assertThat(testInvoice.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testInvoice.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testInvoice.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);
        assertThat(testInvoice.getDepositAmount()).isEqualTo(DEFAULT_DEPOSIT_AMOUNT);
        assertThat(testInvoice.getIsDepositPaied()).isEqualTo(DEFAULT_IS_DEPOSIT_PAIED);
        assertThat(testInvoice.getDepositMethod()).isEqualTo(UPDATED_DEPOSIT_METHOD);
        assertThat(testInvoice.getDepositPayRef()).isEqualTo(UPDATED_DEPOSIT_PAY_REF);
        assertThat(testInvoice.getIsAlreadyPaied()).isEqualTo(UPDATED_IS_ALREADY_PAIED);
        assertThat(testInvoice.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testInvoice.getPaymentRef()).isEqualTo(DEFAULT_PAYMENT_REF);
        assertThat(testInvoice.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInvoice.getLastAmount()).isEqualTo(DEFAULT_LAST_AMOUNT);
        assertThat(testInvoice.getShippingFees()).isEqualTo(DEFAULT_SHIPPING_FEES);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
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

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceDate()).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testInvoice.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testInvoice.getPaymentTerms()).isEqualTo(UPDATED_PAYMENT_TERMS);
        assertThat(testInvoice.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testInvoice.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testInvoice.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);
        assertThat(testInvoice.getDepositAmount()).isEqualTo(UPDATED_DEPOSIT_AMOUNT);
        assertThat(testInvoice.getIsDepositPaied()).isEqualTo(UPDATED_IS_DEPOSIT_PAIED);
        assertThat(testInvoice.getDepositMethod()).isEqualTo(UPDATED_DEPOSIT_METHOD);
        assertThat(testInvoice.getDepositPayRef()).isEqualTo(UPDATED_DEPOSIT_PAY_REF);
        assertThat(testInvoice.getIsAlreadyPaied()).isEqualTo(UPDATED_IS_ALREADY_PAIED);
        assertThat(testInvoice.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testInvoice.getPaymentRef()).isEqualTo(UPDATED_PAYMENT_REF);
        assertThat(testInvoice.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInvoice.getLastAmount()).isEqualTo(UPDATED_LAST_AMOUNT);
        assertThat(testInvoice.getShippingFees()).isEqualTo(UPDATED_SHIPPING_FEES);
    }

    @Test
    @Transactional
    void patchNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(invoice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Delete the invoice
        restInvoiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
