package controller;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

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
    public String processRegistration(Model model, @Valid User user, BindingResult bindingResult) {
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

            //TODO: Add email sending with token
            
            userService.save(user);
            model.addAttribute("info", "Konto aktiveerimiseks ava palun meie saadetud email");
        }
        return "login";
    }

    @GetMapping("/confirm")
    public String processConfimration(Model model, @RequestParam("token") String token) {
        User user = userService.getUserByToken(token);

        if (user == null) { // No token found in DB
            model.addAttribute("error", "Oops! This is an invalid confirmation link.");
        } else { // Token found
            model.addAttribute("info", "Kasutaja aktiveeritud, palun logi sisse");

            // Set user to enabled
            user.setEnabled(true);

            // Save user
            userService.save(user);
        }

        return "login";

    }

}
