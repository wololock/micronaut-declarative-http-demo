package api.security;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.ClientAuthentication;
import io.micronaut.security.filters.AuthenticationFetcher;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Singleton
final class ApiKeyAuthenticationFetcher implements AuthenticationFetcher {

    private static final String HEADER_NAME = "X-API-Key";

    private final Map<String, String> keys;

    public ApiKeyAuthenticationFetcher(@Property(name = "micronaut.security.api-keys") Map<String, String> keys) {
        this.keys = Collections.unmodifiableMap(keys);
    }

    @Override
    public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
        return request.getHeaders()
                .get(HEADER_NAME, String.class)
                .flatMap(key -> Optional.ofNullable(keys.get(key)))
                .map(key -> (Authentication) new ClientAuthentication(key, Collections.emptyMap()))
                .map(Publishers::just)
                .orElse(Publishers.empty());

    }
}