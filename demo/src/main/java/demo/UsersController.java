package demo;

import demo.UsersApiClient.CreateUserRequest;
import demo.UsersApiClient.User;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.reactivestreams.Publisher;

import javax.validation.Valid;
import java.util.List;

@Controller("/client/users")
@ExecuteOn(TaskExecutors.IO)
class UsersController {

    private final UsersApiClient usersApiClient;

    UsersController(UsersApiClient usersApiClient) {
        this.usersApiClient = usersApiClient;
    }

    @Get
    HttpResponse<List<User>> list() {
        var response = usersApiClient.list();

        return HttpResponse.status(response.status())
                .body(response.body())
                .header("X-Target-Host", response.getHeaders().get("Target-Host", String.class, ""));
    }

    @Get("/async")
    Publisher<User> listAsync() {
        return usersApiClient.listAsync();
    }

    @Post
    @Status(HttpStatus.CREATED)
    User create(@Body @Valid CreateUserRequest request) {
        return usersApiClient.create(request);
    }
}
