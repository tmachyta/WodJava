package app.training.controller;

import app.training.dto.trainingprogram.CreateTrainingProgramRequest;
import app.training.dto.trainingprogram.TrainingProgramDto;
import app.training.dto.trainingprogram.UpdateTrainingProgramAboutRequest;
import app.training.dto.trainingprogram.UpdateTrainingProgramDateRequest;
import app.training.dto.trainingprogram.UpdateTrainingProgramImageRequest;
import app.training.dto.trainingprogram.UpdateTrainingProgramNameRequest;
import app.training.dto.trainingprogram.UpdateTrainingProgramRequest;
import app.training.service.trainingprogram.TrainingProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
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
@RequestMapping(value = "/programs")
public class TrainingProgramController {
    private final TrainingProgramService trainingProgramService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Save Training Program to repository",
            description = "Save valid Training Program to repository")
    public TrainingProgramDto save(@RequestBody @Valid CreateTrainingProgramRequest request)
            throws IOException {
        return trainingProgramService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @Operation(summary = "Get list of Training Program",
            description = "Get valid list of Training Programs")
    public List<TrainingProgramDto> getAll(@ParameterObject Pageable pageable) {
        return trainingProgramService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @Operation(summary = "Get Training Program by ID",
            description = "Get valid Training Program by ID")
    public TrainingProgramDto getById(@PathVariable Long id) {
        return trainingProgramService.findById(id);
    }

    @Operation(summary = "Delete Training Program by ID",
            description = "Soft-delete valid Training Program by ID")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        trainingProgramService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update Training Program by ID",
            description = "Update valid Training Program by ID")
    public TrainingProgramDto updateById(@PathVariable Long id,
                                         @RequestBody UpdateTrainingProgramRequest request) {
        return trainingProgramService.updateById(id, request);
    }

    @GetMapping("/by-date/{date}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @Operation(summary = "Get list of Training Program",
            description = "Get valid list of Training Programs by date")
    public List<TrainingProgramDto> getAllByDate(@PathVariable LocalDate date,
                                                 @ParameterObject Pageable pageable) {
        return trainingProgramService.getAllByDate(date, pageable);
    }

    @GetMapping("/program-by-date/{date}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @Operation(summary = "Get Training Program by date",
            description = "Get valid Training Program by date")
    public TrainingProgramDto getByDate(@PathVariable LocalDate date) {
        return trainingProgramService.findByDate(date);
    }

    @PutMapping("/name/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update Training Program Name by ID",
            description = "Update valid Training Program Name by ID")
    public TrainingProgramDto updateProgramName(@PathVariable Long id,
                                                @RequestBody
                                                UpdateTrainingProgramNameRequest request) {
        return trainingProgramService.updateTrainingProgramName(id, request);
    }

    @PutMapping("/about/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update Training Program about by ID",
            description = "Update valid Training Program about by ID")
    public TrainingProgramDto updateProgramAbout(@PathVariable Long id,
                                                 @RequestBody
                                                 UpdateTrainingProgramAboutRequest request) {
        return trainingProgramService.updateTrainingProgramAbout(id, request);
    }

    @PutMapping("/date/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update Training Program date by ID",
            description = "Update valid Training Program date by ID")
    public TrainingProgramDto updateProgramDate(@PathVariable Long id,
                                                @RequestBody
                                                UpdateTrainingProgramDateRequest request) {
        return trainingProgramService.updateTrainingProgramByDate(id, request);
    }

    @PutMapping("/image/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update Training Program image by ID",
            description = "Update valid Training Program image by ID")
    public TrainingProgramDto updateProgramImage(@PathVariable Long id,
                                                 @RequestBody
                                                 UpdateTrainingProgramImageRequest request) {
        return trainingProgramService.updateTrainingProgramImage(id, request);
    }
}
