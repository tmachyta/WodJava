package app.training.dto.trainingprogram;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateTrainingProgramImageRequest {
    private byte[] imageData;
}
