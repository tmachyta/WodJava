package app.training.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserUpdateLastNameRequest {
    private String lastName;
}
