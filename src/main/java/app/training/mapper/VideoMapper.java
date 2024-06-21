package app.training.mapper;

import app.training.config.MapperConfig;
import app.training.dto.video.CreateVideoRequest;
import app.training.dto.video.VideoDto;
import app.training.model.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface VideoMapper {
    VideoDto toDto(Video video);

    @Mapping(target = "id", ignore = true)
    Video toModel(CreateVideoRequest request);
}
