package demo;

import com.agorapulse.worker.annotation.Consumes;
import com.agorapulse.worker.annotation.FixedDelay;
import com.agorapulse.worker.annotation.InitialDelay;
import demo.UsersApiHttpClient.CreateUserRequest;
import jakarta.inject.Singleton;

import java.util.function.Consumer;

@Singleton
final class FallbackUsersApiSqsJob implements Consumer<CreateUserRequest> {

    private final UsersApiHttpClient httpClient;

    FallbackUsersApiSqsJob(UsersApiHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    @FixedDelay("30m")
    @InitialDelay("30m")
    @Consumes("create-user-request")
    public void accept(CreateUserRequest request) {
        System.out.println("Consuming " + request);
        httpClient.create(request);
    }
}
