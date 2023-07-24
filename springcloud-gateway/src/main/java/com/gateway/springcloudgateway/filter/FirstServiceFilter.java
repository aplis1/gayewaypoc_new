package com.gateway.springcloudgateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.springcloudgateway.util.GatewayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FirstServiceFilter extends AbstractGatewayFilterFactory<FirstServiceFilter.Config> {


    private static final String CACHED_REQUEST_BODY_KEY = "cachedRequestBodyObject";;
    final Logger logger = LoggerFactory.getLogger(FirstServiceFilter.class);
    private static final String CACHE_REQUEST_BODY_OBJECT_KEY = "cachedRequestBodyObject";

    @Autowired
    GatewayUtil gatewayUtil;

    @Autowired
    ObjectMapper objectMapper;

    String authenticatedUser = null;

    public FirstServiceFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            return authenticateRequest(exchange, chain);
        });
    }

    private Mono<Void> authenticateRequest(ServerWebExchange exchange, GatewayFilterChain chain) {

        logger.info("Atleast the path {} ", exchange.getRequest().getURI());


        //if (body != null) {
           /* ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(
                    exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return exchange.getRequest().getBody();
                }
            };
            exchange.getAttributes().remove(CACHED_REQUEST_BODY_KEY);
            return chain.filter(exchange.mutate().request(decorator).build());*/
       // }

        //return chain.filter(exchange);

        return gatewayUtil.authenticatedUser(exchange.getRequest().getHeaders()).map(response -> {
            if(null != response){
                authenticatedUser = response.getBody().getUsername();
                logger.error("authenticatedRequest:authenticatedUser {}", authenticatedUser);
            }else {
                logger.error("authenticatedRequest:error");
            }
            ServerHttpRequest mutatedRequest = exchange.mutate().build().getRequest();
            mutatedRequest.mutate().header("aegis-user", authenticatedUser);
            if(exchange.getRequest().getHeaders().getContentType().isCompatibleWith(MediaType.MULTIPART_FORM_DATA)){

            }
            return exchange.mutate().request(mutatedRequest).build();
        }).flatMap(chain::filter);

    }

    public static class Config {

    }
}
