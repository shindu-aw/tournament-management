package pjatk.s18617.tournamentmanagement.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StaticResourcesAvailabilityIT {

    @Autowired
    private MockMvc mockMvc;

    private void performResourceCheck(String url, String contentType, String expectedString) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(content().string(containsString(expectedString)));
    }

    @Test
    void returnWebJarBootstrapCss() throws Exception {
        performResourceCheck(
                "/webjars/bootstrap/css/bootstrap.min.css",
                "text/css",
                "bootstrap"
        );
    }

    @Test
    void returnWebJarBootstrapJs() throws Exception {
        performResourceCheck(
                "/webjars/bootstrap/js/bootstrap.bundle.min.js",
                "text/javascript",
                "bootstrap"
        );
    }

    @Test
    void returnThemeSwitchCss() throws Exception {
        performResourceCheck(
                "/css/theme-switch.css",
                "text/css",
                "theme-switch"
        );
    }

    @Test
    void returnThemeSwitchJs() throws Exception {
        performResourceCheck(
                "/js/theme-switch.js",
                "text/javascript",
                "darkModeSwitch"
        );
    }

    @Test
    void returnSvgCircleUserRound() throws Exception {
        performResourceCheck(
                "/images/circle-user-round.svg",
                "image/svg+xml",
                "lucide"
        );
    }

    @Test
    void returnSvgShieldUser() throws Exception {
        performResourceCheck(
                "/images/shield-user.svg",
                "image/svg+xml",
                "lucide"
        );
    }

}
