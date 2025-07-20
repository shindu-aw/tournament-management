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
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorException = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object errorMessageAttribute = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        String messageH1 = "Coś poszło nie tak!";
        String messageH3 = "Nieznany błąd.";

        if (status != null) {
            String statusCodeString = status.toString();
            int statusCode = Integer.parseInt(statusCodeString);

            String detailedErrorMessage = getDetailedErrorMessage(statusCodeString, errorMessageAttribute,
                    errorException);

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                messageH1 = "Strona nie została odnaleziona";
                messageH3 = "";
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                messageH1 = "Nieprawidłowe żądanie";
                messageH3 = detailedErrorMessage;
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                messageH1 = "Wystąpił wewnętrzny błąd serwera";
                messageH3 = detailedErrorMessage;
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                messageH1 = "Brak uprawnień";
                messageH3 = detailedErrorMessage;
            } else {
                messageH3 = detailedErrorMessage;
            }

        }

        model.addAttribute("messageH1", messageH1);
        model.addAttribute("messageH3", messageH3);

        return "error";
    }

    private static String getDetailedErrorMessage(String statusCodeString, Object errorMessageAttribute,
                                                  Object errorException) {
        String detailedErrorMessage = "Kod błędu: " + statusCodeString;

        if (errorMessageAttribute != null) {
            // if there's a specific error message attribute, use it
            detailedErrorMessage += " - " + errorMessageAttribute;
        } else if (errorException instanceof Throwable throwable) {
            // if no specific error message, but exception is present, get its message
            detailedErrorMessage += " - " + throwable.getMessage();
        }
        return detailedErrorMessage;
    }

}
