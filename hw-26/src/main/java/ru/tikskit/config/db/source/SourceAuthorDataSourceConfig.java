package ru.tikskit.config.db.source;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
//@EnableTransactionManagement
public class SourceAuthorDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.src")
    public DataSourceProperties sourceAuthorDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.src.configuration")
    public DataSource sourceAuthorDataSource() {
        return sourceAuthorDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean sourceAuthorEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(sourceAuthorDataSource())
                .packages(ru.tikskit.domain.src.Author.class)
                .build();
    }
}
