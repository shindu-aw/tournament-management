package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pjatk.s18617.tournamentmanagement.dtos.UserRegistrationDto;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.services.UserService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping(value = {"/", "/home"})
    public String home() {
        return "home";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid UserRegistrationDto userRegistrationDto, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        userService.register(userRegistrationDto);
        return "redirect:/login";
    }

}
