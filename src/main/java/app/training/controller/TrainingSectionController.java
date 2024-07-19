package app.training.controller;

import app.training.dto.trainingsection.CreateTrainingSectionRequest;
import app.training.dto.trainingsection.TrainingSectionDto;
import app.training.dto.trainingsection.UpdateTrainingSectionImageRequest;
import app.training.dto.trainingsection.UpdateTrainingSectionNameRequest;
import app.training.dto.trainingsection.UpdateTrainingSectionProgramRequest;
import app.training.dto.trainingsection.UpdateTrainingSectionRequest;
import app.training.service.trainingsection.TrainingSectionService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Training Sections management",
        description = "Endpoints for managing Training Sections")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/sections")
public class TrainingSectionController {
    private final TrainingSectionService trainingSectionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Save Training Section to repository",
            description = "Save valid Training Section to repository")
    public TrainingSectionDto save(@RequestBody @Valid CreateTrainingSectionRequest request) {
        return trainingSectionService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @Operation(summary = "Get list of Training Sections",
            description = "Get valid list of Training Sections")
    public List<TrainingSectionDto> getAll(@ParameterObject Pageable pageable) {
        return trainingSectionService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @Operation(summary = "Get Training Section by ID",
            description = "Get valid Training Section by ID")
    public TrainingSectionDto getById(@PathVariable Long id) {
        return trainingSectionService.findById(id);
    }

    @Operation(summary = "Delete Training Section by ID",
            description = "Soft-delete valid Training Section by ID")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        trainingSectionService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update Training Section by ID",
            description = "Update valid Training Section by ID")
    public TrainingSectionDto updateById(@PathVariable Long id,
                                  @RequestBody UpdateTrainingSectionRequest request) {
        return trainingSectionService.updateById(id, request);
    }

    @PutMapping("/name/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update Training Section name by ID",
            description = "Update valid Training Section name by ID")
    public TrainingSectionDto updateSectionName(@PathVariable Long id,
                                         @RequestBody UpdateTrainingSectionNameRequest request) {
        return trainingSectionService.updateTrainingSectionName(id, request);
    }

    @PutMapping("/image/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update Training Section image by ID",
            description = "Update valid Training Section image by ID")
    public TrainingSectionDto updateSectionImage(@PathVariable Long id,
                                         @RequestBody UpdateTrainingSectionImageRequest request) {
        return trainingSectionService.updateTrainingSectionImage(id, request);
    }

    @PutMapping("/program/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update Training Section program by ID",
            description = "Update valid Training Section program by ID")
    public TrainingSectionDto updateSectionProgram(@PathVariable Long id,
                                         @RequestBody UpdateTrainingSectionProgramRequest request) {
        return trainingSectionService.updateTrainingSectionProgram(id, request);
    }
}
