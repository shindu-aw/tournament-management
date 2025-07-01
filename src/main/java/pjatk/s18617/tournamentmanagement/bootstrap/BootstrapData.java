package pjatk.s18617.tournamentmanagement.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.UserRepository;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadUserData();
    }

    private void loadUserData() {
        if (userRepository.count() == 0) {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            User user1 = User.builder()
                    .username("testUser")
                    .password(encoder.encode("testPassword"))
                    .description("Test description.")
                    .build();

            User user2 = User.builder()
                    .username("testUser2")
                    .password(encoder.encode("testPassword2"))
                    .description("Test description 2.")
                    .build();

            userRepository.saveAll(Arrays.asList(user1, user2));
        }
    }

}
