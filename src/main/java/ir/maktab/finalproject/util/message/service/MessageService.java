package ir.maktab.finalproject.util.message.service;



import ir.maktab.finalproject.util.message.MessageContext;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MessageService {
Boolean sendMessage(MessageContext messageContext) throws MessagingException, UnsupportedEncodingException;
}
