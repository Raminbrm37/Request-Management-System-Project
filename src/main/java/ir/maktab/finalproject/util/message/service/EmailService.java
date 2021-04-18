package ir.maktab.finalproject.util.message.service;


import ir.maktab.finalproject.util.message.MessageContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService implements MessageService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public Boolean sendMessage(MessageContext emailService) throws MessagingException, UnsupportedEncodingException {


        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());


        Context context = new Context();

        context.setVariables(emailService.getContext());
        String content = templateEngine.process(emailService.getTemplateLocation(), context);

        helper.setFrom(emailService.getFrom());
        helper.setTo(emailService.getTo());
        helper.setSubject(emailService.getSubject());
        helper.setText(content, true);

        javaMailSender.send(message);

        System.out.println("Email has been sent");

        return true;
    }
}
