package app.training.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.training.dto.trainingprogram.CreateTrainingProgramRequest;
import app.training.dto.trainingprogram.TrainingProgramDto;
import app.training.dto.trainingprogram.UpdateTrainingProgramRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled
class TrainingProgramControllerTest {
    protected static MockMvc mockMvc;
    private static final String FIRST_PROGRAM = "Program1";
    private static final String SECOND_PROGRAM = "Program2";
    private static final String FIRST_ABOUT = "WOD";
    private static final String SECOND_ABOUT = "WOD2";
    private static final LocalDate DATE = LocalDate.parse("2002-11-05");
    private static final Long DEFAULT_ID = 1L;
    private static final Long SECOND_ID = 2L;

    @Autowired
    private ObjectMapper objectMapper;

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
                            "database/trainingProgram/add-default-training-programs.sql"
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
                            "database/trainingProgram/delete-from-training-programs.sql"
                    )
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/trainingProgram/delete-test-training-program.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new training program")
    void createTrainingProgram_ValidRequestDto_Success() throws Exception {
        CreateTrainingProgramRequest request = createTrainingProgramRequest(
                FIRST_PROGRAM, FIRST_ABOUT, DATE);

        TrainingProgramDto expected = createExpectedTrainingProgramDto(
                DEFAULT_ID, FIRST_PROGRAM, FIRST_ABOUT, DATE);

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/programs")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TrainingProgramDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), TrainingProgramDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/trainingProgram/add-default-training-programs.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/trainingProgram/delete-from-training-programs.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get list training programs")
    void getAll_GivenTrainingProgramsInCatalog() throws Exception {
        TrainingProgramDto firstProgram = createExpectedTrainingProgramDto(
                DEFAULT_ID, FIRST_PROGRAM, FIRST_ABOUT, DATE);
        TrainingProgramDto secondProgram = createExpectedTrainingProgramDto(
                SECOND_ID, SECOND_PROGRAM, SECOND_ABOUT, DATE);
        List<TrainingProgramDto> expected = new ArrayList<>();
        expected.add(firstProgram);
        expected.add(secondProgram);

        MvcResult result = mockMvc.perform(get("/programs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TrainingProgramDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), TrainingProgramDto[].class);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get training program by id")
    @Sql(
            scripts = "classpath:database/trainingProgram/delete-from-training-programs.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getTrainingProgramById() throws Exception {
        TrainingProgramDto expected = createExpectedTrainingProgramDto(
                SECOND_ID, FIRST_PROGRAM, FIRST_ABOUT, DATE);
        MvcResult result = mockMvc.perform(get("/programs/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TrainingProgramDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), TrainingProgramDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Soft-Delete training program by id")
    void testDeleteById() throws Exception {
        MvcResult result = mockMvc.perform(delete("/programs/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/trainingProgram/add-default-training-programs.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/trainingProgram/delete-from-training-programs.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update training program by id")
    void updateTrainingProgramById() throws Exception {
        UpdateTrainingProgramRequest request = createUpdateTrainingProgramRequest(
                SECOND_PROGRAM, SECOND_ABOUT);
        TrainingProgramDto updatedTrainingProgram = createExpectedTrainingProgramDto(
                DEFAULT_ID, SECOND_PROGRAM, SECOND_ABOUT, DATE);

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/programs/{id}", DEFAULT_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TrainingProgramDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), TrainingProgramDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(updatedTrainingProgram, actual, "id");
    }

    private TrainingProgramDto createExpectedTrainingProgramDto(
            Long id, String name, String about, LocalDate date) {
        return new TrainingProgramDto()
                .setId(id)
                .setName(name)
                .setAbout(about)
                .setDate(date)
                .setSections(new ArrayList<>());
    }

    private CreateTrainingProgramRequest createTrainingProgramRequest(
            String name, String about, LocalDate date) {
        return new CreateTrainingProgramRequest()
                .setName(name)
                .setAbout(about)
                .setDate(date);
    }

    private UpdateTrainingProgramRequest createUpdateTrainingProgramRequest(
            String name, String about) {
        return new UpdateTrainingProgramRequest()
                .setName(name)
                .setAbout(about);
    }
}
