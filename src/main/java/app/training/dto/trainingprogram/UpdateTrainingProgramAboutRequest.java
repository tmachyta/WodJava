package app.training.dto.trainingprogram;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateTrainingProgramAboutRequest {
    private String about;
}
