package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.PurchaseOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PurchaseOrderRepositoryWithBagRelationships {
    Optional<PurchaseOrder> fetchBagRelationships(Optional<PurchaseOrder> purchaseOrder);

    List<PurchaseOrder> fetchBagRelationships(List<PurchaseOrder> purchaseOrders);

    Page<PurchaseOrder> fetchBagRelationships(Page<PurchaseOrder> purchaseOrders);
}
