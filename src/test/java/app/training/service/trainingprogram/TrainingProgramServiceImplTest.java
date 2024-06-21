package app.training.service.trainingprogram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import app.training.dto.trainingprogram.CreateTrainingProgramRequest;
import app.training.dto.trainingprogram.TrainingProgramDto;
import app.training.dto.trainingprogram.UpdateTrainingProgramRequest;
import app.training.exception.EntityNotFoundException;
import app.training.mapper.TrainingProgramMapper;
import app.training.model.TrainingProgram;
import app.training.repository.TrainingProgramRepository;
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
class TrainingProgramServiceImplTest {
    private static final Long TRAINING_PROGRAM_ID = 1L;
    private static final Long NON_EXISTED_TRAINING_PROGRAM_ID = 50L;
    private static final String UPDATED_NAME = "Updated";
    private static final String OLD_NAME = "Old";
    private static final String UPDATED_ABOUT = "Updated";
    private static final String OLD_ABOUT = "Old";

    @Mock
    private TrainingProgramRepository trainingProgramRepository;

    @Mock
    private TrainingProgramMapper trainingProgramMapper;

    @InjectMocks
    private TrainingProgramServiceImpl trainingProgramService;

    @Test
    public void testSuccessfullySaveTrainingProgram() {
        CreateTrainingProgramRequest request = new CreateTrainingProgramRequest();
        TrainingProgramDto trainingProgramDto = new TrainingProgramDto();
        TrainingProgram trainingProgramToSave = new TrainingProgram();

        when(trainingProgramMapper.toModel(request)).thenReturn(trainingProgramToSave);

        when(trainingProgramRepository.save(trainingProgramToSave))
                .thenReturn(trainingProgramToSave);

        when(trainingProgramMapper.toDto(trainingProgramToSave)).thenReturn(trainingProgramDto);

        TrainingProgramDto result = trainingProgramService.create(request);

        assertNotNull(result);
        assertEquals(trainingProgramDto, result);
    }

    @Test
    public void testGetAllTrainingPrograms() {
        TrainingProgram trainingProgram = new TrainingProgram();
        Pageable pageable = PageRequest.of(0, 10);
        List<TrainingProgram> trainingPrograms = List.of(new TrainingProgram());
        List<TrainingProgramDto> expectedPrograms = List.of(new TrainingProgramDto());
        Page<TrainingProgram> page =
                new PageImpl<>(trainingPrograms, pageable, trainingPrograms.size());

        when(trainingProgramRepository.findAll(pageable)).thenReturn(page);

        when(trainingProgramMapper.toDto(trainingProgram)).thenReturn(new TrainingProgramDto());

        List<TrainingProgramDto> result = trainingProgramService.getAll(pageable);

        Assertions.assertEquals(expectedPrograms.size(), result.size());
    }

    @Test
    public void testFindTrainingProgramById() {
        TrainingProgram trainingProgram = new TrainingProgram();
        trainingProgram.setId(TRAINING_PROGRAM_ID);
        TrainingProgramDto trainingProgramDto = new TrainingProgramDto();
        trainingProgramDto.setId(TRAINING_PROGRAM_ID);

        when(trainingProgramRepository.findById(trainingProgram.getId()))
                .thenReturn(Optional.of(trainingProgram));

        when(trainingProgramMapper.toDto(trainingProgram)).thenReturn(trainingProgramDto);

        TrainingProgramDto result = trainingProgramService.findById(TRAINING_PROGRAM_ID);

        Assertions.assertEquals(trainingProgram.getId(), result.getId());
    }

    @Test
    public void testFindTrainingProgramNonExistedId() {
        when(trainingProgramRepository.findById(NON_EXISTED_TRAINING_PROGRAM_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> trainingProgramService.findById(NON_EXISTED_TRAINING_PROGRAM_ID));
    }

    @Test
    public void testDeleteTrainingProgramById() {
        trainingProgramService.deleteById(TRAINING_PROGRAM_ID);

        when(trainingProgramRepository.findById(TRAINING_PROGRAM_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> trainingProgramService.findById(TRAINING_PROGRAM_ID));
    }

    @Test
    public void testUpdateTrainingProgramSuccessfully() {
        UpdateTrainingProgramRequest request = new UpdateTrainingProgramRequest();
        request.setName(UPDATED_NAME);
        request.setAbout(UPDATED_ABOUT);

        TrainingProgramDto expectedResult = new TrainingProgramDto();
        expectedResult.setName(UPDATED_NAME);
        expectedResult.setAbout(UPDATED_ABOUT);

        TrainingProgram trainingProgram = new TrainingProgram();
        trainingProgram.setName(OLD_NAME);
        trainingProgram.setAbout(OLD_ABOUT);

        when(trainingProgramRepository.findById(TRAINING_PROGRAM_ID))
                .thenReturn(Optional.of(trainingProgram));

        when(trainingProgramRepository.save(trainingProgram)).thenReturn(trainingProgram);

        when(trainingProgramMapper.toDto(trainingProgram)).thenReturn(expectedResult);

        TrainingProgramDto updatedTrainingProgram =
                trainingProgramService.updateById(TRAINING_PROGRAM_ID, request);

        Assertions.assertEquals(updatedTrainingProgram.getName(), expectedResult.getName());

        Assertions.assertEquals(updatedTrainingProgram.getAbout(), expectedResult.getAbout());
    }
}
