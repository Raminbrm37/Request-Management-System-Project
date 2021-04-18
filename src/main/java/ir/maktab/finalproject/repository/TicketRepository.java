package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.model.ticket.Status;
import ir.maktab.finalproject.model.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByUser(User user);

    List<Ticket> findAllByUserAndStatus(User user, Status status);

    Integer countByUser(User user);
    Integer countTicketByUserAndStatus(User user,Status status);
}
