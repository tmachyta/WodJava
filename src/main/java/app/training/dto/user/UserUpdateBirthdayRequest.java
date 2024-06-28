package app.training.dto.user;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserUpdateBirthdayRequest {
    private LocalDate dateOfBirth;
}
