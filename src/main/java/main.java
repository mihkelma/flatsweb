import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import service.EmailService;

public class main {
    public static void main(String[] args) {


        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo("Test");
        registrationEmail.setSubject("Registration Confirmation");
        registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
                + "http://localhost:8080" + "/confirm?token=" + "213132123eee");
        registrationEmail.setFrom("noreply@flats.ee");

        //emailService.sendEmail(registrationEmail);
    }
}
