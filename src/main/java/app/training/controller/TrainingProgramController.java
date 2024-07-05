package app.training.controller;

import app.training.dto.trainingprogram.CreateTrainingProgramRequest;
import app.training.dto.trainingprogram.TrainingProgramDto;
import app.training.dto.trainingprogram.UpdateTrainingProgramRequest;
import app.training.service.trainingprogram.TrainingProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public TrainingProgramDto save(@RequestBody @Valid CreateTrainingProgramRequest request) {
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
}
