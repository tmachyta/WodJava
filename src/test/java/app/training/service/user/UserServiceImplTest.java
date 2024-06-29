package app.training.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import app.training.dto.user.UserRegistrationRequest;
import app.training.dto.user.UserResponseDto;
import app.training.dto.user.UserResponseRoleDto;
import app.training.dto.user.UserUpdateImageRequest;
import app.training.dto.user.UserUpdateLastNameRequest;
import app.training.dto.user.UserUpdateNameRequest;
import app.training.dto.user.UserUpdatePasswordRequest;
import app.training.exception.EntityNotFoundException;
import app.training.exception.RegistrationException;
import app.training.mapper.UserMapper;
import app.training.model.Role;
import app.training.model.Role.RoleName;
import app.training.model.User;
import app.training.repository.RoleRepository;
import app.training.repository.UserRepository;
import app.training.service.email.EmailSenderService;
import app.training.service.role.RoleService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final String EMAIL = "test@gmail.com";
    private static final String NON_EXISTED_EMAIL = "test1@gmail.com";
    private static final String UPDATED_EMAIL = "test2@gmail.com";
    private static final String UPDATED_PASSWORD = "123456789";
    private static final String UPDATED_NAME = "Test";
    private static final String OLD_NAME = "Test1";
    private static final String UPDATED_LAST_NAME = "Test2";
    private static final String OLD_LAST_NAME = "Test3";
    private static final String PASSWORD = "12345678";
    private static final String REPEAT_PASSWORD = "12345678";
    private static final String STATUS = "Not_Verified";
    private static final String VERIFICATION_CODE = "7FdAjKL";
    private static final RoleName ROLE = RoleName.USER;
    private static final Long USER_ID = 1L;
    private static final Long NON_EXISTED_USER_ID = 50L;
    private static final byte[] UPDATED_IMAGE = {0x01, 0x02, 0x03, 0x04};
    private static final byte[] OLD_IMAGE = {0x05, 0x06, 0x07, 0x08};

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testRegistrationUserSuccessfully() throws RegistrationException {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail(EMAIL);
        request.setPassword(PASSWORD);
        request.setRepeatPassword(REPEAT_PASSWORD);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        User userToSave = new User();

        when(userMapper.toModel(request)).thenReturn(userToSave);

        userToSave.setPassword(passwordEncoder.encode(request.getPassword()));
        userToSave.setStatus(STATUS);
        userToSave.setVerificationCode(VERIFICATION_CODE);
        Role userRole = new Role();
        userRole.setRoleName(ROLE);
        when(roleService.getRoleByRoleName(ROLE)).thenReturn(userRole);
        userToSave.setRoles(new HashSet<>(Set.of(userRole)));

        when(userRepository.save(userToSave)).thenReturn(userToSave);

        UserResponseDto userResponseDto = new UserResponseDto();

        when(userMapper.toDto(userToSave)).thenReturn(userResponseDto);

        UserResponseDto result = userService.register(request);

        assertNotNull(result);
        /*verify(emailSenderService).sendEmail(
                eq(userToSave.getEmail()),
                eq("WODWarrior"),
                anyString()
        );*/

        assertNotNull(result);
        assertEquals(userResponseDto, result);
    }

    @Test
    public void testGetAllUsers() {
        User user = new User();
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(new User());
        List<UserResponseDto> expectedResult = List.of(new UserResponseDto());
        Page<User> page = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findAll(pageable)).thenReturn(page);

        when(userMapper.toDto(user)).thenReturn(new UserResponseDto());

        List<UserResponseDto> result = userService.getAll(pageable);

        Assertions.assertEquals(expectedResult.size(), result.size());
    }

    @Test
    public void testFindUserById() {
        User user = new User();
        user.setId(USER_ID);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(USER_ID);

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.findById(USER_ID);

        Assertions.assertEquals(user.getId(), result.getId());
    }

    @Test
    public void testFindUserNonExistedId() {
        when(userRepository.findById(NON_EXISTED_USER_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.findById(NON_EXISTED_USER_ID));
    }

    @Test
    public void testDeleteUserById() {
        userService.deleteById(USER_ID);

        when(userRepository.findById(USER_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.findById(USER_ID));
    }

    @Test
    public void testUpdateUserRoleByEmailSuccessfully() {
        Role role = new Role();
        role.setRoleName(ROLE);

        when(roleRepository.findRoleByRoleName(role.getRoleName()))
                .thenReturn(Optional.of(role));

        User user = new User();
        user.setEmail(EMAIL);
        user.setRoles(new HashSet<>(Set.of(role)));

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        Set<Role> roles = user.getRoles();
        roles.add(role);

        UserResponseRoleDto userResponseRoleDto = new UserResponseRoleDto();
        userResponseRoleDto.setRoles(roles);

        when(userRepository.save(user)).thenReturn(user);

        when(userMapper.toUserRoleResponse(user)).thenReturn(userResponseRoleDto);

        UserResponseRoleDto updateUser = userService.updateRoleByEmail(user.getEmail(), role);

        Assertions.assertEquals(user.getRoles(), updateUser.getRoles());
    }

    @Test
    public void testUpdateUserRoleByIdSuccessfully() {
        Role role = new Role();
        role.setRoleName(ROLE);

        when(roleRepository.findRoleByRoleName(role.getRoleName()))
                .thenReturn(Optional.of(role));

        User user = new User();
        user.setId(USER_ID);
        user.setRoles(new HashSet<>(Set.of(role)));

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        Set<Role> roles = user.getRoles();
        roles.add(role);

        UserResponseRoleDto userResponseRoleDto = new UserResponseRoleDto();
        userResponseRoleDto.setRoles(roles);

        when(userRepository.save(user)).thenReturn(user);

        when(userMapper.toUserRoleResponse(user)).thenReturn(userResponseRoleDto);

        UserResponseRoleDto updateUser = userService.updateRoleByUserId(user.getId(), role);

        Assertions.assertEquals(user.getRoles(), updateUser.getRoles());
    }

    @Test
    public void testUpdateUserSuccessfully() throws RegistrationException {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail(UPDATED_EMAIL);
        request.setLastName(UPDATED_NAME);
        request.setLastName(UPDATED_LAST_NAME);
        request.setPassword(PASSWORD);
        request.setRepeatPassword(REPEAT_PASSWORD);

        UserResponseDto expectedResult = new UserResponseDto();
        expectedResult.setEmail(UPDATED_EMAIL);
        expectedResult.setFirstName(UPDATED_NAME);
        expectedResult.setLastName(UPDATED_LAST_NAME);

        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(OLD_NAME);
        user.setLastName(OLD_LAST_NAME);

        when(userRepository.findById(USER_ID))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);

        when(userMapper.toDto(user)).thenReturn(expectedResult);

        UserResponseDto updatedUser = userService.update(USER_ID, request);

        Assertions.assertEquals(updatedUser.getEmail(), expectedResult.getEmail());

        Assertions.assertEquals(updatedUser.getFirstName(), expectedResult.getFirstName());

        Assertions.assertEquals(updatedUser.getLastName(), expectedResult.getLastName());
    }

    @Test
    public void testFindUserByEmail() {
        User user = new User();
        user.setEmail(EMAIL);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(EMAIL);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.findUserByEmail(EMAIL);

        Assertions.assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    public void testFindUserNonExistedEmail() {
        when(userRepository.findByEmail(NON_EXISTED_EMAIL))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.findUserByEmail(NON_EXISTED_EMAIL));
    }

    @Test
    public void testUpdateUserImageSuccessfully() {
        UserUpdateImageRequest request = new UserUpdateImageRequest();
        request.setImageData(UPDATED_IMAGE);

        UserResponseDto expectedResult = new UserResponseDto();
        expectedResult.setImageData(UPDATED_IMAGE);

        User user = new User();
        user.setImageData(OLD_IMAGE);

        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);

        when(userMapper.toDto(user)).thenReturn(expectedResult);

        UserResponseDto result = userService.updateImage(EMAIL, request);

        Assertions.assertEquals(result.getImageData(), expectedResult.getImageData());
    }

    @Test
    public void testUpdateUserNameSuccessfully() {
        UserUpdateNameRequest request = new UserUpdateNameRequest();
        request.setFirstName(UPDATED_NAME);

        UserResponseDto expectedResult = new UserResponseDto();
        expectedResult.setFirstName(UPDATED_NAME);

        User user = new User();
        user.setFirstName(OLD_NAME);

        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);

        when(userMapper.toDto(user)).thenReturn(expectedResult);

        UserResponseDto result = userService.updateName(EMAIL, request);

        Assertions.assertEquals(result.getFirstName(), expectedResult.getFirstName());
    }

    @Test
    public void testUpdateUserLastNameSuccessfully() {
        UserUpdateLastNameRequest request = new UserUpdateLastNameRequest();
        request.setLastName(UPDATED_LAST_NAME);

        UserResponseDto expectedResult = new UserResponseDto();
        expectedResult.setLastName(UPDATED_LAST_NAME);

        User user = new User();
        user.setLastName(OLD_LAST_NAME);

        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);

        when(userMapper.toDto(user)).thenReturn(expectedResult);

        UserResponseDto result = userService.updateLastName(EMAIL, request);

        Assertions.assertEquals(result.getLastName(), expectedResult.getLastName());
    }

    @Test
    public void testDeleteUserByEmail() {
        User user = new User();
        user.setEmail(EMAIL);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user))
                .thenReturn(Optional.empty());

        userService.deleteByEmail(user.getEmail());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.findUserByEmail(EMAIL));
    }

    @Test
    public void testUpdateUserPasswordSuccessfully() {
        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest();
        request.setCurrentPassword(PASSWORD);
        request.setNewPassword(UPDATED_PASSWORD);
        request.setRepeatNewPassword(UPDATED_PASSWORD);

        UserResponseDto expectedResult = new UserResponseDto();
        expectedResult.setPassword(UPDATED_PASSWORD);

        User user = new User();
        user.setPassword(PASSWORD);

        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);

        when(passwordEncoder.matches(PASSWORD, user.getPassword())).thenReturn(true);

        when(userMapper.toDto(user)).thenReturn(expectedResult);

        UserResponseDto result = userService.updatePassword(EMAIL, request);

        Assertions.assertEquals(result.getPassword(), expectedResult.getPassword());
    }
}
