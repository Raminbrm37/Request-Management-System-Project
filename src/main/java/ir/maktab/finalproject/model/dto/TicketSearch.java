package ir.maktab.finalproject.model.dto;

import ir.maktab.finalproject.model.ticket.Status;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketSearch {
    private String subject;
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private LocalDate time;
    private Status status;
    private Long id;
    private Boolean subjectNull;
    private Boolean statusNull;
    private UserSearch userSearch;
}
