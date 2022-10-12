package client;

import client.UserHttpClient.CreateUserRequest;
import client.UserHttpClient.UserDTO;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import javax.validation.Valid;
import java.util.List;

@Controller("/client/users")
class UsersController {

    private final UserHttpClient httpClient;

    UsersController(UserHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Get
    List<UserDTO> list() {
        return httpClient.list();
    }

    @Post
    UserDTO create(@Body @Valid CreateUserRequest request) {
        return httpClient.create(request);
    }
}
