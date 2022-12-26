package com.gksoft.workshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gksoft.workshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttachmentNotesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttachmentNotes.class);
        AttachmentNotes attachmentNotes1 = new AttachmentNotes();
        attachmentNotes1.setId(1L);
        AttachmentNotes attachmentNotes2 = new AttachmentNotes();
        attachmentNotes2.setId(attachmentNotes1.getId());
        assertThat(attachmentNotes1).isEqualTo(attachmentNotes2);
        attachmentNotes2.setId(2L);
        assertThat(attachmentNotes1).isNotEqualTo(attachmentNotes2);
        attachmentNotes1.setId(null);
        assertThat(attachmentNotes1).isNotEqualTo(attachmentNotes2);
    }
}
