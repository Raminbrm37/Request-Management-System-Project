package ir.maktab.finalproject.util.token;

import ir.maktab.finalproject.util.token.CustomToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<CustomToken, Long> {
    CustomToken findByName(String token);
    Long removeByName(String token);
}
