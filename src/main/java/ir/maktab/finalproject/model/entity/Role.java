package ir.maktab.finalproject.model.entity;

import ir.maktab.finalproject.model.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
//@IdClass(value = Role.class)
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity<Long>implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id_role")
//    private Long id;
    @Column(unique = true)
    private String name;

    @ElementCollection(targetClass = ir.maktab.finalproject.model.entity.Authority.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Authority> authorities;
//    @OneToMany(mappedBy = "role")
//    private Set<User> userSet;

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
