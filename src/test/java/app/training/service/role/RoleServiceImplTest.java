package app.training.service.role;

import static org.mockito.Mockito.when;

import app.training.model.Role;
import app.training.repository.RoleRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    private static final Role.RoleName ROLE = Role.RoleName.USER;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    public void testFindRoleByRoleName() {
        Role role = new Role();
        role.setRoleName(ROLE);

        when(roleRepository.findRoleByRoleName(role.getRoleName()))
                .thenReturn(Optional.of(role));

        Role result = roleService.getRoleByRoleName(ROLE);

        Assertions.assertEquals(role.getRoleName(), result.getRoleName());
    }
}
