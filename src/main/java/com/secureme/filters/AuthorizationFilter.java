
package com.secureme.filters;

import com.secureme.dto.UserRegistration;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;
import java.util.Optional;

@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {

    public static class Config {
        // ...
    }

    private final WebClient.Builder webClientBuilder;
    public AuthorizationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            String authorizationHeader = Optional.of(exchange.getRequest().getHeaders())
                    .filter(headers -> headers.containsKey(HttpHeaders.AUTHORIZATION))
                    .map(headers -> headers.get(HttpHeaders.AUTHORIZATION))
                    .map(headers -> headers.get(0))
                    .orElseThrow(() -> new RuntimeException("Missing authorization information"));

            if(!authorizationHeader.startsWith("Bearer"))
                throw new RuntimeException("Invalid authorization structure");

            String token = authorizationHeader.substring("Bearer ".length());


         return webClientBuilder.build()
                 .get()
                 .uri("http://SECURE-ME-AUTHENTICATION/user/validateToken?token="+token)
                 .retrieve().bodyToMono(UserRegistration.class)
                 .map(user -> {
                     exchange.getRequest()
                             .mutate()
                             .header("x-auth-user-name", user.getUsername());
                     return exchange;
                 }).flatMap(chain::filter);


        });
    }


}
