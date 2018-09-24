package controller;

import model.Role;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.EmailService;
import service.RoleService;
import service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        return "redirect:/units";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model, User user){
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(Model model, @Valid User user, BindingResult bindingResult, HttpServletRequest request) {
        // Lookup user in database by e-mail
        User userExists = userService.getUserByUsername(user.getUsername());
        System.out.println("User: " + user.getUsername() + ", " + user.getFullName());

        //TODO: if user parameters are empty, show error on web
        if (userExists != null) {
            model.addAttribute("error", "Sisestatud email on juba registreeritud!");
            bindingResult.reject("email");
        }

        if (bindingResult.hasErrors()) {
            //model.addAttribute("error", "Kõik väljad on kohustuslikud!");
            return "register";
        } else {
            //user needs to confirm email, before using the account
            user.setEnabled(false);

            // Generate random 36-character string token for confirmation link
            user.setConfirmationToken(UUID.randomUUID().toString());

            //Set default role to new user
            Role newRole = roleService.getRoleByRoleName("ROLE_USER");
            user.setRoles(new HashSet<>(Arrays.asList(newRole)));

            //Email token sending, for account activation
            //Email - https://www.codebyamir.com/blog/user-account-registration-with-spring-boot
            //Registration - https://www.jackrutorial.com/2018/04/spring-boot-user-registration-login.html
            String appUrl = request.getScheme() + "://" + request.getServerName() +":"+ request.getServerPort();

            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getUsername());
            registrationEmail.setSubject("Flats.ee konto aktiveerimine");
            registrationEmail.setText("Konto aktiveerimiseks palun kliki alloleval lingil:\n"
                    + appUrl + "/confirm?token=" + user.getConfirmationToken() + "\n\n Flats.ee meeskond");
            registrationEmail.setFrom("noreply@flats.ee");

            emailService.sendEmail(registrationEmail);

            userService.save(user);
            model.addAttribute("info", "Konto aktiveerimiseks vaata palun oma postkasti ning kliki lingil");
        }
        return "login";
    }

    @GetMapping("/confirm")
    public String processConfirmation(Model model, @RequestParam("token") String token) {
        //TODO: add custom access denied handler if not param available
        //TODO: add option to enter the token on the web page
        if (token == null) {
            model.addAttribute("error", "See link on kehtetu.");
        }
        User user = userService.getUserByToken(token);

        if (user == null) { // No token found in DB
            model.addAttribute("error", "See link on kehtetu.");
        } else { // Token found
            // Set user to enabled
            user.setEnabled(true);
            // Save user
            userService.save(user);
            //Send notification to the view
            model.addAttribute("info", "Kasutaja on aktiveeritud, palun logi sisse");
        }

        return "confirm";

    }

}
