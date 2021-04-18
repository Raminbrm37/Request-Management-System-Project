package ir.maktab.finalproject.service.ticket.impl;

import ir.maktab.finalproject.model.dto.TicketSearch;
import ir.maktab.finalproject.model.dto.UserSearch;
import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.model.ticket.Status;
import ir.maktab.finalproject.model.ticket.Ticket;
import ir.maktab.finalproject.repository.TicketRepository;
import ir.maktab.finalproject.service.ticket.TicketService;
import ir.maktab.finalproject.service.user.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserServiceImpl userService;

    @Override
    public void save(Ticket entity) {
        ticketRepository.save(entity);
    }

    @Override
    public Ticket findById(Long id) {
        return ticketRepository.getOne(id);
    }

    @Override
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public void remove(Ticket entity) {
        ticketRepository.delete(entity);
    }

    @Override
    public void removeById(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public List<Ticket> findAllByUser(User user) {
        return ticketRepository.findAllByUser(user);
    }

    @Override
    public List<Ticket> findAllByUserClosedStatus(User user) {
        return ticketRepository.findAllByUserAndStatus(user, Status.CLOSED);
    }

    @Override
    public List<Ticket> findAllByUserHasBeenAnswerStatus(User user) {
        return ticketRepository.findAllByUserAndStatus(user, Status.HAS_BEEN_ANSWERED);
    }

    @Override
    public List<Ticket> findAllByUserUnderConsideration(User user) {
        return ticketRepository.findAllByUserAndStatus(user, Status.UNDER_CONSIDERATION);
    }


    @Override
    public Integer countByUser(User user) {
        return ticketRepository.countByUser(user);
    }

    @Override
    public Integer countTicketByUserAndStatusClosed(User user) {
        return ticketRepository.countTicketByUserAndStatus(user, Status.CLOSED);
    }

    @Override
    public Integer countTicketByUserAndStatusUnderConsideration(User user) {
        return ticketRepository.countTicketByUserAndStatus(user, Status.UNDER_CONSIDERATION);
    }

    @Override
    public Integer countTicketByUserAndStatusHasBeenAnswered(User user) {
        return ticketRepository.countTicketByUserAndStatus(user, Status.HAS_BEEN_ANSWERED);
    }

    @Override
    public List<Ticket> getAdvancedSearch(TicketSearch ticketSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ticket> query = criteriaBuilder.createQuery(Ticket.class);
//        CriteriaQuery<User> queryUser = criteriaBuilder.createQuery(User.class);
        Root<Ticket> from = query.from(Ticket.class);
//        Root<User> fromUser = queryUser.from(User.class);
        List<Predicate> predicates = getPredicates(from, criteriaBuilder, ticketSearch);
        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Page<Ticket> getAdvancedSearchPageable(TicketSearch ticketSearch, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ticket> query = criteriaBuilder.createQuery(Ticket.class);
//        CriteriaQuery<User> queryUser = criteriaBuilder.createQuery(User.class);
        Root<Ticket> from = query.from(Ticket.class);
//        Root<User> fromUser = queryUser.from(User.class);
        List<Predicate> predicates = getPredicates(from, criteriaBuilder, ticketSearch);
        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        TypedQuery<Ticket> typedQuery = entityManager.createQuery(query);
        int count = typedQuery.getResultList().size();
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());


        return new PageImpl<>(typedQuery.getResultList(), pageable, count);
    }

    private List<Predicate> getPredicates(Root<Ticket> from, CriteriaBuilder criteriaBuilder, TicketSearch ticketSearch) {
        List<Predicate> predicates = new ArrayList<>();
        if (ticketSearch.getUserSearch() != null) {
            setFirstname(ticketSearch.getUserSearch().getFirstName(), criteriaBuilder, from, predicates);
            setLastName(ticketSearch.getUserSearch().getLastName(), criteriaBuilder, from, predicates);
            setUsername(ticketSearch.getUserSearch().getUsername(), criteriaBuilder, from, predicates);
            setNationalCode(ticketSearch.getUserSearch().getNationalCode(), criteriaBuilder, from, predicates);
            setEmail(ticketSearch.getUserSearch().getEmail(), criteriaBuilder, from, predicates);
            setMobileNumber(ticketSearch.getUserSearch().getMobileNumber(), criteriaBuilder, from, predicates);
            setIsActive(ticketSearch.getUserSearch().getIsActive(), criteriaBuilder, from, predicates);
        }

        setSubject(ticketSearch.getSubject(), ticketSearch.getSubjectNull(), from, criteriaBuilder, predicates);
        setUser(ticketSearch.getId(), from, criteriaBuilder, predicates);
        setTime(ticketSearch.getTime(), from, criteriaBuilder, predicates);
        setUserSearch(ticketSearch.getUserSearch(), from, criteriaBuilder, predicates);
        setStatus(ticketSearch.getStatus(), ticketSearch.getStatusNull(), from, criteriaBuilder, predicates);
        return predicates;
    }

    private void setUserSearch(UserSearch userSearch, Root<Ticket> from, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {

    }

    private void setStatus(Status status, Boolean statusNull, Root<Ticket> from, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (!statusNull) {
            predicates.add(criteriaBuilder.equal(from.get("status"), status));
        }

    }

    private void setTime(LocalDate time, Root<Ticket> from, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {


        if (time != null) {
            predicates.add(
                    criteriaBuilder.equal(from.get("createAt"), time)
            );
        }
    }

    private void setUser(Long user, Root<Ticket> from, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (user != null) {
            predicates.add(criteriaBuilder.equal(from.get("user").get("id"), user));
        }
    }

    private void setSubject(String subject, Boolean subjectNone, Root<Ticket> from, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (!subjectNone) {
            predicates.add(
                    criteriaBuilder.equal(from.get("subject").get("name"), subject)
            );
        }
    }

    public void setIsActive(Boolean isActive, CriteriaBuilder criteriaBuilder, Root<Ticket> fromUser, List<Predicate> predicates) {
        if (isActive != null) {
            predicates.add(criteriaBuilder.equal(
                    fromUser.get("user").get("isActive"), isActive
            ));
        }

    }


    public void setEmail(String email, CriteriaBuilder criteriaBuilder, Root<Ticket> fromUser, List<Predicate> predicates) {
        if (email != null && !email.isEmpty()) {
            predicates.add(
                    criteriaBuilder.like(
                            fromUser.get("user").get("email"), "%" + email.trim() + "%"
                    )
            );
        }
    }

    public void setNationalCode(String nationalCode, CriteriaBuilder criteriaBuilder, Root<Ticket> fromUser, List<Predicate> predicates) {
        if (nationalCode != null && !nationalCode.isEmpty()) {
            predicates.add(
                    criteriaBuilder.like(
                            fromUser.get("user").get("nationalCode"), "%" + nationalCode.trim() + "%"
                    )
            );
        }
    }

    public void setMobileNumber(String mobileNumber, CriteriaBuilder criteriaBuilder, Root<Ticket> fromUser, List<Predicate> predicates) {
        if (mobileNumber != null && !mobileNumber.isEmpty()) {
            predicates.add(
                    criteriaBuilder.like(
                            fromUser.get("user").get("mobileNumber"), "%" + mobileNumber.trim() + "%"
                    )
            );
        }
    }

    public void setLastName(String lastName, CriteriaBuilder criteriaBuilder, Root<Ticket> fromUser, List<Predicate> predicates) {
        if (lastName != null && !lastName.isEmpty()) {
            predicates.add(
                    criteriaBuilder.like(fromUser.get("user").get("lastName"), "%" + lastName.trim() + "%")
            );
        }
    }

    public void setUsername(String username, CriteriaBuilder criteriaBuilder, Root<Ticket> fromUser, List<Predicate> predicates) {
        if (username != null && !username.isEmpty()) {
            predicates.add(
                    criteriaBuilder.like(fromUser.get("user").get("username"), "%" + username.trim() + "%")
            );
        }
    }

    public void setFirstname(String firstName, CriteriaBuilder criteriaBuilder, Root<Ticket> fromUser, List<Predicate> predicates) {
        if (firstName != null && !firstName.isEmpty()) {
            predicates.add(
                    criteriaBuilder.like(fromUser.get("user").get("firstName"), "%" + firstName.trim() + "%")
            );
        }
    }


    @PostAuthorize("returnObject==authentication.principal.username")
    public String getUserTicket(Long id) {
        User user = this.findById(id).getUser();
        return user.getUsername();
    }


}
