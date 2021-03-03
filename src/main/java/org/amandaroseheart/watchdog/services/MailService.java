package org.amandaroseheart.watchdog.services;

import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;

@Service
public class MailService {

  Properties prop = new Properties();

  @PostConstruct
  public void init() {
    prop.put("mail.smtp.auth", true);
    prop.put("mail.smtp.starttls.enable", "true");
    prop.put("mail.smtp.host", "smtp.mailtrap.io");
    prop.put("mail.smtp.port", "587");
    prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
  }

  public void sendMail(String msg) throws Exception {
    Session session = createSession();
    Message message = createMessage(msg, session);
    Transport.send(message);
  }

  private Session createSession() {
    return Session.getInstance(prop, new Authenticator() {
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("ddfe6e9c7b1413", "9dcaa9c3b06b4e");
          }
        }
    );
  }

  private Message createMessage(String msg, Session session)
      throws MessagingException {

    Message message = new MimeMessage(session);

    message.setFrom(new InternetAddress("watchdog@gmail.com"));
    message.setRecipients(Message.RecipientType.TO,
        InternetAddress.parse("to@gmail.com"));

    message.setSubject("Notification from Watchdog");

    MimeBodyPart mimeBodyPart = new MimeBodyPart();
    mimeBodyPart.setContent(msg, "text/html");

    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(mimeBodyPart);

    message.setContent(multipart);
    return message;
  }

}
