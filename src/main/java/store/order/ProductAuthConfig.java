package store.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import feign.auth.BasicAuthRequestInterceptor;

public class ProductAuthConfig {

    @Bean
    public BasicAuthRequestInterceptor productAuth(
        @Value("${product.security.username}") String username,
        @Value("${product.security.password}") String password
    ) {
        return new BasicAuthRequestInterceptor(username, password);
    }

}
