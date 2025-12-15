package com.errasoft.friendfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@EnableCaching
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class FriendFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(FriendFinderApplication.class, args);
	}

}
