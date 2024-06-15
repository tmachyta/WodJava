package app.training.dto.trainingsection;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateTrainingSectionRequest {
    private String name;
    private byte[] imageData;
    private Long trainingProgramId;
}
