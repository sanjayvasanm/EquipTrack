package com.equiptrack.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration
 * Enables MongoDB auditing for @CreatedDate and @LastModifiedDate annotations
 */
@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.equiptrack.repository")
public class MongoConfig {
}
