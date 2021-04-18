package ir.maktab.finalproject.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@ToString
//@IdClass(value = BaseEntity.class)
public class BaseEntity<T extends Serializable>implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")

    private T id;

}
