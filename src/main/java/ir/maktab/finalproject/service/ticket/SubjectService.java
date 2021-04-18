package ir.maktab.finalproject.service.ticket;

import ir.maktab.finalproject.model.ticket.Subject;
import ir.maktab.finalproject.service.BaseEntityService;
import ir.maktab.finalproject.util.exception.SubjectAlreadyExistException;

import java.util.List;

public interface SubjectService extends BaseEntityService<Subject,Long> {
    List<Subject> findAllByIsActiveTrue();
    Boolean checkIfExistSubjectByName(String name);


    void saveByException(Subject entity)throws SubjectAlreadyExistException;
}
