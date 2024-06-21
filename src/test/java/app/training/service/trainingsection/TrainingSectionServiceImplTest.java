package app.training.service.trainingsection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import app.training.dto.trainingsection.CreateTrainingSectionRequest;
import app.training.dto.trainingsection.TrainingSectionDto;
import app.training.dto.trainingsection.UpdateTrainingSectionRequest;
import app.training.exception.EntityNotFoundException;
import app.training.mapper.TrainingSectionMapper;
import app.training.model.TrainingProgram;
import app.training.model.TrainingSection;
import app.training.repository.TrainingProgramRepository;
import app.training.repository.TrainingSectionRepository;
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
class TrainingSectionServiceImplTest {
    private static final Long TRAINING_PROGRAM_ID = 1L;
    private static final Long TRAINING_SECTION_ID = 1L;
    private static final Long NON_EXISTED_TRAINING_SECTION_ID = 50L;
    private static final String OLD_NAME = "Old";
    private static final String UPDATED_NAME = "Updated";

    @Mock
    private TrainingSectionRepository trainingSectionRepository;

    @Mock
    private TrainingSectionMapper trainingSectionMapper;

    @Mock
    private TrainingProgramRepository trainingProgramRepository;

    @InjectMocks
    private TrainingSectionServiceImpl trainingSectionService;

    @Test
    public void testSuccessfullySavedTrainingSection() {
        CreateTrainingSectionRequest request = new CreateTrainingSectionRequest();
        request.setTrainingProgramId(TRAINING_PROGRAM_ID);
        TrainingProgram trainingProgram = new TrainingProgram();
        TrainingSection trainingSectionToSave = new TrainingSection();
        trainingProgram.setId(TRAINING_PROGRAM_ID);

        when(trainingProgramRepository.findById(trainingProgram.getId()))
                .thenReturn(Optional.of(trainingProgram));

        when(trainingSectionMapper.toModel(request)).thenReturn(trainingSectionToSave);

        trainingSectionToSave.setTrainingProgram(trainingProgram);

        when(trainingSectionRepository.save(trainingSectionToSave))
                .thenReturn(trainingSectionToSave);

        TrainingSectionDto trainingSectionDto = new TrainingSectionDto();

        when(trainingSectionMapper.toDto(trainingSectionToSave))
                .thenReturn(trainingSectionDto);

        TrainingSectionDto result = trainingSectionService.create(request);

        assertNotNull(result);
        assertEquals(trainingSectionDto, result);
    }

    @Test
    public void testGetAllTrainingSections() {
        TrainingSection trainingSection = new TrainingSection();
        Pageable pageable = PageRequest.of(0, 10);
        List<TrainingSection> trainingSections = List.of(new TrainingSection());
        List<TrainingSectionDto> expectedSections = List.of(new TrainingSectionDto());

        Page<TrainingSection> page =
                new PageImpl<>(trainingSections, pageable, trainingSections.size());

        when(trainingSectionRepository.findAll(pageable)).thenReturn(page);

        when(trainingSectionMapper.toDto(trainingSection)).thenReturn(new TrainingSectionDto());

        List<TrainingSectionDto> result = trainingSectionService.getAll(pageable);

        Assertions.assertEquals(expectedSections.size(), result.size());
    }

    @Test
    public void testFindTrainingSectionById() {
        TrainingSection trainingSection = new TrainingSection();
        trainingSection.setId(TRAINING_SECTION_ID);
        TrainingSectionDto trainingSectionDto = new TrainingSectionDto();
        trainingSectionDto.setId(TRAINING_SECTION_ID);

        when(trainingSectionRepository.findById(trainingSection.getId()))
                .thenReturn(Optional.of(trainingSection));

        when(trainingSectionMapper.toDto(trainingSection))
                .thenReturn(trainingSectionDto);

        TrainingSectionDto result = trainingSectionService.findById(TRAINING_SECTION_ID);

        Assertions.assertEquals(trainingSection.getId(), result.getId());
    }

    @Test
    public void testFindTrainingSectionNonExistedId() {
        when(trainingSectionRepository.findById(NON_EXISTED_TRAINING_SECTION_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> trainingSectionService.findById(NON_EXISTED_TRAINING_SECTION_ID));
    }

    @Test
    public void testDeleteTrainingSectionById() {
        trainingSectionService.deleteById(TRAINING_SECTION_ID);

        when(trainingSectionRepository.findById(TRAINING_SECTION_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> trainingSectionService.findById(TRAINING_SECTION_ID));
    }

    @Test
    public void testUpdateTrainingSectionSuccessfully() {
        UpdateTrainingSectionRequest request = new UpdateTrainingSectionRequest();
        request.setName(UPDATED_NAME);

        TrainingSectionDto expectedResult = new TrainingSectionDto();
        expectedResult.setName(UPDATED_NAME);

        TrainingSection trainingSection = new TrainingSection();
        trainingSection.setName(OLD_NAME);

        when(trainingSectionRepository.findById(TRAINING_SECTION_ID))
                .thenReturn(Optional.of(trainingSection));

        when(trainingSectionRepository.save(trainingSection)).thenReturn(trainingSection);

        when(trainingSectionMapper.toDto(trainingSection)).thenReturn(expectedResult);

        TrainingSectionDto updatedTrainingSection =
                trainingSectionService.updateById(TRAINING_SECTION_ID, request);

        Assertions.assertEquals(updatedTrainingSection.getName(), expectedResult.getName());
    }
}
