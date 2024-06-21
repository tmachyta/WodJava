package app.training.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.training.dto.video.CreateVideoRequest;
import app.training.dto.video.VideoDto;
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
//import org.junit.jupiter.api.Disabled;
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
class VideoControllerTest {
    protected static MockMvc mockMvc;
    private static final String FIRST_VIDEO = "Video1";
    private static final String SECOND_VIDEO = "Video2";
    private static final String PATH = "path.mp4";
    private static final Long EXERCISE_ID = 1L;
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
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/video/add-default-videos.sql"
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
                            "database/exercise/delete-from-exercises.sql"
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
                            "database/video/delete-from-videos.sql"
                    )
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/video/delete-test-video.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/video/delete-from-videos.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new video")
    void createVideo_ValidRequestDto_Success() throws Exception {
        CreateVideoRequest request = createVideoRequest(
                FIRST_VIDEO, PATH, EXERCISE_ID);

        VideoDto expected = createExpectedVideoDto(
                DEFAULT_ID, FIRST_VIDEO, PATH);

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/videos")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        VideoDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), VideoDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/video/delete-from-videos.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    /*@Sql(
            scripts = "classpath:database/video/add-default-videos.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )*/
    @DisplayName("Get list videos")
    void getAll_GivenVideosInCatalog() throws Exception {
        VideoDto firstVideo = createExpectedVideoDto(
                DEFAULT_ID, FIRST_VIDEO, PATH);
        VideoDto secondVideo = createExpectedVideoDto(
                SECOND_ID, SECOND_VIDEO, PATH);
        List<VideoDto> expected = new ArrayList<>();
        expected.add(firstVideo);
        expected.add(secondVideo);

        MvcResult result = mockMvc.perform(get("/videos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        VideoDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), VideoDto[].class);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get video by id")
    @Sql(
            scripts = "classpath:database/video/add-default-videos.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/video/delete-from-videos.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getVideoById() throws Exception {
        VideoDto expected = createExpectedVideoDto(
                SECOND_ID, FIRST_VIDEO, PATH);
        MvcResult result = mockMvc.perform(get("/videos/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        VideoDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), VideoDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Soft-Delete video by id")
    void testDeleteById() throws Exception {
        MvcResult result = mockMvc.perform(delete("/videos/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    private VideoDto createExpectedVideoDto(
            Long id, String name, String relativePath) {
        return new VideoDto()
                .setId(id)
                .setName(name)
                .setVideoRelativePath(relativePath);
    }

    private CreateVideoRequest createVideoRequest(
            String name, String relativePath, Long exerciseId) {
        return new CreateVideoRequest()
                .setName(name)
                .setVideoRelativePath(relativePath)
                .setExerciseId(exerciseId);
    }
}
