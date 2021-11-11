package ru.tikskit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class Channels {

    @Bean
    public MessageChannel publishSubscribe() {
        return MessageChannels.publishSubscribe("pubsub1").get();
    }

    @Bean
    public IntegrationFlow channelFlow() {
        return IntegrationFlows.from("productServiceInChannel")
                .channel("pubsub1")
                .get();
    }
}
