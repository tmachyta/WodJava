package app.training.dto.trainingsection;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateTrainingSectionRequest {
    private String name;
    private byte[] imageData;
}
