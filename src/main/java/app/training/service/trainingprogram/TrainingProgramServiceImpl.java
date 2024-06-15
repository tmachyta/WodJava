package app.training.service.trainingprogram;

import app.training.dto.trainingprogram.CreateTrainingProgramRequest;
import app.training.dto.trainingprogram.TrainingProgramDto;
import app.training.dto.trainingprogram.UpdateTrainingProgramRequest;
import app.training.exception.EntityNotFoundException;
import app.training.mapper.TrainingProgramMapper;
import app.training.mapper.TrainingSectionMapper;
import app.training.model.TrainingProgram;
import app.training.repository.TrainingProgramRepository;
import app.training.repository.TrainingSectionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingProgramServiceImpl implements TrainingProgramService {
    private final TrainingProgramRepository trainingProgramRepository;
    private final TrainingProgramMapper trainingProgramMapper;
    private final TrainingSectionRepository trainingSectionRepository;
    private final TrainingSectionMapper trainingSectionMapper;

    @Override
    public List<TrainingProgramDto> getAll(Pageable pageable) {
        return trainingProgramRepository.findAll(pageable)
                .stream()
                .map(trainingProgramMapper::toDto)
                .toList();
    }

    @Override
    public TrainingProgramDto create(CreateTrainingProgramRequest request) {
        TrainingProgram trainingProgram = trainingProgramMapper.toModel(request);
        return trainingProgramMapper.toDto(trainingProgramRepository.save(trainingProgram));
    }

    @Override
    public TrainingProgramDto findById(Long id) {
        TrainingProgram trainingProgram = trainingProgramRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find training program by id " + id));
        return trainingProgramMapper.toDto(trainingProgram);
    }

    @Override
    public void deleteById(Long id) {
        trainingProgramRepository.deleteById(id);
    }

    @Override
    public TrainingProgramDto updateById(Long id, UpdateTrainingProgramRequest request) {
        TrainingProgram existedTrainingProgram = trainingProgramRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find training program by id " + id));

        existedTrainingProgram.setName(request.getName());
        existedTrainingProgram.setAbout(request.getAbout());
        existedTrainingProgram.setImageData(request.getImageData());
        return trainingProgramMapper.toDto(trainingProgramRepository.save(existedTrainingProgram));
    }
}
