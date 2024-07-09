package app.training.service.user;

import app.training.dto.user.UserRegistrationRequest;
import app.training.dto.user.UserResponseDto;
import app.training.dto.user.UserResponseRoleDto;
import app.training.dto.user.UserUpdateBirthdayRequest;
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
import app.training.utils.VerificationCodeUtil;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String STATUS = "Not_Verified";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final EmailSenderService emailSenderService;

    @Override
    public UserResponseDto register(UserRegistrationRequest request) throws RegistrationException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("Unable to complete registration");
        }

        if (!request.getPassword().equals(request.getRepeatPassword())) {
            throw new RegistrationException("Passwords do not match");
        }

        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(STATUS);
        String code = VerificationCodeUtil.generateCode();
        user.setVerificationCode(code);
        Role userRole = roleService.getRoleByRoleName(RoleName.USER);
        user.setRoles(new HashSet<>(Set.of(userRole)));
        User savedUser = userRepository.save(user);
        String emailBody = """
                Dear %s,
                
                Welcome to the WODWarrior family!
                
                To complete your registration and start your fitness journey with us,
                please verify your account.
                Here's your verification code: %s

                Please enter this code in your profile under 'Account Verification'.

                If you have any questions or need assistance,
                feel free to reach out to our support team.

                Thank you for joining WODWarrior.
                We look forward to helping you achieve your fitness goals!

                Best regards,
                The WODWarrior Team
    
                """.formatted(savedUser.getFirstName(), code).replace("\n", LINE_SEPARATOR);
        emailSenderService.sendEmail(savedUser.getEmail(),
                "WODWarrior - Verify Your Account",
                emailBody);
        return userMapper.toDto(savedUser);
    }

    @Override
    public List<UserResponseDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by id " + id));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseRoleDto updateRoleByEmail(String email, Role roleName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));
        Role role = roleRepository.findRoleByRoleName(roleName.getRoleName())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find role by roleName " + roleName));
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        return userMapper.toUserRoleResponse(savedUser);
    }

    @Override
    public UserResponseRoleDto updateRoleByUserId(Long id, Role roleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by id " + id));
        Role role = roleRepository.findRoleByRoleName(roleName.getRoleName())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find role by roleName " + roleName));
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        return userMapper.toUserRoleResponse(savedUser);
    }

    @Override
    public UserResponseDto update(Long id, UserRegistrationRequest requestDto)
            throws RegistrationException {
        User existedUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by id " + id));
        if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
            throw new RegistrationException("Passwords do not match");
        }
        existedUser.setEmail(requestDto.getEmail());
        existedUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        existedUser.setFirstName(requestDto.getFirstName());
        existedUser.setLastName(requestDto.getLastName());
        User savedUser = userRepository.save(existedUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateImage(String email, UserUpdateImageRequest request) {
        User existedUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));

        existedUser.setImageData(request.getImageData());
        User savedUser = userRepository.save(existedUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDto updateName(String email, UserUpdateNameRequest request) {
        User existedUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));
        existedUser.setFirstName(request.getFirstName());
        User savedUser = userRepository.save(existedUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDto updateLastName(String email, UserUpdateLastNameRequest request) {
        User existedUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));

        existedUser.setLastName(request.getLastName());
        User savedUser = userRepository.save(existedUser);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    @Override
    public void deleteByEmail(String email) {
        User existedUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));
        userRepository.deleteUserByEmail(existedUser.getEmail());
    }

    @Override
    public UserResponseDto updatePassword(String email, UserUpdatePasswordRequest request) {
        User existedUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));

        if (!passwordEncoder.matches(request.getCurrentPassword(), existedUser.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        } else if (!request.getNewPassword().equals(request.getRepeatNewPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        existedUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        User savedUser = userRepository.save(existedUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    public void subscribeUser(String email) {
        User existedUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));
        Role userRole = roleService.getRoleByRoleName(RoleName.SUBSCRIBED);
        LocalDate now = LocalDate.now();
        LocalDate expiration = now.plusDays(30);
        existedUser.setSubscriptionExpiration(expiration);
        existedUser.setRoles(new HashSet<>(Set.of(userRole)));
        userRepository.save(existedUser);
        String body = """
                Dear %s,
                
                Welcome to the WODWarrior family!
                
                We are excited to inform you that your subscription is now active,
                
                Get ready to embark on an incredible fitness journey with us.
                
                Here are some things you can do now:
                
                - Explore our exclusive training programs.
                
                Thank you for choosing WODWarrior.
                
                Let's start our training and achieve your fitness goals together!
                
                Best regards,
                
                The WODWarrior Team
                
                """.formatted(existedUser.getFirstName()).replace("\n", LINE_SEPARATOR);
        emailSenderService.sendEmail(existedUser.getEmail(),
                "WODWarrior - Subscription Activated",
                body);
    }

    @Override
    public void unSubscribeUser(String email) {
        User existedUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));
        LocalDate expirationDate = existedUser.getSubscriptionExpiration();
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);

        if (expirationDate != null && expirationDate.isBefore(thirtyDaysAgo)) {
            Role userRole = roleService.getRoleByRoleName(RoleName.USER);
            existedUser.setRoles(new HashSet<>(Set.of(userRole)));
            userRepository.save(existedUser);
            String body = """
                    Dear %s,
                    
                    We wanted to let you know that your subscription
                    
                    to WODWarrior has expired
                    
                    To continue enjoying our training programs and exclusive content,
                    
                    please renew your subscription as soon as possible
                    
                    Thank you for being a valued member of the WODWarrior family
                    
                    Best regards
                    
                    The WODWarrior Team
                    
                    """.formatted(existedUser.getFirstName()).replace("\n", LINE_SEPARATOR);
            emailSenderService.sendEmail(existedUser.getEmail(),
                    "WODWarrior",
                    body);
        }
    }

    @Override
    public UserResponseDto updateBirthday(String email, UserUpdateBirthdayRequest request) {
        User existedUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));

        existedUser.setDateOfBirth(request.getDateOfBirth());
        User savedUser = userRepository.save(existedUser);
        return userMapper.toDto(savedUser);
    }
}
