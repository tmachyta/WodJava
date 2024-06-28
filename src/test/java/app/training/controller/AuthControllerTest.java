package app.training.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.training.dto.user.UserLoginRequestDto;
import app.training.dto.user.UserLoginResponseDto;
import app.training.dto.user.UserRegistrationRequest;
import app.training.dto.user.UserResponseDto;
import app.training.service.password.PasswordResetService;
import app.training.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled
class AuthControllerTest {
    protected static MockMvc mockMvc;
    private static final String FIRST_EMAIL = "test@gmail.com";
    private static final String SECOND_EMAIL = "test2@gmail.com";
    private static final String THIRD_EMAIL = "test3@gmail.com";
    private static final String FIRST_NAME = "Test";
    private static final String SECOND_NAME = "Test2";
    private static final String FIRST_LAST_NAME = "Test";
    private static final String SECOND_LAST_NAME = "Test2";
    private static final String PASSWORD = "12345678";
    private static final String REPEAT_PASSWORD = "12345678";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.parse("2002-11-05");
    private static final String STATUS = "Not Verified";
    private static final String TOKEN = "hfhsfdsfkksakdsakdsafgsd";
    private static final String ROLE = "USER";
    private static final Long DEFAULT_ID = 1L;
    private static final Long SECOND_ID = 2L;
    private static final Long THIRD_ID = 3L;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private PasswordResetService passwordResetService;

    @Mock
    private UserService userService;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/role/add-default-roles.sql"
                    )
            );
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/user/add-default-users.sql"
                    )
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/role/delete-from-users-roles.sql"
                    )
            );
        }

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/role/delete-from-roles.sql"
                    )
            );
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/user/delete-from-users.sql"
                    )
            );
        }
    }

    @Test
    @Sql(
            scripts = "classpath:database/user/delete-from-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Login a new user")
    //@Disabled
    void loginUser_ValidRequestDto_Success() throws Exception {
        UserLoginRequestDto request = createUserLoginRequest(
                SECOND_EMAIL,
                PASSWORD
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserLoginResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), UserLoginResponseDto.class);

        assertNotNull(actual);
    }

    @Test
    @Sql(
            scripts = "classpath:database/role/delete-from-users-roles.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/user/delete-test-user.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Register a new user")
    void registerUser_ValidRequestDto_Success() throws Exception {
        UserRegistrationRequest request = createUserRegistrationRequest(
                THIRD_EMAIL,
                FIRST_NAME,
                FIRST_LAST_NAME,
                PASSWORD,
                REPEAT_PASSWORD,
                DATE_OF_BIRTH
        );

        UserResponseDto expected = createExpectedUserResponseDto(
                DEFAULT_ID,
                THIRD_EMAIL,
                FIRST_NAME,
                FIRST_LAST_NAME,
                DATE_OF_BIRTH,
                STATUS
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/auth/register")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), UserResponseDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @Sql(
            scripts = "classpath:database/user/add-default-users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/user/delete-from-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Check email availability")
    void checkEmail_ValidEmail_Success() throws Exception {
        when(userService.findUserByEmail(FIRST_EMAIL)).thenReturn(null);

        mockMvc.perform(get("/auth/check/{email}", FIRST_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Reset password by email")
    void resetPassword_ValidEmail_Success() throws Exception {
        doNothing().when(passwordResetService).resetPassword(FIRST_EMAIL);

        mockMvc.perform(put("/auth/reset/{email}", FIRST_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private UserLoginResponseDto createExpectedUserLoginResponseDto(
            String token,
            String roleName
    ) {
        return new UserLoginResponseDto(token, roleName);
    }

    private UserLoginRequestDto createUserLoginRequest(
            String email, String password
    ) {
        return new UserLoginRequestDto(email, password);
    }

    private UserResponseDto createExpectedUserResponseDto(
            Long id, String email, String firstName, String lastName,
            LocalDate dateOfBirth, String status
    ) {
        return new UserResponseDto()
                .setId(id)
                .setEmail(email)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setDateOfBirth(dateOfBirth)
                .setStatus(status);
    }

    private UserRegistrationRequest createUserRegistrationRequest(
            String email, String firstName, String lastName,
            String password, String repeatPassword,
            LocalDate dateOfBirth
    ) {
        return new UserRegistrationRequest()
                .setEmail(email)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPassword(password)
                .setRepeatPassword(repeatPassword)
                .setDateOfBirth(dateOfBirth);
    }
}
