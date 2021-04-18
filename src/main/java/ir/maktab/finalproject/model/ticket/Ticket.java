package ir.maktab.finalproject.model.ticket;

import ir.maktab.finalproject.model.BaseEntity;
import ir.maktab.finalproject.model.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Setter
@Getter
@ToString
public class Ticket extends BaseEntity<Long> {


    @ManyToOne
    @JoinColumn(name = "subject_id")

    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "user_id")

    private User user;

    @Column(nullable = false, length = 4000)
    private String description;

//    @OneToMany(mappedBy = "ticket")
//    private Set<Attachment> attachmentSet;

    @Column(name = "create_at", updatable = false)
    @CreationTimestamp
    private LocalDate createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    private LocalDateTime updateAt;


    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "document")
    private String document;
    @Column(name = "response")
    private String response;

    @PrePersist
    public void initStatus() {

        this.status = Status.UNDER_CONSIDERATION;
    }

    @Transient
    public String getDocumentPath() {
        if (document == null) return null;
        return "/customer-document/" + this.getId() + "/" + document;
    }

}
