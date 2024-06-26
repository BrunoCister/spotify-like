package com.infnet.br.SpotifyLike.configs;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RetryPolicyConfiguration {

    private static final int MAX_RETRY = 5;

    @Bean
    public static Retry getRetryPolicy() {

        RetryConfig retryConfig = RetryConfig.custom().maxAttempts(MAX_RETRY).waitDuration(Duration.ofSeconds(1)).build();
        return Retry.of("retry-policy", retryConfig);
    }
}
