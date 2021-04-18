package ir.maktab.finalproject.service.user.impl;

import ir.maktab.finalproject.model.dto.UserSearch;
import ir.maktab.finalproject.model.entity.Role;
import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.repository.UserRepository;
import ir.maktab.finalproject.service.user.UserService;
import ir.maktab.finalproject.util.exception.UserAlreadyExistException;
import ir.maktab.finalproject.util.message.impl.EmailResetPasswordContext;
import ir.maktab.finalproject.util.message.impl.EmailVerificationContext;
import ir.maktab.finalproject.util.message.service.EmailService;
import ir.maktab.finalproject.util.token.CustomToken;
import ir.maktab.finalproject.util.token.CustomTokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomTokenServiceImpl customTokenService;
    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private EmailService messageService;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserByAdmin(User user) throws UserAlreadyExistException {
        if (this.checkIfUserExistByUsername(user.getUsername())) {
            throw new UserAlreadyExistException("User already exists for this username :" + user.getUsername());
        } else if (this.checkIfUserExistByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("User already exists for this Email :" + user.getEmail());
        } else if (this.checkIfUserExistByMobileNumber(user.getMobileNumber())) {
            throw new UserAlreadyExistException("User already exists for this MobileNumber :" + user.getMobileNumber());
        } else if (this.checkIfUserExistByNationalCode(user.getNationalCode())) {
            throw new UserAlreadyExistException("User already exists for this NationalCode :" + user.getNationalCode());
        } else {
            userRepository.save(user);
        }

    }

    @Override
    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @PreAuthorize("#username==authentication.principal.username")
    @Override
    public User findCurrentUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void register(User user, String siteURL) throws UnsupportedEncodingException, MessagingException, UserAlreadyExistException {
        passwordEncoding(user);
        CustomToken customToken = customTokenService.createCustomToken();

        user.setIsActive(false);
        if (this.checkIfUserExistByUsername(user.getUsername())) {
            throw new UserAlreadyExistException("User already exists for this username :" + user.getUsername());
        } else if (this.checkIfUserExistByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("User already exists for this Email :" + user.getEmail());
        } else if (this.checkIfUserExistByMobileNumber(user.getMobileNumber())) {
            throw new UserAlreadyExistException("User already exists for this MobileNumber :" + user.getMobileNumber());
        } else if (this.checkIfUserExistByNationalCode(user.getNationalCode())) {
            throw new UserAlreadyExistException("User already exists for this NationalCode :" + user.getNationalCode());
        }

        sendCustomTokenVerificationViaEmail(user, customToken);
    }

    @Override
    public void updateUserByFullInfo(User user, User currentUser) throws UserAlreadyExistException {
//        if (!user.getUsername().equals(currentUser.getUsername().trim())) {
//            if (this.checkIfUserExistByUsername(user.getUsername())) {
//                throw new UserAlreadyExistException("User already exists for this username :" + user.getUsername());
//            }
//        }
        if (!user.getEmail().equals(currentUser.getEmail().trim())) {
            if (this.checkIfUserExistByEmail(user.getEmail())) {
                throw new UserAlreadyExistException("User already exists for this Email :" + user.getEmail());
            }
        }
        if (!user.getNationalCode().equals(currentUser.getNationalCode().trim())) {
            if (this.checkIfUserExistByNationalCode(user.getNationalCode())) {
                throw new UserAlreadyExistException("User already exists for this NationalCode :" + user.getNationalCode());
            }
        }
        if (!user.getMobileNumber().equals(currentUser.getMobileNumber().trim())) {
            if (this.checkIfUserExistByMobileNumber(user.getMobileNumber())) {
                throw new UserAlreadyExistException("User already exists for this MobileNumber :" + user.getMobileNumber());
            }
        }

        userRepository.updateUserByFullInfo(user.getFirstName(),
                user.getLastName(),
                user.getNationalCode(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getId());
    }


    @Override
    public void updateUserRole(Role role, Long userId) {
        roleService.save(role);
//userRepository.updateUserRole(role.getId(),userId);
        User user = this.findById(userId);
//        Role role1=roleService.findById(2L);
//        user.setRole(role);
        user.setFirstName("2221");
        userRepository.save(user);

    }

    @Override
    public List<User> findUserByRoleNotNull() {
        return userRepository.findUserByRoleNotNull();
    }

    @Override
    public Boolean checkIfUserExistByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public Boolean checkIfUserExistByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public Boolean checkIfUserExistByNationalCode(String nationalCode) {
        return userRepository.findByNationalCode(nationalCode) != null;
    }

    @Override
    public Boolean checkIfUserExistByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber) != null;
    }


    private void sendCustomTokenVerificationViaEmail(User user, CustomToken customToken) throws UnsupportedEncodingException, MessagingException {


        EmailVerificationContext email = new EmailVerificationContext();
        email.init(user);
        email.setToken(customToken.getName());
        email.buildVerificationUrl("http://localhost:8082", customToken.getName());
        if (messageService.sendMessage(email)) {
            userRepository.save(user);
            customToken.setUser(user);
            customTokenService.save(customToken);
        }

    }

    public Boolean verify(String verifyCode) {
        CustomToken token = customTokenService.findByName(verifyCode);
        User user = userRepository.getOne(token.getUser().getId());
        if (token == null || token.isExpired()) {

            return false;
        } else {
            user.setIsActive(true);
            Role role = roleService.findById(2L);
            roleService.save(role);
            user.setRole(role);
            userRepository.save(user);
            customTokenService.remove(token);
            return true;
        }


    }


    private void passwordEncoding(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

    }

    @Override
    public void save(User entity) {
        userRepository.save(entity);
    }

    @Override
    public User findById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public List<User> findAll() {

        return userRepository.findAll();
    }

    @Override
    public void remove(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public void removeById(Long id) {
        userRepository.deleteById(id);
    }

    public void changeUserStatus(String username) {
        User user = this.findByUserName(username);
        if (user.getIsActive()) {
            user.setIsActive(false);
        } else {
            user.setIsActive(true);
        }
        this.save(user);
    }

    public void changePasswordUser(User user, String password) {
        String encode = passwordEncoder.encode(password);
        user.setPassword(encode);
        userRepository.save(user);
    }

    public void sendVerificationTokenByEmail(User user) throws UnsupportedEncodingException, MessagingException {
        CustomToken customToken = customTokenService.createCustomToken();
        EmailResetPasswordContext context = new EmailResetPasswordContext();
        context.init(user);
        context.setToken(customToken.getName());
        context.buildVerificationUrl("http://localhost:8082", customToken.getName());
        if (messageService.sendMessage(context)) {
            userRepository.save(user);
            customToken.setUser(user);
            customTokenService.save(customToken);
        }
    }

    @Override
    public List<User> getAdvancedSearch(UserSearch userSearch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> fromUser = criteriaQuery.from(User.class);
        List<Predicate> predicates = getPredicates(userSearch, criteriaBuilder, fromUser);
        criteriaQuery.where(
                criteriaBuilder.and(
                        predicates.toArray(
                                new Predicate[0]
                        )
                )
        );
        List<User> list = entityManager.createQuery(criteriaQuery).getResultList();
        return list;
    }

    public List<Predicate> getPredicates(UserSearch userSearch, CriteriaBuilder criteriaBuilder, Root<User> fromUser) {
        List<Predicate> predicates = new ArrayList<>();
        setFirstname(userSearch.getFirstName(), criteriaBuilder, fromUser, predicates);
        setLastName(userSearch.getLastName(), criteriaBuilder, fromUser, predicates);
        setUsername(userSearch.getUsername(), criteriaBuilder, fromUser, predicates);
        setMobileNumber(userSearch.getMobileNumber(), criteriaBuilder, fromUser, predicates);
        setNationalCode(userSearch.getNationalCode(), criteriaBuilder, fromUser, predicates);
        setEmail(userSearch.getEmail(), criteriaBuilder, fromUser, predicates);
        setIsActive(userSearch.getIsActive(), criteriaBuilder, fromUser, predicates);
        notRoleNull(criteriaBuilder, fromUser, predicates);
        return predicates;
    }

    public void setIsActive(Boolean isActive, CriteriaBuilder criteriaBuilder, Root<User> fromUser, List<Predicate> predicates) {
        predicates.add(criteriaBuilder.equal(
                fromUser.get("isActive"), isActive
        ));

    }

    public void notRoleNull(CriteriaBuilder criteriaBuilder, Root<User> fromUser, List<Predicate> predicates) {
        predicates.add(
                criteriaBuilder.isNotNull(fromUser.get("role"))
        );
    }

    public void setEmail(String email, CriteriaBuilder criteriaBuilder, Root<User> fromUser, List<Predicate> predicates) {
        if (email != null && !email.isEmpty()) {
            predicates.add(
                    criteriaBuilder.like(
                            fromUser.get("email"), "%" + email.trim() + "%"
                    )
            );
        }
    }

    public void setNationalCode(String nationalCode, CriteriaBuilder criteriaBuilder, Root<User> fromUser, List<Predicate> predicates) {
        if (nationalCode != null && !nationalCode.isEmpty()) {
            predicates.add(
                    criteriaBuilder.like(
                            fromUser.get("nationalCode"), "%" + nationalCode.trim() + "%"
                    )
            );
        }
    }

    public void setMobileNumber(String mobileNumber, CriteriaBuilder criteriaBuilder, Root<User> fromUser, List<Predicate> predicates) {
        if (mobileNumber != null && !mobileNumber.isEmpty()) {
            predicates.add(
                    criteriaBuilder.like(
                            fromUser.get("mobileNumber"), "%" + mobileNumber.trim() + "%"
                    )
            );
        }
    }

    public void setLastName(String lastName, CriteriaBuilder criteriaBuilder, Root<User> fromUser, List<Predicate> predicates) {
        if (lastName != null && !lastName.isEmpty()) {
            predicates.add(
                    criteriaBuilder.like(fromUser.get("lastName"), "%" + lastName.trim() + "%")
            );
        }
    }

    public void setUsername(String username, CriteriaBuilder criteriaBuilder, Root<User> fromUser, List<Predicate> predicates) {
        if (username != null && !username.isEmpty()) {
            predicates.add(
                    criteriaBuilder.like(fromUser.get("username"), "%" + username.trim() + "%")
            );
        }
    }

    public void setFirstname(String firstName, CriteriaBuilder criteriaBuilder, Root<User> fromUser, List<Predicate> predicates) {
        if (firstName != null && !firstName.isEmpty()) {
            predicates.add(
                    criteriaBuilder.like(fromUser.get("firstName"), "%" + firstName.trim() + "%")
            );
        }
    }


}
