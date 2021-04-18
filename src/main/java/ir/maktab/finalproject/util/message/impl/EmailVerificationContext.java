package ir.maktab.finalproject.util.message.impl;


import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.util.message.MessageContext;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailVerificationContext extends MessageContext {
    String token;

    @Override
    public <T> void init(T context) {
        User user = (User) context;
        put("firstName", user.getFirstName());
        setTemplateLocation("test/test.html");
        setSubject("Complete our Registration");
        setFrom("Ramin.brm37@gmail.com");
        setTo(user.getEmail());

    }
    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }
    public void buildVerificationUrl( String baseURL,  String token){
        String verifyURL = baseURL + "/register/verify?code=" + token;
        put("verificationURL", verifyURL);
    }

}
