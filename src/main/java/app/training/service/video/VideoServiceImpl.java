package app.training.service.video;

import app.training.dto.video.CreateVideoRequest;
import app.training.dto.video.VideoDto;
import app.training.exception.EntityNotFoundException;
import app.training.mapper.VideoMapper;
import app.training.model.Exercise;
import app.training.model.Video;
import app.training.repository.ExerciseRepository;
import app.training.repository.VideoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    private final ExerciseRepository exerciseRepository;

    @Override
    public List<VideoDto> getAll(Pageable pageable) {
        return videoRepository.findAll(pageable)
                .stream()
                .map(videoMapper::toDto)
                .toList();
    }

    @Override
    public VideoDto create(CreateVideoRequest request) {
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find exercise by id "
                                + request.getExerciseId()));

        Video video = videoMapper.toModel(request);
        video.setExercise(exercise);
        return videoMapper.toDto(videoRepository.save(video));
    }

    @Override
    public VideoDto findById(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find video by id " + id));
        return videoMapper.toDto(video);
    }

    @Override
    public void deleteById(Long id) {
        videoRepository.deleteById(id);
    }
}
