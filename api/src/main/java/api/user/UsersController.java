package api.user;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;
import java.util.UUID;

@Controller("/users")
@Secured("isAuthenticated()")
@ExecuteOn(TaskExecutors.IO)
class UsersController {

    private final UserRepository userRepository;

    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Get
    Iterable<User> users() {
        return userRepository.findAll();
    }

    @Get("/{uuid}")
    Optional<User> user(UUID uuid) {
        return userRepository.findById(uuid);
    }

    @Delete("/{uuid}")
    HttpResponse<?> delete(UUID uuid) {
        if (!userRepository.existsById(uuid)) {
            throw new IllegalArgumentException("User does not exist");
        }

        userRepository.deleteById(uuid);

        return HttpResponse.noContent();
    }

    @Post
    Optional<User> create(@Valid @Body CreateUserRequest request, Authentication authentication) {
        if (userRepository.existsByEmail(request.email)) {
            throw new IllegalArgumentException("User with email " + request.email + " already exists");
        }

        userRepository.save(User.create(request.name, request.email, authentication.getName()));

        return userRepository.findByEmail(request.email);
    }

    @Error(IllegalArgumentException.class)
    HttpResponse<?> errorHandler(Exception e) {
        return HttpResponse.badRequest(new JsonError(e.getMessage()));
    }

    @Introspected
    record CreateUserRequest(
        @NotEmpty String name,
        @NotEmpty @Email String email
    ){}
}
