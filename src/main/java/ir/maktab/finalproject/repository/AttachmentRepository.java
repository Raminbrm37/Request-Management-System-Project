package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.model.ticket.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
}
