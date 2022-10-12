package client;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.CircuitBreaker;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Client("api")
@Header(name = "X-API-Key", value = "${micronaut.users-api.key}")
@CircuitBreaker(maxDelay = "1s")
public interface UserHttpClient {

    @Get("/users")
    List<UserDTO> list();

    @Post("/users")
    UserDTO create(@Valid @Body CreateUserRequest request);

    @Introspected
    record UserDTO(UUID uuid, String name, String email, String createdBy){}

    @Introspected
    record CreateUserRequest(
            @NotEmpty String name,
            @NotEmpty @Email String email
    ){}
}
