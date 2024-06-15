package app.training.service.role;

import app.training.model.Role;
import app.training.model.Role.RoleName;

public interface RoleService {
    Role getRoleByRoleName(RoleName roleName);
}
