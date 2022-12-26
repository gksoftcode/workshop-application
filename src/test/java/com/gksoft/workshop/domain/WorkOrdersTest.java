package com.gksoft.workshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gksoft.workshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkOrdersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkOrders.class);
        WorkOrders workOrders1 = new WorkOrders();
        workOrders1.setId(1L);
        WorkOrders workOrders2 = new WorkOrders();
        workOrders2.setId(workOrders1.getId());
        assertThat(workOrders1).isEqualTo(workOrders2);
        workOrders2.setId(2L);
        assertThat(workOrders1).isNotEqualTo(workOrders2);
        workOrders1.setId(null);
        assertThat(workOrders1).isNotEqualTo(workOrders2);
    }
}
