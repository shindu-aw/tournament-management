package pjatk.s18617.tournamentmanagement.config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("flyway-clean")
@RequiredArgsConstructor
@Component
public class FlywayCleanupOnShutdown implements DisposableBean {

    private final Flyway flyway;

    @Override
    public void destroy() throws Exception {
        flyway.clean();
    }


}
