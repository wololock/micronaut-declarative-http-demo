package demo;

import com.agorapulse.worker.JobManager;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpResponse;
import io.micronaut.retry.annotation.Fallback;
import io.micronaut.retry.event.CircuitClosedEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import org.reactivestreams.Publisher;

import java.util.Collections;
import java.util.List;

@Fallback
final class FallbackUsersApiClient implements UsersApiClient {

    private final JobManager jobManager;

    FallbackUsersApiClient(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public HttpResponse<List<User>> list() {
        return HttpResponse.ok(Collections.emptyList());
    }

    @Override
    public Publisher<User> listAsync() {
        return Publishers.empty();
    }

    @Override
    public User create(CreateUserRequest request) {
        jobManager.enqueue(SqsWorkerJob.class, request);
        return new User(null, request.name(), request.email(), "__fallback__");
    }

    @EventListener
    void onCircuitClosedEvent(CircuitClosedEvent event) {
        var source = event.getSource();

        if (source.getMethodName().equals("create") && source.getDeclaringType().equals(UsersApiClient.class)) {
            jobManager.run(SqsWorkerJob.class);
        }
    }
}
