package ir.maktab.finalproject.service.user;

import ir.maktab.finalproject.model.dto.UserSearch;
import ir.maktab.finalproject.model.entity.Role;
import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.service.BaseEntityService;
import ir.maktab.finalproject.util.exception.UserAlreadyExistException;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService extends BaseEntityService<User, Long> {
    User findByEmail(String email);

    void saveUserByAdmin(User user) throws UserAlreadyExistException;

    User findByUserName(String username);

    User findCurrentUser(String username);

    void register(User user, String siteURL) throws UnsupportedEncodingException, MessagingException, UserAlreadyExistException;

    void updateUserByFullInfo(User user,User currentUser) throws UserAlreadyExistException;

    void updateUserRole(Role role, Long userId);

    List<User> findUserByRoleNotNull();

    Boolean checkIfUserExistByUsername(String username);

    Boolean checkIfUserExistByEmail(String email);

    Boolean checkIfUserExistByNationalCode(String nationalCode);

    Boolean checkIfUserExistByMobileNumber(String mobileNumber);


    @Transactional
    List<User> getAdvancedSearch(UserSearch userSearch);


}
