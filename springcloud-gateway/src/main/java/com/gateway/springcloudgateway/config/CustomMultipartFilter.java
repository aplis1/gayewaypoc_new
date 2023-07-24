package com.gateway.springcloudgateway.config;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

//@Component
public class CustomMultipartFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest originalRequest = exchange.getRequest();

        if (isMultipartRequest(originalRequest)) {
            // Create a custom ServerHttpRequest to handle the multipart data
            ServerHttpRequest modifiedRequest = new CustomServerHttpRequest(originalRequest);

            // Continue the request processing with the modified request
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        } else {
            // Not a multipart request, continue processing without modification
            return chain.filter(exchange);
        }
    }

    private boolean isMultipartRequest(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        return headers.getContentType() != null
                && headers.getContentType().isCompatibleWith(MediaType.MULTIPART_FORM_DATA);
    }

    // Custom implementation of ServerHttpRequestDecorator
    private static class CustomServerHttpRequest extends ServerHttpRequestDecorator {
        private final ServerHttpRequest delegate;

        public CustomServerHttpRequest(ServerHttpRequest delegate) {
            super(delegate);
            this.delegate = delegate;
        }

        @Override
        public Flux<DataBuffer> getBody() {
            HttpHeaders headers = getHeaders();
            MediaType contentType = headers.getContentType();
            if (contentType != null && MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType)) {
                String boundary = extractBoundary(contentType);
                if (boundary != null) {
                    // Apply the transformation to text files in the multipart content
                    Flux<DataBuffer> originalBody = delegate.getBody();
                    return transformTextFiles(originalBody, boundary);
                }
            }
            return delegate.getBody();
        }

        private String extractBoundary(MediaType contentType) {
            String boundary = contentType.getParameter("boundary");
            return boundary != null ? "--" + boundary : null;
        }

        private Flux<DataBuffer> transformTextFiles(Flux<DataBuffer> body, String boundary) {
            // Check if this is a text file based on its content-type
            return body.flatMap(dataBuffer -> {
                String content = DataBufferUtils.join((Publisher<? extends DataBuffer>) dataBuffer).toString();
                DataBufferUtils.release(dataBuffer);

                // Check if this part contains a text file based on the filename in the Content-Disposition header
                String filename = extractFilename(dataBuffer, boundary);
                if (filename != null && filename.endsWith(".txt")) {
                    // Modify the content of the text file (convert to uppercase in this example)
                    String modifiedContent = content.toUpperCase();
                    byte[] modifiedBytes = modifiedContent.getBytes(StandardCharsets.UTF_8);
                    DataBuffer modifiedDataBuffer = (DataBuffer) new DefaultDataBufferFactory();
                    return Flux.just(modifiedDataBuffer);
                }

                return Flux.just(dataBuffer);
            });
        }

        private String extractFilename(DataBuffer dataBuffer, String boundary) {
            String data = dataBuffer.toString(StandardCharsets.UTF_8);
            int filenameStart = data.indexOf("filename=\"") + "filename=\"".length();
            int filenameEnd = data.indexOf("\"", filenameStart);
            return data.substring(filenameStart, filenameEnd);
        }
    }
}
