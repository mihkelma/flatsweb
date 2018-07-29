package controller;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.UserDetailsServiceImp;
import service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        model.addAttribute("message", "You are logged in as " + principal.getName());
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
        //System.out.println("Finding user: " + requestParams.get("username").toString());
        User userExists = userService.getUserByUsername(user.getUsername());

        if (userExists != null) {
            System.out.println("User found");
            model.addAttribute("error", "Oops! Try different ");
            bindingResult.reject("email");
        }

        if (bindingResult.hasErrors()) {
            System.out.println("Bindingresult had errors");
            return "register";
        } else {
            System.out.println("Saving user");
            User user2 = new User();
            user2.setUsername(user.getUsername());
            user2.setFirstName(user.getFirstName());
            user2.setLastName(user.getLastName());
            user2.setEnabled(false);

            // Generate random 36-character string token for confirmation link
            user2.setConfirmationToken(UUID.randomUUID().toString());

            System.out.println("Saving user with password: " + user.getPassword());
            user2.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            userService.save(user2);
            model.addAttribute("info", "Confirmation has been sent");
        }
        return "login";
    }

}
