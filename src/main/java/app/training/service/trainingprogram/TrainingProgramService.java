package app.training.service.trainingprogram;

import app.training.dto.trainingprogram.CreateTrainingProgramRequest;
import app.training.dto.trainingprogram.TrainingProgramDto;
import app.training.dto.trainingprogram.UpdateTrainingProgramRequest;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TrainingProgramService {
    List<TrainingProgramDto> getAll(Pageable pageable);

    TrainingProgramDto create(CreateTrainingProgramRequest request);

    TrainingProgramDto findById(Long id);

    void deleteById(Long id);

    TrainingProgramDto updateById(Long id, UpdateTrainingProgramRequest request);

    List<TrainingProgramDto> getAllByDate(LocalDate date, Pageable pageable);
}
