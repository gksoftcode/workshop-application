package com.gksoft.workshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gksoft.workshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemBrandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemBrand.class);
        ItemBrand itemBrand1 = new ItemBrand();
        itemBrand1.setId(1L);
        ItemBrand itemBrand2 = new ItemBrand();
        itemBrand2.setId(itemBrand1.getId());
        assertThat(itemBrand1).isEqualTo(itemBrand2);
        itemBrand2.setId(2L);
        assertThat(itemBrand1).isNotEqualTo(itemBrand2);
        itemBrand1.setId(null);
        assertThat(itemBrand1).isNotEqualTo(itemBrand2);
    }
}
