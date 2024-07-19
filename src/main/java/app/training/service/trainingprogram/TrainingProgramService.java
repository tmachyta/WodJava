package app.training.service.trainingprogram;

import app.training.dto.trainingprogram.CreateTrainingProgramRequest;
import app.training.dto.trainingprogram.TrainingProgramDto;
import app.training.dto.trainingprogram.UpdateTrainingProgramAboutRequest;
import app.training.dto.trainingprogram.UpdateTrainingProgramDateRequest;
import app.training.dto.trainingprogram.UpdateTrainingProgramImageRequest;
import app.training.dto.trainingprogram.UpdateTrainingProgramNameRequest;
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

    TrainingProgramDto findByDate(LocalDate date);

    TrainingProgramDto updateTrainingProgramByDate(Long id,
                                                   UpdateTrainingProgramDateRequest request);

    TrainingProgramDto updateTrainingProgramName(Long id,
                                                 UpdateTrainingProgramNameRequest request);

    TrainingProgramDto updateTrainingProgramAbout(Long id,
                                                  UpdateTrainingProgramAboutRequest request);

    TrainingProgramDto updateTrainingProgramImage(Long id,
                                                  UpdateTrainingProgramImageRequest request);
}
