package ir.maktab.finalproject.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserSearch {

    private String firstName;
    private String lastName;
    private String username;
    private String mobileNumber;
    private String nationalCode;
    private String email;
    private Boolean isActive;

}
