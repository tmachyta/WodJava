package app.training.dto.trainingsection;

import app.training.dto.exercise.ExerciseDto;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TrainingSectionDto {
    private Long id;
    private String name;
    private byte[] imageData;
    private List<ExerciseDto> exercises;
}
