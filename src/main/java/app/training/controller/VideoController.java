package app.training.controller;

import app.training.dto.video.CreateVideoRequest;
import app.training.dto.video.VideoDto;
import app.training.service.video.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Video management", description = "Endpoints for managing Videos")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/videos")
public class VideoController {
    private final VideoService videoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Save Video to repository",
            description = "Save valid Video to repository")
    public VideoDto save(@RequestBody @Valid CreateVideoRequest request) {
        return videoService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get list of Videos",
            description = "Get valid list of videos")
    public List<VideoDto> getAll(@ParameterObject Pageable pageable) {
        return videoService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get Video by ID",
            description = "Get valid Video by ID")
    public VideoDto getById(@PathVariable Long id) {
        return videoService.findById(id);
    }

    @Operation(summary = "Delete Video by ID",
            description = "Soft-delete valid Video by ID")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        videoService.deleteById(id);
    }
}
