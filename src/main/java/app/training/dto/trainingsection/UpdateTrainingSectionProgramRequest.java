package app.training.dto.trainingsection;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateTrainingSectionProgramRequest {
    private Long trainingProgramId;
}
