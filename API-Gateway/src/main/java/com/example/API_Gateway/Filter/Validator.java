package com.example.API_Gateway.Filter;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.function.Predicate;

@Component
public class Validator {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public static final List<String> endpoints = List.of(
            "/register-user",
            "/generate-token",
            "/validate-token/{token}"
    );

    public Predicate<ServerHttpRequest> predicate = serverHttpRequest -> {
        String requestPath = serverHttpRequest.getURI().getPath();
        return endpoints.stream().noneMatch(uri -> antPathMatcher.match(uri, requestPath));
    };
}
