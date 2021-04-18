package ir.maktab.finalproject.model.entity;

import ir.maktab.finalproject.model.BaseEntity;

import ir.maktab.finalproject.util.token.CustomToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ir.maktab.finalproject.model.ticket.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
//@IdClass(User.class)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity<Long> implements UserDetails, Serializable {
    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id_user")
//    private Long id;
    @NotBlank(message = "نمیتواند خالی باشد")
    @Size(min = 6, max = 50, message = "اندازه نام کرابری باید بین 6 و 50 باشد ")
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @NotBlank(message = "نمیتواند خالی باشد")
    @Size(min = 2, max = 50, message = "اندازه  نام باید بین 2 و 50 باشد ")
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @NotBlank(message = "نمیتواند خالی باشد")

    @Size(min = 2, max = 50, message = "اندازه  نام خانوداگی باید بین 2 و 50 باشد ")
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @NotBlank(message = "نمیتواند خالی باشد")

    @Size(min = 6, message = "اندازه رمز عبور باید بیشتر از ۶ باشد  ")
    @Column(name = "password", nullable = false)

    private String password;
    @Transient
    private String retypePassword;
    @NotBlank(message = "نمیتواند خالی باشد")
    @Size(min = 11, max = 11, message = "فرمت شماره تماس صحیح نیست")
    @Column(name = "mobile_number", nullable = false, length = 11, unique = true)
    private String mobileNumber;
    @NotBlank(message = "نمیتواند خالی باشد")
    @Size(min = 10, max = 10, message = "فرمت کد ملی صحیح نیست")

    @Column(name = "national_code", nullable = false, length = 10, unique = true)
    private String nationalCode;
    @NotBlank(message = "نمیتواند خالی باشد")
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "user_role")
    private Role role;

    @OneToMany(mappedBy = "user")
    private Set<CustomToken> tokens;

    @OneToMany(mappedBy = "user")
    private Set<Ticket> tickets;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();


        authorities.addAll(role.getAuthorities());
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getIsActive();
    }


    public void addToken(CustomToken customToken) {
        tokens.add(customToken);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", nationalCode='" + nationalCode + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                ", role=" + role +
                '}';
    }
}
