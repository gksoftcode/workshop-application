package com.gksoft.workshop.repository;

import com.gksoft.workshop.domain.AttachmentNotes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AttachmentNotesRepositoryWithBagRelationships {
    Optional<AttachmentNotes> fetchBagRelationships(Optional<AttachmentNotes> attachmentNotes);

    List<AttachmentNotes> fetchBagRelationships(List<AttachmentNotes> attachmentNotes);

    Page<AttachmentNotes> fetchBagRelationships(Page<AttachmentNotes> attachmentNotes);
}
