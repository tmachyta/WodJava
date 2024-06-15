package app.training.service.exercise;

import app.training.dto.exercise.CreateExerciseRequest;
import app.training.dto.exercise.ExerciseDto;
import app.training.dto.exercise.UpdateExerciseRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ExerciseService {

    List<ExerciseDto> getAll(Pageable pageable);

    ExerciseDto create(CreateExerciseRequest request);

    ExerciseDto findById(Long id);

    void deleteById(Long id);

    ExerciseDto updateById(Long id, UpdateExerciseRequest request);
}
