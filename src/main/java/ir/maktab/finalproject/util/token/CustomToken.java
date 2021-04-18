package ir.maktab.finalproject.util.token;

import ir.maktab.finalproject.model.BaseEntity;
import ir.maktab.finalproject.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomToken extends BaseEntity<Long> {

    @Column(unique = true, nullable = false)
    private String name;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp timeStamp;

    @Column(updatable = false)
    @Basic(optional = false)
    private LocalDateTime expireAt;
    //    @Transient
    private Boolean isExpired;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_token")
    private User user;

    public boolean isExpired(){
        return getExpireAt().isBefore(LocalDateTime.now());
    }

}
