package pjatk.s18617.tournamentmanagement;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MariaDBContainer;

@TestConfiguration(proxyBeanMethods = false)
public class MyTestContainerConfig {

    @Bean
    @ServiceConnection // automatically creates connection details; necessary for Spring to use this container
    public MariaDBContainer<?> mariaDBContainer() {
        return new MariaDBContainer<>("mariadb:latest");
    }

}
