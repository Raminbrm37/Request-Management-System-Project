package ir.maktab.finalproject.model.ticket;


import ir.maktab.finalproject.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;

@Entity
@Setter
@Getter
@ToString
public class Subject extends BaseEntity<Long> {

    @Column(unique = true)
    private String name;


    private Boolean isActive;

    @PrePersist
    public void getDefaultIsActive() {
        this.isActive = false;
    }
}
