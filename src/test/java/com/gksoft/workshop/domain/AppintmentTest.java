package com.gksoft.workshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gksoft.workshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppintmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appintment.class);
        Appintment appintment1 = new Appintment();
        appintment1.setId(1L);
        Appintment appintment2 = new Appintment();
        appintment2.setId(appintment1.getId());
        assertThat(appintment1).isEqualTo(appintment2);
        appintment2.setId(2L);
        assertThat(appintment1).isNotEqualTo(appintment2);
        appintment1.setId(null);
        assertThat(appintment1).isNotEqualTo(appintment2);
    }
}
