package app.training.dto.user;

import app.training.model.Role;
import java.util.Set;
import lombok.Data;

@Data
public class UserResponseRoleDto {
    private Set<Role> roles;
}
