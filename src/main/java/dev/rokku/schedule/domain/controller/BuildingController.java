package dev.rokku.schedule.domain.controller;

import dev.rokku.schedule.domain.dto.building.request.BuildingRequest;
import dev.rokku.schedule.domain.dto.building.response.BuildingResponse;
import dev.rokku.schedule.domain.service.BuildingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;

    @GetMapping
    public List<BuildingResponse> findAll() {
        return buildingService.findAll();
    }

    @GetMapping("/{id}")
    public BuildingResponse findById(@PathVariable Long id) {
        return buildingService.findById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public BuildingResponse create(
            @RequestPart("building") @Valid BuildingRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return buildingService.create(request, image);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public BuildingResponse update(
            @PathVariable Long id,
            @RequestPart("building") @Valid BuildingRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return buildingService.update(id, request, image);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        buildingService.delete(id);
    }
}
