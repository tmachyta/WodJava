package app.training.dto.trainingprogram;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateTrainingProgramNameRequest {
    private String name;
}