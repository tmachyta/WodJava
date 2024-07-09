package app.training.dto.exercise;

import app.training.dto.video.VideoDto;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExerciseDto {
    private Long id;
    private String name;
    private String about;
    private byte[] imageData;
    private List<VideoDto> videos;
}
