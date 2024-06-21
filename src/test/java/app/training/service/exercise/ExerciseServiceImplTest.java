package app.training.service.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import app.training.dto.exercise.CreateExerciseRequest;
import app.training.dto.exercise.ExerciseDto;
import app.training.dto.exercise.UpdateExerciseRequest;
import app.training.exception.EntityNotFoundException;
import app.training.mapper.ExerciseMapper;
import app.training.model.Exercise;
import app.training.model.TrainingSection;
import app.training.repository.ExerciseRepository;
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
class ExerciseServiceImplTest {
    private static final Long EXERCISE_ID = 1L;
    private static final Long TRAINING_SECTION_ID = 1L;
    private static final Long NON_EXISTED_EXERCISE_ID = 50L;
    private static final String OLD_NAME = "Old";
    private static final String UPDATED_NAME = "Updated";
    private static final String UPDATED_ABOUT = "Updated";
    private static final String OLD_ABOUT = "Old";

    @Mock
    private ExerciseMapper exerciseMapper;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private TrainingSectionRepository trainingSectionRepository;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    @Test
    public void testSuccessfullySavedExercise() {
        CreateExerciseRequest request = new CreateExerciseRequest();
        request.setTrainingSectionId(TRAINING_SECTION_ID);
        TrainingSection trainingSection = new TrainingSection();
        Exercise exerciseToSave = new Exercise();
        trainingSection.setId(TRAINING_SECTION_ID);

        when(trainingSectionRepository.findById(trainingSection.getId()))
                .thenReturn(Optional.of(trainingSection));

        when(exerciseMapper.toModel(request)).thenReturn(exerciseToSave);

        exerciseToSave.setTrainingSection(trainingSection);

        when(exerciseRepository.save(exerciseToSave)).thenReturn(exerciseToSave);

        ExerciseDto exerciseDto = new ExerciseDto();

        when(exerciseMapper.toDto(exerciseToSave)).thenReturn(exerciseDto);

        ExerciseDto result = exerciseService.create(request);

        assertNotNull(result);
        assertEquals(exerciseDto, result);
    }

    @Test
    public void testGetAllExercises() {
        Exercise exercise = new Exercise();
        Pageable pageable = PageRequest.of(0, 10);
        List<Exercise> exercises = List.of(new Exercise());
        List<ExerciseDto> expectedResult = List.of(new ExerciseDto());

        Page<Exercise> page = new PageImpl<>(exercises, pageable, exercises.size());

        when(exerciseRepository.findAll(pageable)).thenReturn(page);

        when(exerciseMapper.toDto(exercise)).thenReturn(new ExerciseDto());

        List<ExerciseDto> result = exerciseService.getAll(pageable);

        Assertions.assertEquals(expectedResult.size(), result.size());

    }

    @Test
    public void testFindExerciseById() {
        Exercise exercise = new Exercise();
        exercise.setId(EXERCISE_ID);
        ExerciseDto exerciseDto = new ExerciseDto();
        exerciseDto.setId(EXERCISE_ID);

        when(exerciseRepository.findById(exercise.getId()))
                .thenReturn(Optional.of(exercise));

        when(exerciseMapper.toDto(exercise)).thenReturn(exerciseDto);

        ExerciseDto result = exerciseService.findById(EXERCISE_ID);

        Assertions.assertEquals(exercise.getId(), result.getId());
    }

    @Test
    public void testFindExerciseNonExistedId() {
        when(exerciseRepository.findById(NON_EXISTED_EXERCISE_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> exerciseService.findById(NON_EXISTED_EXERCISE_ID));
    }

    @Test
    public void testDeleteExerciseById() {
        exerciseService.deleteById(EXERCISE_ID);

        when(exerciseRepository.findById(EXERCISE_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> exerciseService.findById(EXERCISE_ID));
    }

    @Test
    public void testUpdateExerciseSuccessfully() {
        UpdateExerciseRequest request = new UpdateExerciseRequest();
        request.setName(UPDATED_NAME);
        request.setAbout(UPDATED_ABOUT);

        ExerciseDto expectedResult = new ExerciseDto();
        expectedResult.setName(UPDATED_NAME);
        expectedResult.setAbout(UPDATED_ABOUT);

        Exercise exercise = new Exercise();
        exercise.setName(OLD_NAME);
        exercise.setAbout(OLD_ABOUT);

        when(exerciseRepository.findById(EXERCISE_ID))
                .thenReturn(Optional.of(exercise));

        when(exerciseRepository.save(exercise)).thenReturn(exercise);

        when(exerciseMapper.toDto(exercise)).thenReturn(expectedResult);

        ExerciseDto updatedExercise = exerciseService.updateById(EXERCISE_ID, request);

        Assertions.assertEquals(updatedExercise.getName(), expectedResult.getName());

        Assertions.assertEquals(updatedExercise.getAbout(), expectedResult.getAbout());
    }
}
