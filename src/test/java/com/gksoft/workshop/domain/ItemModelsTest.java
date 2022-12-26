package com.gksoft.workshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gksoft.workshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemModelsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemModels.class);
        ItemModels itemModels1 = new ItemModels();
        itemModels1.setId(1L);
        ItemModels itemModels2 = new ItemModels();
        itemModels2.setId(itemModels1.getId());
        assertThat(itemModels1).isEqualTo(itemModels2);
        itemModels2.setId(2L);
        assertThat(itemModels1).isNotEqualTo(itemModels2);
        itemModels1.setId(null);
        assertThat(itemModels1).isNotEqualTo(itemModels2);
    }
}
