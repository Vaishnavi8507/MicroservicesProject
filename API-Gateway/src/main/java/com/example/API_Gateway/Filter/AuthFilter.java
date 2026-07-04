package com.example.API_Gateway.Filter;

import com.example.API_Gateway.Exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Validator validator;

    public AuthFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

            if(validator.predicate.test(request)) {
                // ✅ Use getFirst() instead of containsHeader()
                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                if(authHeader == null || authHeader.isEmpty()) {
                    throw new BadRequestException("Authorization header is missing", HttpStatus.UNAUTHORIZED);
                }

                String token = null;
                if(authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                }

                if(token == null || token.isEmpty()) {
                    throw new BadRequestException("Invalid token format", HttpStatus.UNAUTHORIZED);
                }

                try {
                    jwtUtil.validateToke(token);
                } catch (Exception e) {
                    throw new BadRequestException("Invalid token", HttpStatus.UNAUTHORIZED);
                }
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}