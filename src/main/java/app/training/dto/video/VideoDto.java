package app.training.dto.video;

import java.net.URL;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VideoDto {
    private Long id;
    private String name;
    private String videoRelativePath;
    private URL video;
}
