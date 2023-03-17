package tiimae.webshop.iprwc.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService extends Thread {

    @Autowired private JavaMailSender emailSender;

    private String subject;
    private String to;
    private String body;

    public void sendMessage() throws MessagingException {
        MimeMessage mail = this.emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);

        // rewrite this with a template
        String text = "<html>" +
                "<head>" +
                "</head>" +
                "<body style=\"display: flex; flex-direction: column; padding: 30px;\">" +
                "<div style=\"display: flex; flex-direction: column; width: 100%; font-family: sans-serif; text-align: center; \">" +
                "<h1 style=\"margin: 50px 0 20px 0;\">" +
                "" + this.subject + "</h1>" +
                "<hr style=\"border: 1px block solid\">" +
                "<div style=\"margin: 0;\">" +
                "" + this.body + "" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        helper.setFrom("de.kok.ac@gmail.com");
        helper.setTo(this.to);
        helper.setSubject(this.subject);
        helper.setText(text, true);

        this.emailSender.send(mail);
    }

    public void run() {
        try {
            this.sendMessage();
            Thread.currentThread().interrupt();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setData(String subject, String to, String body) {
        this.setBody(body);
        this.setSubject(subject);
        this.setTo(to);
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
