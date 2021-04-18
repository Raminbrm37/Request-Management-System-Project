package ir.maktab.finalproject.controller.crm.subject;

import ir.maktab.finalproject.model.ticket.Subject;
import ir.maktab.finalproject.service.ticket.impl.SubjectServiceImp;
import ir.maktab.finalproject.util.exception.SubjectAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/crm/subject")
@PreAuthorize("hasAuthority('OP_SUBJECT_MANAGEMENT_CRM')")
public class SubjectController {
    @Autowired
    private SubjectServiceImp subjectServiceImp;

    @GetMapping
    public String getSubjectIndex(Model model, @ModelAttribute("exception") String exception) {
        List<Subject> subjects = subjectServiceImp.findAll();
        model.addAttribute("subject", new Subject());
        model.addAttribute("subjects", subjects);
        return "crm/subject/subject-index";
    }

    @PostMapping("/create")
    public String getCreateSubject(Subject subject, RedirectAttributes attributes) throws SubjectAlreadyExistException {
        try {
            subjectServiceImp.saveByException(subject);
            return "redirect:/crm/subject";
        } catch (SubjectAlreadyExistException e) {
            attributes.addFlashAttribute("exception", e.getMessage());
            return "redirect:/crm/subject";
        }

    }

    @GetMapping("/change-active-status/{id}")
    public String getChangeStatus(@PathVariable("id") Long id)   {
        Subject subject = subjectServiceImp.findById(id);
        if (subject.getIsActive()) {
            subject.setIsActive(false);
        } else {
            subject.setIsActive(true);
        }
        subjectServiceImp.save(subject);
        return "redirect:/crm/subject";
    }

    @GetMapping("/edit-name-status/{id}")
    public String getEditNameStatusPage(@PathVariable("id") Long id, Model model) {
        Subject subject = subjectServiceImp.findById(id);
        model.addAttribute("subject", subject);
        return "crm/subject/edit-name-status";
    }

    @PostMapping("/edit-name-status")
    public String getEditNameStatus(Subject subject, Model model,RedirectAttributes attributes) throws SubjectAlreadyExistException {

        try {
            subjectServiceImp.saveByException(subject);
            return "redirect:/crm/subject";
        } catch (SubjectAlreadyExistException e) {
            attributes.addFlashAttribute("exception", e.getMessage());
            return "redirect:/crm/subject";
        }
    }

}
