package ru.tikskit.config;

import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.io.IOException;

@Configuration
public class TargetDataSourceConfig {

    @Bean
    @ConfigurationProperties("app.datasource.tar")
    @Primary
    public MongoProperties targetMongoProperties() {
        return new MongoProperties();
    }

    @Bean
    public MongoDatabaseFactory targetMongoDatabaseFactory(MongoProperties targetMongoProperties) {
        return new SimpleMongoClientDatabaseFactory(MongoClients.create(), targetMongoProperties.getDatabase());
    }

    @Bean
    public MongoTemplate targetMongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }

    @Bean
    public MongodConfig mongodConfig(MongoProperties targetMongoProperties) throws IOException {
       // Настраиваем embedded mongo server так, чтобы он работал на дефолтном порту mongodb
        int port = targetMongoProperties.getPort() == null || targetMongoProperties.getPort() <= 0 ?
                MongoProperties.DEFAULT_PORT : targetMongoProperties.getPort();
        return MongodConfig.builder()
                .net(new Net(port, Network.localhostIsIPv6()))
                .version(Version.Main.PRODUCTION)
                .build();
    }
}
