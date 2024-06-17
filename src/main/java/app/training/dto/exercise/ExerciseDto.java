package app.training.dto.exercise;

import java.net.URL;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExerciseDto {
    private Long id;
    private String name;
    private String about;
    private String videoRelativePath;
    private URL video;
    private byte[] imageData;
}
