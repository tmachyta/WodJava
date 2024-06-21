package app.training.dto.video;

import java.net.URL;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateVideoRequest {
    private String name;
    private String videoRelativePath;
    private URL video;
    private Long exerciseId;
}
