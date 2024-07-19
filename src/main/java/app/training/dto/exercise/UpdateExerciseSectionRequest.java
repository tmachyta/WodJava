package app.training.dto.exercise;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateExerciseSectionRequest {
    private Long trainingSectionId;
}
