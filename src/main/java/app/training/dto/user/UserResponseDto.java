package app.training.dto.user;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private LocalDate dateOfBirth;
    private byte[] imageData;
    private String status;
}
