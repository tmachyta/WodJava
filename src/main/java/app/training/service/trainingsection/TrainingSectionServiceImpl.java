package app.training.service.trainingsection;

import app.training.dto.trainingsection.CreateTrainingSectionRequest;
import app.training.dto.trainingsection.TrainingSectionDto;
import app.training.dto.trainingsection.UpdateTrainingSectionImageRequest;
import app.training.dto.trainingsection.UpdateTrainingSectionNameRequest;
import app.training.dto.trainingsection.UpdateTrainingSectionProgramRequest;
import app.training.dto.trainingsection.UpdateTrainingSectionRequest;
import app.training.exception.EntityNotFoundException;
import app.training.mapper.TrainingSectionMapper;
import app.training.model.TrainingProgram;
import app.training.model.TrainingSection;
import app.training.repository.TrainingProgramRepository;
import app.training.repository.TrainingSectionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingSectionServiceImpl implements TrainingSectionService {
    private final TrainingSectionRepository trainingSectionRepository;
    private final TrainingSectionMapper trainingSectionMapper;
    private final TrainingProgramRepository trainingProgramRepository;

    @Override
    public List<TrainingSectionDto> getAll(Pageable pageable) {
        return trainingSectionRepository.findAll(pageable)
                .stream()
                .map(trainingSectionMapper::toDto)
                .toList();
    }

    @Override
    public TrainingSectionDto create(CreateTrainingSectionRequest request) {
        TrainingProgram trainingProgram =
                trainingProgramRepository.findById(request.getTrainingProgramId())
                        .orElseThrow(() ->
                                new EntityNotFoundException("Can't find program by id "
                                        + request.getTrainingProgramId()));
        TrainingSection trainingSection = trainingSectionMapper.toModel(request);
        trainingSection.setTrainingProgram(trainingProgram);
        return trainingSectionMapper.toDto(trainingSectionRepository.save(trainingSection));
    }

    @Override
    public TrainingSectionDto findById(Long id) {
        TrainingSection trainingSection = trainingSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find section by id " + id));
        return trainingSectionMapper.toDto(trainingSection);
    }

    @Override
    public void deleteById(Long id) {
        trainingSectionRepository.deleteById(id);
    }

    @Override
    public TrainingSectionDto updateById(Long id, UpdateTrainingSectionRequest request) {
        TrainingSection existedTrainingSection = trainingSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find section by id " + id));

        existedTrainingSection.setName(request.getName());
        existedTrainingSection.setImageData(request.getImageData());
        return trainingSectionMapper.toDto(trainingSectionRepository.save(existedTrainingSection));
    }

    @Override
    public TrainingSectionDto updateTrainingSectionName(
            Long id,
            UpdateTrainingSectionNameRequest request) {
        TrainingSection existedTrainingSection = trainingSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find section by id " + id));
        existedTrainingSection.setName(request.getName());
        TrainingSection savedSection = trainingSectionRepository.save(existedTrainingSection);
        return trainingSectionMapper.toDto(savedSection);
    }

    @Override
    public TrainingSectionDto updateTrainingSectionImage(
            Long id,
            UpdateTrainingSectionImageRequest request) {
        TrainingSection existedTrainingSection =
                trainingSectionRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Can't find section by id " + id));
        existedTrainingSection.setImageData(request.getImageData());
        TrainingSection savedSection = trainingSectionRepository.save(existedTrainingSection);
        return trainingSectionMapper.toDto(savedSection);
    }

    @Override
    public TrainingSectionDto updateTrainingSectionProgram(
            Long id,
            UpdateTrainingSectionProgramRequest request) {
        TrainingSection existedTrainingSection = trainingSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find section by id " + id));
        TrainingProgram trainingProgram =
                trainingProgramRepository.findById(request.getTrainingProgramId())
                        .orElseThrow(() ->
                                new EntityNotFoundException("Can't find program by id "
                                        + request.getTrainingProgramId()));
        existedTrainingSection.setTrainingProgram(trainingProgram);
        TrainingSection savedSection = trainingSectionRepository.save(existedTrainingSection);
        return trainingSectionMapper.toDto(savedSection);
    }
}
