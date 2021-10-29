package ru.tikskit.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class SourceDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.src")
    public DataSourceProperties sourceDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource sourceDataSource(DataSourceProperties sourceDataSourceProperties) {
        return sourceDataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean sourceEntityManagerFactory(
            EntityManagerFactoryBuilder builder, DataSource sourceDataSource) {
        return builder
                .dataSource(sourceDataSource)
                .packages(ru.tikskit.domain.src.Author.class, ru.tikskit.domain.src.Genre.class)
                .build();
    }
}
