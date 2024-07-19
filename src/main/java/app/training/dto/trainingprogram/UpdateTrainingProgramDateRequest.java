package app.training.dto.trainingprogram;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateTrainingProgramDateRequest {
    private LocalDate date;
}
