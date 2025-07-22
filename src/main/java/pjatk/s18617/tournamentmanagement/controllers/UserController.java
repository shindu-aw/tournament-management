package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pjatk.s18617.tournamentmanagement.dtos.PasswordDto;
import pjatk.s18617.tournamentmanagement.dtos.UserEditDto;
import pjatk.s18617.tournamentmanagement.dtos.UserRegistrationDto;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.TeamUser;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.services.UserService;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "returnTo", required = false) String returnTo, Model model) {
        model.addAttribute("returnTo", returnTo);
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

    @GetMapping("/user/{userId}")
    public String showUser(@PathVariable Long userId, Model model) {
        User viewedUser = userService.findById(userId).orElseThrow(NotFoundException::new);

        model.addAttribute("user", viewedUser);
        model.addAttribute("teamsOwned", viewedUser.getTeamsOwned());
        model.addAttribute("teamRegistrations", viewedUser.getTeamRegistrations());
        model.addAttribute("tournamentsOwned", viewedUser.getTournamentsOwned());
        model.addAttribute("tournamentsManaged", viewedUser.getTournamentsManaged());

        return "user";
    }

    @GetMapping("/user/{userId}/edit")
    public String showEditUserForm(@PathVariable Long userId, Principal principal, Model model) {
        User editedUser = userService.findById(userId).orElseThrow(NotFoundException::new);

        String currentUserName = principal.getName();
        userService.checkEditUserAuthorization(editedUser, currentUserName);

        UserEditDto userEditDto = new UserEditDto();
        userEditDto.setDescription(editedUser.getDescription());

        model.addAttribute("userEditDto", userEditDto);
        model.addAttribute("editedUser", editedUser);

        return "user-edit";
    }

    @PostMapping("/user/{userId}/edit")
    public String processEditUserPasswordForm(@PathVariable Long userId, Principal principal, Model model,
                                              @Valid UserEditDto userEditDto, BindingResult result) {
        User editedUser = userService.findById(userId).orElseThrow(NotFoundException::new);

        if (result.hasErrors()) {
            model.addAttribute("editedUser", editedUser);
            return "user-edit";
        }

        String currentUserName = principal.getName();
        userService.updateUserWithAuthorization(editedUser, userEditDto, currentUserName);

        return "redirect:/user/" + userId;
    }

    @GetMapping("/user/{userId}/edit-password")
    public String showEditUserPasswordForm(@PathVariable Long userId, Principal principal, Model model) {
        User editedUser = userService.findById(userId).orElseThrow(NotFoundException::new);

        String currentUserName = principal.getName();
        userService.checkEditUserAuthorization(editedUser, currentUserName);

        model.addAttribute("passwordDto", new PasswordDto());
        model.addAttribute("editedUser", editedUser);

        return "user-edit-password";
    }

    @PostMapping("/user/{userId}/edit-password")
    public String processEditUserForm(@PathVariable Long userId, Principal principal, Model model,
                                      @Valid PasswordDto passwordDto, BindingResult result) {
        User editedUser = userService.findById(userId).orElseThrow(NotFoundException::new);

        if (!passwordDto.getPassword().equals(passwordDto.getPasswordRepeat()))
            result.rejectValue("passwordRepeat", "error.passwordRepeat", "hasła muszą być takie same");

        if (result.hasErrors()) {
            model.addAttribute("editedUser", editedUser);
            return "user-edit-password";
        }

        String currentUserName = principal.getName();
        userService.updateUserPasswordWithAuthorization(editedUser, passwordDto, currentUserName);

        return "redirect:/user/" + userId;
    }

    @GetMapping("/user/list")
    public String showUserList(Model model,
                               @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
                               @RequestParam(value = "username", required = false) String username,
                               @RequestParam(value = "teamName", required = false) String teamName
    ) {
        Page<User> usersPage = userService.searchPage(username, teamName, currentPage, 10);

        // page-related
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", usersPage.getTotalPages());

        // content
        model.addAttribute("users", usersPage.getContent());

        // search form values
        model.addAttribute("username", username);
        model.addAttribute("teamName", teamName);

        return "users-list";
    }

}
