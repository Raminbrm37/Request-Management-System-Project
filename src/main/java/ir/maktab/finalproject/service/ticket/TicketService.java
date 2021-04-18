package ir.maktab.finalproject.service.ticket;

import ir.maktab.finalproject.model.dto.TicketSearch;
import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.model.ticket.Status;
import ir.maktab.finalproject.model.ticket.Ticket;
import ir.maktab.finalproject.service.BaseEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TicketService extends BaseEntityService<Ticket, Long> {
    List<Ticket> findAllByUser(User user);

    List<Ticket> findAllByUserClosedStatus(User user);

    List<Ticket> findAllByUserHasBeenAnswerStatus(User user);

    List<Ticket> findAllByUserUnderConsideration(User user);

    @Transactional
    List<Ticket> getAdvancedSearch(TicketSearch ticketSearch);
    Page<Ticket> getAdvancedSearchPageable(TicketSearch ticketSearch, Pageable pageable);
    Integer countByUser(User user);

    Integer countTicketByUserAndStatusClosed(User user);
    Integer countTicketByUserAndStatusUnderConsideration(User user);
    Integer countTicketByUserAndStatusHasBeenAnswered(User user);
    String getUserTicket(Long id);

}
