package ir.maktab.finalproject.repository;

import ir.maktab.finalproject.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findUserByRoleNotNull();

    User findByNationalCode(String nationalCode);
    User findByMobileNumber(String mobileNumber);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET  u.firstName= :firstName , u.lastName= :lastName ," +
            "u.nationalCode= :nationalCode , u.email= :email , u.mobileNumber= :mobileNumber " +
            "WHERE u.id= :id")
    void updateUserByFullInfo(

            @Param("firstName") String firstName,
            @Param("lastName") String firstname,
            @Param("nationalCode") String nationalCode,
            @Param("email") String email,
            @Param("mobileNumber") String mobileNumber,
            @Param("id") Long id
    );

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE  User u SET u.role_user= ?1 WHERE u.id= ?2")
    void updateUserRole(Long id, Long userId);



//User fiID()
}
