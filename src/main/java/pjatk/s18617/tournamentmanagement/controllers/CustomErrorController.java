package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        String messageH1 = "Coś poszło nie tak!";
        String messageH2 = "Drużyna specjalnie wytrenowanych małp już się tym zajmuje!";

        if (status != null) {
            String statusCodeString = status.toString();
            int statusCode = Integer.parseInt(statusCodeString);

            String errorMessage = "Kod błędu: " + statusCodeString;
            model.addAttribute("errorMessage", errorMessage);

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                messageH1 = "Strona nie została odnaleziona";
                messageH2 = "";
            }

        }

        model.addAttribute("messageH1", messageH1);
        model.addAttribute("messageH2", messageH2);

        return "error";
    }

}
