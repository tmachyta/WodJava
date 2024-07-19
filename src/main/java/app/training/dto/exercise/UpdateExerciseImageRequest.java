package app.training.dto.exercise;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateExerciseImageRequest {
    private byte[] imageData;
}
