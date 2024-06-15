package app.training.controller;

import app.training.dto.exercise.CreateExerciseRequest;
import app.training.dto.exercise.ExerciseDto;
import app.training.dto.exercise.UpdateExerciseRequest;
import app.training.service.exercise.ExerciseService;
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

@Tag(name = "Exercise management", description = "Endpoints for managing Exercises")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exercises")
public class ExerciseController {
    private final ExerciseService exerciseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Save Exercise to repository",
            description = "Save valid Exercise to repository")
    public ExerciseDto save(@RequestBody @Valid CreateExerciseRequest request) {
        return exerciseService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get list of Exercises",
            description = "Get valid list of Exercises")
    public List<ExerciseDto> getAll(@ParameterObject Pageable pageable) {
        return exerciseService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get Exercise by ID",
            description = "Get valid Exercise by ID")
    public ExerciseDto getById(@PathVariable Long id) {
        return exerciseService.findById(id);
    }

    @Operation(summary = "Delete Exercise by ID",
            description = "Soft-delete valid Exercise by ID")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        exerciseService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Update Exercise by ID",
            description = "Update valid Exercise by ID")
    public ExerciseDto updateById(@PathVariable Long id,
                                    @RequestBody UpdateExerciseRequest request) {
        return exerciseService.updateById(id, request);
    }
}
