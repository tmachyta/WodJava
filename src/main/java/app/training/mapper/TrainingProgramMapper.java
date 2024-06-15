package app.training.mapper;

import app.training.config.MapperConfig;
import app.training.dto.trainingprogram.CreateTrainingProgramRequest;
import app.training.dto.trainingprogram.TrainingProgramDto;
import app.training.dto.trainingprogram.UpdateTrainingProgramRequest;
import app.training.model.TrainingProgram;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface TrainingProgramMapper {

    TrainingProgramDto toDto(TrainingProgram program);

    @Mapping(target = "id", ignore = true)
    TrainingProgram toModel(CreateTrainingProgramRequest request);

    @Mapping(target = "id", ignore = true)
    TrainingProgram toUpdateModel(UpdateTrainingProgramRequest request);
}
