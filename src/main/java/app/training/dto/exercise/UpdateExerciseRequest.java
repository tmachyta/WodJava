package app.training.dto.exercise;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateExerciseRequest {
    private String name;
    private String about;
    private byte[] imageData;
}
