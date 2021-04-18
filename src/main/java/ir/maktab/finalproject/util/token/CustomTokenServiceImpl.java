package ir.maktab.finalproject.util.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CustomTokenServiceImpl implements CustomTokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public CustomToken createCustomToken() {
        String tokenName = UUID.randomUUID().toString();
        CustomToken token = new CustomToken();
        token.setName(tokenName);
        token.setExpireAt(LocalDateTime.now().plusMinutes(15));
        this.save(token);
        return token;
    }

    @Override
    public CustomToken findByName(String name) {
        return tokenRepository.findByName(name);
    }

    @Override
    public void removeByName(String name) {
        tokenRepository.removeByName(name);
    }

    @Override
    public void save(CustomToken entity) {
        tokenRepository.save(entity);
    }

    @Override
    public CustomToken findById(Long id) {
        return tokenRepository.getOne(id);
    }

    @Override
    public List<CustomToken> findAll() {
        return null;
    }

    @Override
    public void remove(CustomToken entity) {
        tokenRepository.delete(entity);
    }

    @Override
    public void removeById(Long id) {
        tokenRepository.deleteById(id);
    }
}
