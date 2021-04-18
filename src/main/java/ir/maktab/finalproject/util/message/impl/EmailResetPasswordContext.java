package ir.maktab.finalproject.util.message.impl;


import ir.maktab.finalproject.model.entity.User;
import ir.maktab.finalproject.util.message.MessageContext;

public class EmailResetPasswordContext extends MessageContext {
    private String token;

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    @Override
    public <T> void init(T context) {
        User user = (User) context;
        put("firstName", user.getFirstName());
//        setTemplateLocation("email/forgot-password.html");
        setTemplateLocation("email/forgot-password.html");
        setSubject("Reset Password");
        setFrom("Ramin.brm37@gmail.com");
        setTo(user.getEmail());

    }
    public void buildVerificationUrl( String baseURL,  String token){
        String verifyURL = baseURL + "/reset-password-verify?token=" + token;
//         String url= UriComponentsBuilder.fromHttpUrl(baseURL)
//                .path("/register/verify").queryParam("token", token).toUriString();
        put("verificationURL", verifyURL);
    }
}
