package com.gateway.springcloudgateway.util;

import com.first.firstmicroservice.model.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Component
public class GatewayUtil {

    final Logger logger = LoggerFactory.getLogger(GatewayUtil.class);

    @Autowired
    private WebClient.Builder webClient;

    public Mono<ResponseEntity<AuthenticatedUser>> authenticatedUser(HttpHeaders headers) {
        String url = "http://first-microservice/first/authenticate/" + "aletisil";

        LinkedMultiValueMap multiValueMap = new LinkedMultiValueMap<>(headers);
        Consumer<HttpHeaders> httpHeadersConsumer = it -> it.addAll(multiValueMap);

        return this.webClient.build().get().uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .exchangeToMono(clientResponse -> {
                    Mono<AuthenticatedUser> bodyMono = clientResponse.bodyToMono(AuthenticatedUser.class);
                    return bodyMono.map(body -> ResponseEntity.status(clientResponse.statusCode())
                            .headers(clientResponse.headers().asHttpHeaders())
                            .body(body));
                });
    }
}
