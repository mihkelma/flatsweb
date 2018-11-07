package controller;

import config.AppConfig;
import model.Role;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger logger = LogManager.getLogger(HomeController.class);

    //TODO: error management: http://blog.codeleak.pl/2014/06/better-error-messages-with-bean.html
    //https://nixmash.com/post/five-thymeleaf-format-examples
    //https://serverfault.com/questions/112795/how-to-run-a-server-on-port-80-as-a-normal-user-on-linux
    @GetMapping("/")
    public String index(Model model, Principal principal) {
        return "redirect:/units";
    }

    @GetMapping("/login")
    public String login() {
        logger.info("Starting login");
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
            String appUrl = request.getScheme() + "://" + request.getServerName();


            String subject = "Flats.ee konto aktiveerimine";
            String text = "Konto aktiveerimiseks palun kliki alloleval lingil:\n " + appUrl +
                      "/confirm?token=" + user.getConfirmationToken() + "\n\n Flats.ee meeskond";

            emailService.sendEmail(user.getUsername(), subject, text, "noreply@flats.ee", null);

            userService.save(user);
            model.addAttribute("error", "Konto aktiveerimiseks vaata palun oma postkasti ning kliki lingil");
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

        return "login";

    }

}
