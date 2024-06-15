package app.training.dto.trainingprogram;

import app.training.dto.trainingsection.TrainingSectionDto;
import java.time.LocalDate;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TrainingProgramDto {
    private Long id;
    private String name;
    private String about;
    private byte[] imageData;
    private LocalDate date;
    private Set<TrainingSectionDto> sections;
}
