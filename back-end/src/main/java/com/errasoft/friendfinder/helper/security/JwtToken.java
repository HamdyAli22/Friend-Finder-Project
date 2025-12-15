package com.errasoft.friendfinder.helper.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "token")
public class JwtToken {
    private String secret;
    private Duration time;
}
