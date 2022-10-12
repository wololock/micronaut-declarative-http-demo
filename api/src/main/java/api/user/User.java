package api.user;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Introspected
public record User(
        @Id UUID uuid,
        @NotEmpty String name,
        @NotEmpty @Email String email,
        @NotEmpty String createdBy
) {
    static User create(String name, String email, String createdBy) {
        return new User(UUID.randomUUID(), name, email, createdBy);
    }
}
