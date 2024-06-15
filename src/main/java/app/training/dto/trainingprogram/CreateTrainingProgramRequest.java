package app.training.dto.trainingprogram;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateTrainingProgramRequest {
    private String name;
    private String about;
    private byte[] imageData;
    private LocalDate date;
}
