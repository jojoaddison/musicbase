package org.cloudfoundry.samples.music.config.data;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("postgres-local")
public class PostgresLocalDataSourceConfig extends AbstractLocalDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return createDataSource("jdbc:postgresql://localhost/music",
                "org.postgresql.Driver", "postgres", "postgres");
    }

}
