package client;

import com.agorapulse.worker.JobManager;
import io.micronaut.retry.annotation.Fallback;
import io.micronaut.retry.event.CircuitClosedEvent;
import io.micronaut.runtime.event.annotation.EventListener;

import java.util.Collections;
import java.util.List;

@Fallback
final class FallbackUserHttpClient implements UserHttpClient {

    private final JobManager jobManager;

    public FallbackUserHttpClient(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @Override
    public List<UserDTO> list() {
        return Collections.emptyList();
    }

    @Override
    public UserDTO create(CreateUserRequest request) {
        jobManager.enqueue(FallbackSqsWorker.class, request);

        return new UserDTO(null, request.name(), request.email(), null);
    }

    @EventListener
    void onCircuitClosedEvent(CircuitClosedEvent event) {
        var method = event.getSource().getTargetMethod();

        if ("create".equals(method.getName()) && method.getDeclaringClass().equals(UserHttpClient.class)) {
            jobManager.run(FallbackSqsWorker.class);
        }
    }
}
