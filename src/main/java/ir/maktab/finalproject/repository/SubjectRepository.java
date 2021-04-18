package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.model.ticket.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    List<Subject> findAllByIsActiveTrue();
    Subject findByName(String name);
}
