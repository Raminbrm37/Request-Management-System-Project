package ir.maktab.finalproject.controller.customer;

import ir.maktab.finalproject.model.entity.Authority;
import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.model.ticket.Ticket;
import ir.maktab.finalproject.service.ticket.impl.TicketServiceImpl;
import ir.maktab.finalproject.service.user.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private UserServiceImpl userService;


    @Autowired
    private TicketServiceImpl ticketService;


@PreAuthorize("hasAuthority('OP_INDEX_TICKET')")
    @GetMapping
    public String getMainPage(Model model, Principal principal, HttpServletRequest request) {
        User currentUser = userService.findByUserName(principal.getName());

        List<Ticket> tickets = ticketService.findAllByUser(currentUser);

        model.addAttribute("tickets", tickets);
        request.getSession().setAttribute("me", currentUser);
        model.addAttribute("ticket", new Ticket());
        return "customer/user-index";
    }

}
