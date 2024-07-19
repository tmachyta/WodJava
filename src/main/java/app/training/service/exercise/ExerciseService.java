package app.training.service.exercise;

import app.training.dto.exercise.CreateExerciseRequest;
import app.training.dto.exercise.ExerciseDto;
import app.training.dto.exercise.UpdateExerciseAboutRequest;
import app.training.dto.exercise.UpdateExerciseImageRequest;
import app.training.dto.exercise.UpdateExerciseNameRequest;
import app.training.dto.exercise.UpdateExerciseRequest;
import app.training.dto.exercise.UpdateExerciseSectionRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ExerciseService {

    List<ExerciseDto> getAll(Pageable pageable);

    ExerciseDto create(CreateExerciseRequest request);

    ExerciseDto findById(Long id);

    void deleteById(Long id);

    ExerciseDto updateById(Long id, UpdateExerciseRequest request);

    ExerciseDto updateExerciseName(Long id, UpdateExerciseNameRequest request);

    ExerciseDto updateExerciseAbout(Long id, UpdateExerciseAboutRequest request);

    ExerciseDto updateExerciseImage(Long id, UpdateExerciseImageRequest request);

    ExerciseDto updateExerciseSection(Long id, UpdateExerciseSectionRequest request);
}
