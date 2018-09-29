import model.Contract;
import model.Invoice;
import model.InvoiceRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import service.EmailService;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;

public class main {
    public static void main(String[] args) {


//        SimpleMailMessage registrationEmail = new SimpleMailMessage();
//        registrationEmail.setTo("Test");
//        registrationEmail.setSubject("Registration Confirmation");
//        registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
//                + "http://localhost:8080" + "/confirm?token=" + "213132123eee");
//        registrationEmail.setFrom("noreply@flats.ee");

        //emailService.sendEmail(registrationEmail);

//        Contract c1 = new Contract();
//        System.out.println("Leping1 id: " + c1.getContractNumber());
//        Contract c2 = new Contract();
//
//        System.out.println("Leping2 id: " + c2.getContractNumber());


        //LocalDate date = LocalDate.now();
        //System.out.println(date);

        //String encoded=new BCryptPasswordEncoder().encode("user");
        //System.out.println(encoded);



    }
}
