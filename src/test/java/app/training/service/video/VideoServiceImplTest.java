package app.training.service.video;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import app.training.dto.video.CreateVideoRequest;
import app.training.dto.video.VideoDto;
import app.training.exception.EntityNotFoundException;
import app.training.mapper.VideoMapper;
import app.training.model.Exercise;
import app.training.model.Video;
import app.training.repository.ExerciseRepository;
import app.training.repository.VideoRepository;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
class VideoServiceImplTest {
    private static final Long VIDEO_ID = 1L;
    private static final Long EXERCISE_ID = 1L;
    private static final Long NON_EXISTED_VIDEO_ID = 50L;

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private VideoMapper videoMapper;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private VideoServiceImpl videoService;

    @Test
    public void testSuccessfullySavedVideo() {
        CreateVideoRequest request = new CreateVideoRequest();
        request.setExerciseId(EXERCISE_ID);
        Exercise exercise = new Exercise();
        Video videoToSave = new Video();
        exercise.setId(EXERCISE_ID);

        when(exerciseRepository.findById(exercise.getId()))
                .thenReturn(Optional.of(exercise));

        when(videoMapper.toModel(request)).thenReturn(videoToSave);

        videoToSave.setExercise(exercise);

        when(videoRepository.save(videoToSave)).thenReturn(videoToSave);

        VideoDto videoDto = new VideoDto();

        when(videoMapper.toDto(videoToSave)).thenReturn(videoDto);

        VideoDto result = videoService.create(request);
        assertNotNull(result);
        assertEquals(videoDto, result);
    }

    @Test
    public void testGetAllVideos() {
        Video video = new Video();
        Pageable pageable = PageRequest.of(0, 10);
        List<Video> videos = List.of(new Video());
        List<VideoDto> expectedResult = List.of(new VideoDto());

        Page<Video> page = new PageImpl<>(videos, pageable, videos.size());

        when(videoRepository.findAll(pageable)).thenReturn(page);

        when(videoMapper.toDto(video)).thenReturn(new VideoDto());

        List<VideoDto> result = videoService.getAll(pageable);

        Assertions.assertEquals(expectedResult.size(), result.size());

    }

    @Test
    public void testFindVideoById() {
        Video video = new Video();
        video.setId(VIDEO_ID);
        VideoDto videoDto = new VideoDto();
        videoDto.setId(VIDEO_ID);

        when(videoRepository.findById(video.getId()))
                .thenReturn(Optional.of(video));

        when(videoMapper.toDto(video)).thenReturn(videoDto);

        VideoDto result = videoService.findById(VIDEO_ID);

        Assertions.assertEquals(video.getId(), result.getId());
    }

    @Test
    public void testFindVideoNonExistedId() {
        when(videoRepository.findById(NON_EXISTED_VIDEO_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> videoService.findById(NON_EXISTED_VIDEO_ID));
    }

    @Test
    public void testDeleteVideoById() {
        videoService.deleteById(VIDEO_ID);

        when(videoRepository.findById(VIDEO_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> videoService.findById(VIDEO_ID));
    }
}
