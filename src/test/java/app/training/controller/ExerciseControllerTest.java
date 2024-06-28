package app.training.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.training.dto.exercise.CreateExerciseRequest;
import app.training.dto.exercise.ExerciseDto;
import app.training.dto.exercise.UpdateExerciseRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
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
class ExerciseControllerTest {
    protected static MockMvc mockMvc;
    private static final String FIRST_EXERCISE = "Exercise1";
    private static final String SECOND_EXERCISE = "Exercise2";
    private static final String FIRST_ABOUT = "About1";
    private static final String SECOND_ABOUT = "About2";
    private static final Long TRAINING_SECTION_ID = 1L;
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
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/trainingSection/add-default-training-sections.sql"
                    )
            );
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/exercise/add-default-exercises.sql"
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
                            "database/trainingSection/delete-from-training-sections.sql"
                    )
            );
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/exercise/delete-from-exercises.sql"
                    )
            );
        }
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
            scripts = "classpath:database/exercise/delete-test-exercise.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/exercise/delete-from-exercises.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new exercise")
    void createExercise_ValidRequestDto_Success() throws Exception {
        CreateExerciseRequest request = createExerciseRequest(
                FIRST_EXERCISE, FIRST_ABOUT, TRAINING_SECTION_ID);

        ExerciseDto expected = createExpectedExerciseDto(
                DEFAULT_ID, FIRST_EXERCISE, FIRST_ABOUT);

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/exercises")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ExerciseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ExerciseDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/exercise/delete-from-exercises.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/exercise/add-default-exercises.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Get list exercises")
    void getAll_GivenExercisesInCatalog() throws Exception {
        ExerciseDto firstExercise = createExpectedExerciseDto(
                DEFAULT_ID, FIRST_EXERCISE, FIRST_ABOUT);
        ExerciseDto secondExercise = createExpectedExerciseDto(
                SECOND_ID, SECOND_EXERCISE, SECOND_ABOUT);
        List<ExerciseDto> expected = new ArrayList<>();
        expected.add(firstExercise);
        expected.add(secondExercise);

        MvcResult result = mockMvc.perform(get("/exercises")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ExerciseDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), ExerciseDto[].class);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get exercise by id")
    @Sql(
            scripts = "classpath:database/exercise/add-default-exercises.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/exercise/delete-from-exercises.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getExerciseById() throws Exception {
        ExerciseDto expected = createExpectedExerciseDto(
                SECOND_ID, FIRST_EXERCISE, FIRST_ABOUT);
        MvcResult result = mockMvc.perform(get("/exercises/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ExerciseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ExerciseDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Soft-Delete exercise by id")
    void testDeleteById() throws Exception {
        MvcResult result = mockMvc.perform(delete("/exercises/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/exercise/add-default-exercises.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/exercise/delete-from-exercises.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update exercise by id")
    void updateExerciseById() throws Exception {
        UpdateExerciseRequest request = createUpdateExerciseRequest(
                SECOND_EXERCISE, SECOND_ABOUT);
        ExerciseDto updatedExercise = createExpectedExerciseDto(
                DEFAULT_ID, SECOND_EXERCISE, SECOND_ABOUT);

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/exercises/{id}", DEFAULT_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ExerciseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ExerciseDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(updatedExercise, actual, "id");
    }

    private ExerciseDto createExpectedExerciseDto(
            Long id, String name, String about) {
        return new ExerciseDto()
                .setId(id)
                .setName(name)
                .setAbout(about)
                .setVideos(new ArrayList<>());
    }

    private CreateExerciseRequest createExerciseRequest(
            String name, String about, Long trainingSectionId) {
        return new CreateExerciseRequest()
                .setName(name)
                .setAbout(about)
                .setTrainingSectionId(trainingSectionId);
    }

    private UpdateExerciseRequest createUpdateExerciseRequest(
            String name, String about) {
        return new UpdateExerciseRequest()
                .setName(name)
                .setAbout(about);
    }
}

