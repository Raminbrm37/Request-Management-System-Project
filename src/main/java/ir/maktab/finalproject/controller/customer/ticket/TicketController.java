package ir.maktab.finalproject.controller.customer.ticket;

import ir.maktab.finalproject.model.dto.TicketSearch;
import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.model.ticket.Status;
import ir.maktab.finalproject.model.ticket.Subject;
import ir.maktab.finalproject.model.ticket.Ticket;
import ir.maktab.finalproject.repository.SubjectRepository;
import ir.maktab.finalproject.service.ticket.TicketService;
import ir.maktab.finalproject.service.ticket.impl.SubjectServiceImp;
import ir.maktab.finalproject.service.user.impl.UserServiceImpl;
import ir.maktab.finalproject.util.file.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customer/ticket")
public class TicketController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SubjectServiceImp subjectServiceImp;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TicketService ticketService;

    @PreAuthorize("hasAuthority('OP_CREATE_TICKET')")
    @GetMapping("/create")
    public String getCreateTicketPage(Model model) {
        List<Subject> subjects = subjectServiceImp.findAllByIsActiveTrue();
        model.addAttribute("subjects", subjects);
        model.addAttribute("ticket", new Ticket());
        return "customer/create-ticket";
    }

    @PreAuthorize("hasAuthority('OP_CREATE_TICKET')")
    @PostMapping("/create")
    public String getCreateTicket(Ticket ticket, @SessionAttribute("me") User user,
                                  @RequestParam("file") MultipartFile multipartFile) throws IOException {
        ticket.setUser(user);
        ticketService.save(ticket);

        if (!multipartFile.getOriginalFilename().isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "./customer-document/" + ticket.getId();
            if (FileUpload.saveFile(uploadDir, fileName, multipartFile)) {
                ticket.setDocument(fileName);

            }
        }
        ticketService.save(ticket);
        return "redirect:/customer";
    }
    @PreAuthorize("hasAuthority('OP_UPDATE_TICKET')")
    @GetMapping("/update")
    public String getUpdateTicketPage(Long id, Model model) {
        Ticket ticket = ticketService.findById(id);
        if (!ticket.getStatus().equals(Status.UNDER_CONSIDERATION)) {

            return "customer";
        }
        List<Subject> subjects = subjectServiceImp.findAllByIsActiveTrue();
        model.addAttribute("subjects", subjects);
        model.addAttribute("ticket", ticket);
        return "customer/update-ticket";

    }
    @PreAuthorize("hasAuthority('OP_UPDATE_TICKET')")
    @PostMapping("/update")
    public String getUpdateTicket(Ticket ticket) {
        ticketService.save(ticket);
        return "redirect:/customer";

    }
    @PreAuthorize("hasAuthority('OP_UPDATE_DOCUMENT_TICKET')")
    @GetMapping("/edit-document")
    public String getEditDocumentPage(Long id, Model model) {
        Ticket ticket = ticketService.findById(id);
        model.addAttribute("ticket", ticket);
        return "customer/update-document";

    }

    @PreAuthorize("hasAuthority('OP_UPDATE_DOCUMENT_TICKET')")
    @GetMapping("/delete-document")
    public String getDeleteDocumentPage(Long id, Model model) {
        Ticket ticket = ticketService.findById(id);
        ticket.setDocument(null);
        ticketService.save(ticket);

        return "redirect:/customer";

    }
    @PreAuthorize("hasAuthority('OP_UPDATE_DOCUMENT_TICKET')")
    @PostMapping("/update-document")
    public String getDeleteDocument(Long id, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        Ticket ticket = ticketService.findById(id);


        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = "./customer-document/" + ticket.getId();
        if (FileUpload.saveFile(uploadDir, fileName, multipartFile)) {
            ticket.setDocument(fileName);

        }
        ticketService.save(ticket);
        return "redirect:/customer";
    }
    @PreAuthorize("hasAuthority('OP_CLOSE_TICKET')")
    @GetMapping("/closed-ticket")
    public String getClosedTicket(Long id) {
        Ticket ticket = ticketService.findById(id);
        ticket.setStatus(Status.CLOSED);
        ticketService.save(ticket);
        return "redirect:/customer/ticket/step-two";

    }
    @PreAuthorize("hasAuthority('OP_STEP_TICKET')")
    @GetMapping("/step-three")
    public String getListClosedTicket(@SessionAttribute("me") User user, Model model) {
        List<Ticket> tickets = ticketService.findAllByUserClosedStatus(user);
        model.addAttribute("tickets", tickets);
        return "customer/step-three-ticket";

    }
    @PreAuthorize("hasAuthority('OP_STEP_TICKET')")
    @GetMapping("/step-two")
    public String getListAnswerTicket(@SessionAttribute("me") User user, Model model) {
        List<Ticket> tickets = ticketService.findAllByUserHasBeenAnswerStatus(user);
        model.addAttribute("tickets", tickets);
        return "customer/step-two-ticket";

    }
    @PreAuthorize("hasAuthority('OP_STEP_TICKET')")
    @GetMapping("/step-one")
    public String getTicketUnderConsideration(@SessionAttribute("me") User user, Model model) {
        List<Ticket> tickets = ticketService.findAllByUserUnderConsideration(user);
        model.addAttribute("tickets", tickets);
        return "customer/step-one-ticket";

    }
    @PreAuthorize("hasAuthority('OP_STEP_TICKET')")
    @GetMapping("/advanced-search")
    public String getAdvancedSearchPage(Model model, @ModelAttribute("tickets") ArrayList<Ticket> tickets) {
        List<Subject> subjects = subjectServiceImp.findAllByIsActiveTrue();
        model.addAttribute("tickets", tickets);
        model.addAttribute("subjects", subjects);
        model.addAttribute("ticketSearch", new TicketSearch());
        return "customer/advanced-search-ticket";
    }

    @PreAuthorize("hasAuthority('OP_SEARCH_TICKET')")
    @PostMapping("/advanced-search")
    public String getAdvancedSearch(TicketSearch ticketSearch, @SessionAttribute("me") User user
            , RedirectAttributes redirectAttributes) {

        ticketSearch.setId(user.getId());

        List<Ticket> tickets = ticketService.getAdvancedSearch(ticketSearch);
        redirectAttributes.addFlashAttribute("tickets", tickets);

        return "redirect:/customer/ticket/advanced-search";
//        return "redirect:/customer/ticket/advanced-search";
    }
    @PreAuthorize("hasAuthority('OP_DELETE_TICKET')")
    @GetMapping("/delete/{id}")
    public String getDeleteTicket(@PathVariable("id") Long id) {
        ticketService.getUserTicket(id);
        ticketService.removeById(id);
        return "redirect:/customer";
    }

}
