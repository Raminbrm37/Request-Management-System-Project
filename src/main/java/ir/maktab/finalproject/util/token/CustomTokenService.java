package ir.maktab.finalproject.util.token;

import ir.maktab.finalproject.service.BaseEntityService;

public interface CustomTokenService extends BaseEntityService<CustomToken, Long> {

    CustomToken createCustomToken();

    CustomToken findByName(String name);

    void removeByName(String name);
}
