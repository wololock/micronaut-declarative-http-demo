package demo;

import com.agorapulse.worker.annotation.Consumes;
import com.agorapulse.worker.annotation.FixedDelay;
import com.agorapulse.worker.annotation.InitialDelay;
import demo.UsersApiClient.CreateUserRequest;
import jakarta.inject.Singleton;

import java.util.function.Consumer;

@Singleton
final class SqsWorkerJob implements Consumer<CreateUserRequest> {

    private final UsersApiClient usersApiClient;

    SqsWorkerJob(UsersApiClient usersApiClient) {
        this.usersApiClient = usersApiClient;
    }

    @Override
    @Consumes("create-user-request")
    @InitialDelay("10s")
    @FixedDelay("10s")
    public void accept(CreateUserRequest request) {
        System.out.println("Resending " + request);
        usersApiClient.create(request);
    }
}
