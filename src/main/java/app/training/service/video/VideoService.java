package app.training.service.video;

import app.training.dto.video.CreateVideoRequest;
import app.training.dto.video.VideoDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface VideoService {

    List<VideoDto> getAll(Pageable pageable);

    VideoDto create(CreateVideoRequest request);

    VideoDto findById(Long id);

    void deleteById(Long id);
}
