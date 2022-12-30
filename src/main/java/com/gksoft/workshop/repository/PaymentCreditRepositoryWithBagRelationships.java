package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.PaymentCredit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PaymentCreditRepositoryWithBagRelationships {
    Optional<PaymentCredit> fetchBagRelationships(Optional<PaymentCredit> paymentCredit);

    List<PaymentCredit> fetchBagRelationships(List<PaymentCredit> paymentCredits);

    Page<PaymentCredit> fetchBagRelationships(Page<PaymentCredit> paymentCredits);
}
