package demo;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.CircuitBreaker;
import org.reactivestreams.Publisher;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Client("users-api")
@Header(name = "X-API-Key", value = "${users.api.key}")
@CircuitBreaker(reset = "10s")
public interface UsersApiClient {

    @Get("/users")
    HttpResponse<List<User>> list();

    @Get("/users")
    Publisher<User> listAsync();

    @Post("/users")
    User create(@Body @Valid CreateUserRequest request);

    @Introspected
    record User(UUID uuid, String name, String email, String createdBy){}

    @Introspected
    record CreateUserRequest(
            @NotEmpty String name,
            @NotEmpty @Email String email
    ){}
}
