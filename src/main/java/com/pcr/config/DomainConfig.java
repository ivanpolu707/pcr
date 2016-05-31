package com.pcr.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.pcr.domain")
@EnableJpaRepositories("com.pcr.repos")
@EnableTransactionManagement
public class DomainConfig {
}
