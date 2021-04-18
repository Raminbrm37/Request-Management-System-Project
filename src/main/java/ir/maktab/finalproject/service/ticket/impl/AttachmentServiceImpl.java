//package ir.maktab.finalproject.service.ticket.impl;
//
//import ir.maktab.finalproject.model.ticket.Attachment;
//import ir.maktab.finalproject.repository.AttachmentRepository;
//import ir.maktab.finalproject.service.ticket.AttachmentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class AttachmentServiceImpl implements AttachmentService {
//    @Autowired
//    private AttachmentRepository attachmentRepository;
//
//    @Override
//    public void save(Attachment entity) {
//        attachmentRepository.save(entity);
//    }
//
//    @Override
//    public Attachment findById(Long id) {
//        return attachmentRepository.getOne(id);
//    }
//
//    @Override
//    public List<Attachment> findAll() {
//        return attachmentRepository.findAll();
//    }
//
//    @Override
//    public void remove(Attachment entity) {
//        attachmentRepository.delete(entity);
//    }
//
//    @Override
//    public void removeById(Long id) {
//        attachmentRepository.deleteById(id);
//    }
//}
