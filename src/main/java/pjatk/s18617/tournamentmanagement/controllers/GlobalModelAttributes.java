package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pjatk.s18617.tournamentmanagement.utils.UrlUtils;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("currentBarePath")
    public String currentBarePath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("currentPathWithoutMessage")
    public String currentPathWithoutMessage(HttpServletRequest request) {
        return UrlUtils.currentUrlWithoutMessage(request);
    }

    @ModelAttribute("currentPathWithoutCurrentPage")
    public String currentPathWithoutCurrentPage(HttpServletRequest request) {
        return UrlUtils.currentUrlWithoutCurrentPage(request);
    }

}
