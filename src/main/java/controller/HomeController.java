package controller;

import model.Role;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        model.addAttribute("message", "Kasutaja: " + principal.getName());
        return "index";
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
    //public String processRegistration(Model model, BindingResult bindingResult, @RequestParam Map requestParams) {
    public String processRegistration(Model model, @Valid User user, BindingResult bindingResult, HttpServletRequest request) {
        // Lookup user in database by e-mail
        User userExists = userService.getUserByUsername(user.getUsername());

        if (userExists != null) {
            model.addAttribute("error", "Ohoo! Email on juba kasutusel!");
            bindingResult.reject("email");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        } else {
            //user needs to confirm email, before using the account
            user.setEnabled(false);

            // Generate random 36-character string token for confirmation link
            user.setConfirmationToken(UUID.randomUUID().toString());

            //TODO: user.setRoles(Arrays.asList("ROLE_USER"));
            Role newRole = roleService.getRoleByRoleName("ROLE_USER");
            user.setRoles(new HashSet<>(Arrays.asList(newRole)));

            //TODO: Add email sending with token
            //Email - https://www.codebyamir.com/blog/user-account-registration-with-spring-boot
            //Registration - https://www.jackrutorial.com/2018/04/spring-boot-user-registration-login.html
            String appUrl = request.getScheme() + "://" + request.getServerName() +":"+ request.getServerPort();

            SimpleMailMessage registrationEmail = new SimpleMailMessage();
            registrationEmail.setTo(user.getUsername());
            registrationEmail.setSubject("Registration Confirmation");
            registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
                    + appUrl + "/confirm?token=" + user.getConfirmationToken());
            registrationEmail.setFrom("noreply@flats.ee");

            emailService.sendEmail(registrationEmail);

            userService.save(user);
            model.addAttribute("info", "Konto aktiveerimiseks ava palun meie saadetud email");
        }
        return "login";
    }

    @GetMapping("/confirm")
    public String processConfimration(Model model, @RequestParam("token") String token) {
        User user = userService.getUserByToken(token);

        if (user == null) { // No token found in DB
            model.addAttribute("error", "Oih! See link on kehtetu.");
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
