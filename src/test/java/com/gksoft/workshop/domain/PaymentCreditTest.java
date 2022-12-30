package com.gksoft.workshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gksoft.workshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentCreditTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentCredit.class);
        PaymentCredit paymentCredit1 = new PaymentCredit();
        paymentCredit1.setId(1L);
        PaymentCredit paymentCredit2 = new PaymentCredit();
        paymentCredit2.setId(paymentCredit1.getId());
        assertThat(paymentCredit1).isEqualTo(paymentCredit2);
        paymentCredit2.setId(2L);
        assertThat(paymentCredit1).isNotEqualTo(paymentCredit2);
        paymentCredit1.setId(null);
        assertThat(paymentCredit1).isNotEqualTo(paymentCredit2);
    }
}
