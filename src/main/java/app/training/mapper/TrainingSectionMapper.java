package app.training.mapper;

import app.training.config.MapperConfig;
import app.training.dto.trainingsection.CreateTrainingSectionRequest;
import app.training.dto.trainingsection.TrainingSectionDto;
import app.training.dto.trainingsection.UpdateTrainingSectionRequest;
import app.training.model.TrainingSection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface TrainingSectionMapper {

    TrainingSectionDto toDto(TrainingSection section);

    @Mapping(target = "id", ignore = true)
    TrainingSection toModel(CreateTrainingSectionRequest request);

    @Mapping(target = "id", ignore = true)
    TrainingSection toUpdateModel(UpdateTrainingSectionRequest request);
}
