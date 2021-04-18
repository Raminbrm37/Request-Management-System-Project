package ir.maktab.finalproject.controller.crm;

import ir.maktab.finalproject.model.dto.TicketSearch;
import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.model.ticket.Subject;
import ir.maktab.finalproject.model.ticket.Ticket;
import ir.maktab.finalproject.service.ticket.impl.SubjectServiceImp;
import ir.maktab.finalproject.service.ticket.impl.TicketServiceImpl;
import ir.maktab.finalproject.service.user.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/crm")
public class CRMController {
    @Autowired
    private SubjectServiceImp subjectServiceImp;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private TicketServiceImpl ticketService;
    @PreAuthorize("hasAuthority('OP_INDEX_CRM')")
    @GetMapping
    public String getMainPage(Principal principal, HttpServletRequest request, Model model
//            , @ModelAttribute("tickets") ArrayList<Ticket> tickets) {
    ) {
        User currentUser = userService.findByUserName(principal.getName());
        List<Subject> subjects = subjectServiceImp.findAllByIsActiveTrue();
        model.addAttribute("subjects", subjects);
        model.addAttribute("ticketSearch", new TicketSearch());
//        System.out.println(request.getAttribute("tickets"));
//        model.addAttribute("tickets", request.getAttribute("tickets"));
        request.getSession().setAttribute("me", currentUser);
        return "crm/crm-index";
    }
    @PreAuthorize("hasAuthority('OP_SEARCH_CRM')")
    @GetMapping("/advanced-search-ticket")
    public String getAdvancedSearchPage(@ModelAttribute("ticketSearch") TicketSearch ticketSearch,
                                        @PageableDefault(size = 5) Pageable pageable, Model model

    ) {

        Page<Ticket> tickets = ticketService.getAdvancedSearchPageable(ticketSearch, pageable);

        List<Subject> subjects = subjectServiceImp.findAllByIsActiveTrue();
        model.addAttribute("subjects", subjects);
        model.addAttribute("tickets", tickets);
        model.addAttribute("subjects", subjects);

        return "crm/crm-index";
    }
//        redirectAttributes.addFlashAttribute("tickets", tickets);
//        List<Subject> subjects = subjectServiceImp.findAllByIsActiveTrue();
//        model.addAttribute("subjects", subjects);
//        model.addAttribute("ticketSearch", new TicketSearch());
//        model.addAttribute("tickets", tickets);
//        return "crm/crm-index";}

//    } @PostMapping("/advanced-search-ticket")
//    public String getAdvancedSearchPage(TicketSearch ticketSearch, RedirectAttributes redirectAttributes) {
//        List<Ticket> tickets = ticketService.getAdvancedSearch(ticketSearch);
//        redirectAttributes.addFlashAttribute("tickets", tickets);
//        return "redirect:/crm";
//    }
@PreAuthorize("hasAuthority('OP_VIEW_TICKET_CRM')")
    @GetMapping("/view-ticket/{id}")
    public String getViewTicket(@PathVariable("id") Long id, Model model) {

        Ticket ticket = ticketService.findById(id);

        model.addAttribute("ticket", ticket);
        return "crm/view-ticket";

    }
    @PreAuthorize("hasAuthority('OP_VIEW_USER_INFO_CRM')")
    @GetMapping("/user-info/{id}/{tid}")
    public String getUserInfo(@PathVariable("id") Long id, @PathVariable("tid") Long tid, Model model) {
        User user = userService.findById(id);

        Integer closed = ticketService.countTicketByUserAndStatusClosed(user);
        Integer under = ticketService.countTicketByUserAndStatusUnderConsideration(user);
        Integer answered = ticketService.countTicketByUserAndStatusHasBeenAnswered(user);
        model.addAttribute("user", user);
        model.addAttribute("ticketId", tid);
        model.addAttribute("closed", closed);
        model.addAttribute("under", under);
        model.addAttribute("answered", answered);
        return "crm/user-info";
    }
    @PreAuthorize("hasAuthority('OP_ANSWER_TICKET_CRM')")
    @GetMapping("/answered-ticket/{id}")
    public String getAnsweredTicketPage(@PathVariable("id") Long id, Model model) {

        Ticket ticket = ticketService.findById(id);
        model.addAttribute("ticket", ticket);

        return "crm/answer-ticket";

    }
    @PreAuthorize("hasAuthority('OP_ANSWER_TICKET_CRM')")
    @PostMapping("/answered-ticket")
    public String getAnsweredTicket(Ticket ticket) {
        ticketService.save(ticket);
        return "redirect:/crm/view-ticket/" + ticket.getId();
    }
    @PreAuthorize("hasAuthority('OP_REQUEST_HANDLER_CRM')")
    @GetMapping("/request-handler")
    public String getRequestHandlerPage(Model model,
                                        @ModelAttribute("ticketSearch") TicketSearch ticketSearch) {
        List<Subject> subjects = subjectServiceImp.findAllByIsActiveTrue();
        model.addAttribute("subjects", subjects);
        model.addAttribute("ticketSearch", ticketSearch);
//        model.addAttribute("tickets", tickets);
        return "crm/request-handler";
    }
    @PreAuthorize("hasAuthority('OP_REQUEST_HANDLER_CRM')")
    @GetMapping("/request-handler-search")
    public String getRequestHandler(Model model,
                                    @ModelAttribute("ticketSearch") TicketSearch ticketSearch,
                                    RedirectAttributes attributes
            , @PageableDefault(size = 5) Pageable pageable) {
      ticketSearch.setStatusNull(false);
        Page<Ticket> tickets = ticketService.getAdvancedSearchPageable(ticketSearch,pageable );
        model.addAttribute("tickets", tickets);
        model.addAttribute("ticketSearch", ticketSearch);
        List<Subject> subjects = subjectServiceImp.findAllByIsActiveTrue();
        model.addAttribute("subjects", subjects);
        return "crm/request-handler";
    }
}
