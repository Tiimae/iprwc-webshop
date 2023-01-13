package tiimae.webshop.iprwc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

//    public EmailService(JavaMailSender emailSender) {
//        this.emailSender = emailSender;
//    }

    public void sendMessage(String to, String subject, String body) throws MessagingException {
        MimeMessage mail = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);

        // rewrite this with a template
        String text = "<html><head></head><body style=\"display: flex; flex-direction: column; padding: 30px;\"><div style=\"display: flex; flex-direction: column; width: 100%; font-family: sans-serif; text-align: center; \"><h1 style=\"margin: 50px 0 20px 0;\">"+subject+"</h1><hr style=\"border: 1px block solid\"><div style=\"margin: 0;\">"+body +"</div></div></body></html>";

        helper.setFrom("de.kok.ac@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);


        emailSender.send(mail);
    }
}
