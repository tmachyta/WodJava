package app.training.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserUpdatePasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String repeatNewPassword;
}
