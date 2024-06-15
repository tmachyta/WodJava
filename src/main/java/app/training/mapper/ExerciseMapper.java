package app.training.mapper;

import app.training.config.MapperConfig;
import app.training.dto.exercise.CreateExerciseRequest;
import app.training.dto.exercise.ExerciseDto;
import app.training.dto.exercise.UpdateExerciseRequest;
import app.training.model.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ExerciseMapper {

    ExerciseDto toDto(Exercise exercise);

    @Mapping(target = "id", ignore = true)
    Exercise toModel(CreateExerciseRequest request);

    @Mapping(target = "id", ignore = true)
    Exercise toUpdateModel(UpdateExerciseRequest request);
}
