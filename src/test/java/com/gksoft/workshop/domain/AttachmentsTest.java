package com.gksoft.workshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gksoft.workshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttachmentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attachments.class);
        Attachments attachments1 = new Attachments();
        attachments1.setId(1L);
        Attachments attachments2 = new Attachments();
        attachments2.setId(attachments1.getId());
        assertThat(attachments1).isEqualTo(attachments2);
        attachments2.setId(2L);
        assertThat(attachments1).isNotEqualTo(attachments2);
        attachments1.setId(null);
        assertThat(attachments1).isNotEqualTo(attachments2);
    }
}
