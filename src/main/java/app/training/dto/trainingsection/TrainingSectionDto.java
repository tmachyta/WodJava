package app.training.dto.trainingsection;

import app.training.model.Exercise;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TrainingSectionDto {
    private Long id;
    private String name;
    private byte[] imageData;
    private Set<Exercise> exercises;
}
