package app.training.dto.user;

import app.training.lib.FieldsValueMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@FieldsValueMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match!"
)

@Data
@Accessors(chain = true)
public class UserRegistrationRequest {
    @NotBlank
    @Size(min = 8, max = 50)
    private String email;
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
    private String repeatPassword;
    @NotBlank
    @Size(min = 2, max = 35)
    private String firstName;
    @Size(min = 2, max = 35)
    private String lastName;
    private LocalDate dateOfBirth;
}
