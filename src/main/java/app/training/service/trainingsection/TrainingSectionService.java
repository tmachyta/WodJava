package app.training.service.trainingsection;

import app.training.dto.trainingsection.CreateTrainingSectionRequest;
import app.training.dto.trainingsection.TrainingSectionDto;
import app.training.dto.trainingsection.UpdateTrainingSectionRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TrainingSectionService {

    List<TrainingSectionDto> getAll(Pageable pageable);

    TrainingSectionDto create(CreateTrainingSectionRequest request);

    TrainingSectionDto findById(Long id);

    void deleteById(Long id);

    TrainingSectionDto updateById(Long id, UpdateTrainingSectionRequest request);
}
