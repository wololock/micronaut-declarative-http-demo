package demo;

import demo.UsersApiHttpClient.CreateUserRequest;
import demo.UsersApiHttpClient.User;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.TaskScheduler;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.reactivestreams.Publisher;

import java.util.List;

@Controller("/client/users")
final class UsersController {

    private final UsersApiHttpClient httpClient;

    UsersController(UsersApiHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Get
    MutableHttpResponse<List<User>> list() {
        var response = httpClient.list();

        return HttpResponse.status(response.status())
                .body(response.body())
                .header("X-Target-Host", response.getHeaders().get("Target-Host", String.class, ""));
    }

    @Get("/async")
    Publisher<User> listAsync() {
        return httpClient.listAsync();
    }

    @Post
    User create(@Body CreateUserRequest request) {
        return httpClient.create(request);
    }
}

