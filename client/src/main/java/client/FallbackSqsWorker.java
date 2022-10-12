package client;

import client.UserHttpClient.CreateUserRequest;
import com.agorapulse.worker.annotation.Consumes;
import com.agorapulse.worker.annotation.FixedDelay;
import com.agorapulse.worker.annotation.InitialDelay;
import jakarta.inject.Singleton;

import java.util.function.Consumer;

@Singleton
final class FallbackSqsWorker implements Consumer<CreateUserRequest> {

    private final UserHttpClient userHttpClient;

    FallbackSqsWorker(UserHttpClient userHttpClient) {
        this.userHttpClient = userHttpClient;
    }

    @Override
    @Consumes("create-user-request")
    @FixedDelay("1h")
    @InitialDelay("30m")
    public void accept(CreateUserRequest request) {
        System.out.println("Resending " + request);
        userHttpClient.create(request);
    }

}
