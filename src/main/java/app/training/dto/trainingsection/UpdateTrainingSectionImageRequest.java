package app.training.dto.trainingsection;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateTrainingSectionImageRequest {
    private byte[] imageData;
}
