package ir.maktab.finalproject.service.ticket.impl;

import ir.maktab.finalproject.model.ticket.Subject;
import ir.maktab.finalproject.repository.SubjectRepository;
import ir.maktab.finalproject.service.ticket.SubjectService;
import ir.maktab.finalproject.util.exception.SubjectAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImp implements SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public void save(Subject entity)  {

        subjectRepository.save(entity);
    }

    @Override
    public Subject findById(Long id) {
        return subjectRepository.getOne(id);
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    @Override
    public void remove(Subject entity) {
        subjectRepository.delete(entity);
    }

    @Override
    public void removeById(Long id) {
        subjectRepository.deleteById(id);
    }

    @Override
    public List<Subject> findAllByIsActiveTrue() {
        return subjectRepository.findAllByIsActiveTrue();
    }

    @Override
    public Boolean checkIfExistSubjectByName(String name) {
        return subjectRepository.findByName(name)!=null;
    }

    @Override
    public void saveByException(Subject entity) throws SubjectAlreadyExistException {
        if (this.checkIfExistSubjectByName(entity.getName())){
            throw new SubjectAlreadyExistException("این موضوع با این نام وجود دارد"+entity.getName());
        }
        subjectRepository.save(entity);
    }
}
