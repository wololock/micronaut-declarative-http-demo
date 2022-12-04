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
final class FallbackHttpClient implements UsersApiHttpClient {

    private final JobManager jobManager;

    FallbackHttpClient(JobManager jobManager) {
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
        jobManager.enqueue(FallbackUsersApiSqsJob.class, request);
        return new User(null, request.name(), request.email(), "fallback");
    }

    @EventListener
    void onCircuitClosedEvent(CircuitClosedEvent event) throws NoSuchMethodException {
        System.out.println("Received " + event);

        var method =event.getSource().getTargetMethod();


        if ("create".equals(method.getName()) && UsersApiHttpClient.class.equals(method.getDeclaringClass())){
            System.out.println("Triggering SQS job");
            jobManager.run(FallbackUsersApiSqsJob.class);

        }









    }







}
