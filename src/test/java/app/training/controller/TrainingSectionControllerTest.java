package app.training.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.training.dto.trainingsection.CreateTrainingSectionRequest;
import app.training.dto.trainingsection.TrainingSectionDto;
import app.training.dto.trainingsection.UpdateTrainingSectionRequest;
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
class TrainingSectionControllerTest {
    protected static MockMvc mockMvc;
    private static final String FIRST_SECTION = "Section1";
    private static final String SECOND_SECTION = "Section2";
    private static final Long TRAINING_PROGRAM_ID = 1L;
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
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/trainingSection/delete-from-training-sections.sql"
                    )
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/trainingSection/delete-test-training-section.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/trainingSection/delete-from-training-sections.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new training section")
    void createTrainingSection_ValidRequestDto_Success() throws Exception {
        CreateTrainingSectionRequest request = createTrainingSectionRequest(
                FIRST_SECTION, TRAINING_PROGRAM_ID);

        TrainingSectionDto expected = createExpectedTrainingSectionDto(
                DEFAULT_ID, FIRST_SECTION);

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/sections")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TrainingSectionDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), TrainingSectionDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/trainingSection/delete-from-training-sections.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/trainingSection/add-default-training-sections.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Get list training sections")
    void getAll_GivenTrainingSectionsInCatalog() throws Exception {
        TrainingSectionDto firstSection = createExpectedTrainingSectionDto(
                DEFAULT_ID, FIRST_SECTION);
        TrainingSectionDto secondSection = createExpectedTrainingSectionDto(
                SECOND_ID, SECOND_SECTION);
        List<TrainingSectionDto> expected = new ArrayList<>();
        expected.add(firstSection);
        expected.add(secondSection);

        MvcResult result = mockMvc.perform(get("/sections")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TrainingSectionDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), TrainingSectionDto[].class);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get training section by id")
    @Sql(
            scripts = "classpath:database/trainingSection/delete-from-training-sections.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getTrainingSectionById() throws Exception {
        TrainingSectionDto expected = createExpectedTrainingSectionDto(
                SECOND_ID, FIRST_SECTION);
        MvcResult result = mockMvc.perform(get("/sections/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TrainingSectionDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), TrainingSectionDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Soft-Delete training section by id")
    void testDeleteById() throws Exception {
        MvcResult result = mockMvc.perform(delete("/sections/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/trainingSection/add-default-training-sections.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/trainingSection/delete-from-training-sections.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update training section by id")
    void updateTrainingSectionById() throws Exception {
        UpdateTrainingSectionRequest request = createUpdateTrainingSectionRequest(
                SECOND_SECTION);
        TrainingSectionDto updatedTrainingSection = createExpectedTrainingSectionDto(
                DEFAULT_ID, SECOND_SECTION);

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/sections/{id}", DEFAULT_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TrainingSectionDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), TrainingSectionDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(updatedTrainingSection, actual, "id");
    }

    private TrainingSectionDto createExpectedTrainingSectionDto(
            Long id, String name) {
        return new TrainingSectionDto()
                .setId(id)
                .setName(name)
                .setExercises(new ArrayList<>());
    }

    private CreateTrainingSectionRequest createTrainingSectionRequest(
            String name, Long trainingProgramId) {
        return new CreateTrainingSectionRequest()
                .setName(name)
                .setTrainingProgramId(trainingProgramId);
    }

    private UpdateTrainingSectionRequest createUpdateTrainingSectionRequest(
            String name) {
        return new UpdateTrainingSectionRequest()
                .setName(name);
    }
}
