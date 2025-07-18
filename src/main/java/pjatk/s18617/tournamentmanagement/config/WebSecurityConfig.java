package pjatk.s18617.tournamentmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.util.StringUtils;
import pjatk.s18617.tournamentmanagement.repositories.UserRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserRepository userRepository;

    public WebSecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // defines which URL paths should be secured and which should not
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/webjars/**",
                                "/images/**",
                                "/login**",
                                "/error",
                                "/",
                                "/home",
                                "/register",
                                "/game",
                                "/game/*/tournaments**",
                                "/team/list**",
                                "/user/list**"
                        ).permitAll()
                        .requestMatchers(
                                new RegexRequestMatcher("/game/\\d+", null), // /game/{id}
                                new RegexRequestMatcher("/tournament/\\d+", null), // /tournament/{id}
                                new RegexRequestMatcher("/user/\\d+", null), // /user/{id}
                                new RegexRequestMatcher("/team/\\d+", null) // /team/{id}
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler())
                        .permitAll()
                )
                .logout((logout) -> logout
                    .logoutSuccessHandler(customLogoutSuccessHandler())
                    .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // check for the saved request in the session (default behavior of the success handler)
            // this handles redirects from pages that automatically require authentication
            SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
            if (savedRequest != null) {
                // if present, redirect to the originally requested URL
                response.sendRedirect(savedRequest.getRedirectUrl());
            } else {
                // otherwise, continue to the custom returnTo parameter (from navbar login)
                String returnTo = request.getParameter("returnTo");
                if (StringUtils.hasText(returnTo))
                    response.sendRedirect(returnTo);
                else
                    response.sendRedirect("/"); // fallback
            }
        };
    }

    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler() {
        return (request, response, authentication) -> {
            String returnTo = request.getParameter("returnTo");
            if (StringUtils.hasText(returnTo))
                response.sendRedirect(returnTo);
            else
                response.sendRedirect("/"); // fallback
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
