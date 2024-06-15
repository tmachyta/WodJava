package app.training.dto.trainingprogram;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateTrainingProgramRequest {
    private String name;
    private String about;
    private byte[] imageData;
}
