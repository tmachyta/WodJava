package app.training.service.exercise;

import app.training.dto.exercise.CreateExerciseRequest;
import app.training.dto.exercise.ExerciseDto;
import app.training.dto.exercise.UpdateExerciseAboutRequest;
import app.training.dto.exercise.UpdateExerciseImageRequest;
import app.training.dto.exercise.UpdateExerciseNameRequest;
import app.training.dto.exercise.UpdateExerciseRequest;
import app.training.dto.exercise.UpdateExerciseSectionRequest;
import app.training.exception.EntityNotFoundException;
import app.training.mapper.ExerciseMapper;
import app.training.model.Exercise;
import app.training.model.TrainingSection;
import app.training.repository.ExerciseRepository;
import app.training.repository.TrainingSectionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    private final TrainingSectionRepository trainingSectionRepository;

    @Override
    public List<ExerciseDto> getAll(Pageable pageable) {
        return exerciseRepository.findAll(pageable)
                .stream()
                .map(exerciseMapper::toDto)
                .toList();
    }

    @Override
    public ExerciseDto create(CreateExerciseRequest request) {
        TrainingSection trainingSection =
                trainingSectionRepository.findById(request.getTrainingSectionId())
                        .orElseThrow(() ->
                                new EntityNotFoundException("Can't find seciton by id "
                                        + request.getTrainingSectionId()));

        Exercise exercise = exerciseMapper.toModel(request);
        exercise.setTrainingSection(trainingSection);
        return exerciseMapper.toDto(exerciseRepository.save(exercise));
    }

    @Override
    public ExerciseDto findById(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find exercise by id " + id));
        return exerciseMapper.toDto(exercise);
    }

    @Override
    public void deleteById(Long id) {
        exerciseRepository.deleteById(id);
    }

    @Override
    public ExerciseDto updateById(Long id, UpdateExerciseRequest request) {
        Exercise existedExercise = exerciseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find exercise by id " + id));
        existedExercise.setName(request.getName());
        existedExercise.setAbout(request.getAbout());
        existedExercise.setImageData(request.getImageData());
        return exerciseMapper.toDto(exerciseRepository.save(existedExercise));
    }

    @Override
    public ExerciseDto updateExerciseName(Long id, UpdateExerciseNameRequest request) {
        Exercise existedExercise = exerciseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find exercise by id " + id));
        existedExercise.setName(request.getName());
        Exercise savedExercise = exerciseRepository.save(existedExercise);
        return exerciseMapper.toDto(savedExercise);
    }

    @Override
    public ExerciseDto updateExerciseAbout(Long id, UpdateExerciseAboutRequest request) {
        Exercise existedExercise = exerciseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find exercise by id " + id));
        existedExercise.setAbout(request.getAbout());
        Exercise savedExercise = exerciseRepository.save(existedExercise);
        return exerciseMapper.toDto(savedExercise);
    }

    @Override
    public ExerciseDto updateExerciseImage(Long id, UpdateExerciseImageRequest request) {
        Exercise existedExercise = exerciseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find exercise by id " + id));
        existedExercise.setImageData(request.getImageData());
        Exercise savedExercise = exerciseRepository.save(existedExercise);
        return exerciseMapper.toDto(savedExercise);
    }

    @Override
    public ExerciseDto updateExerciseSection(Long id, UpdateExerciseSectionRequest request) {
        Exercise existedExercise = exerciseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find exercise by id " + id));
        TrainingSection trainingSection =
                trainingSectionRepository.findById(request.getTrainingSectionId())
                        .orElseThrow(() ->
                                new EntityNotFoundException("Can't find seciton by id "
                                        + request.getTrainingSectionId()));
        existedExercise.setTrainingSection(trainingSection);
        Exercise savedExercise = exerciseRepository.save(existedExercise);
        return exerciseMapper.toDto(savedExercise);
    }
}
