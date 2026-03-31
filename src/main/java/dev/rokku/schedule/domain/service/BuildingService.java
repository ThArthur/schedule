package dev.rokku.schedule.domain.service;

import dev.rokku.schedule.domain.dto.building.request.BuildingRequest;
import dev.rokku.schedule.domain.dto.building.response.BuildingResponse;
import dev.rokku.schedule.domain.exception.ApiException;
import dev.rokku.schedule.domain.model.building.Building;
import dev.rokku.schedule.domain.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuildingService {
    private final BuildingRepository buildingRepository;
    private final FileStorageService fileStorageService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Transactional(readOnly = true)
    public List<BuildingResponse> findAll() {
        return buildingRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BuildingResponse findById(Long id) {
        return buildingRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Building not found"));
    }

    @Transactional
    public BuildingResponse create(BuildingRequest request, MultipartFile image) {
        Building building = new Building();
        updateEntity(building, request);
        if (image != null && !image.isEmpty()) {
            String path = fileStorageService.storeFile(image, "buildings");
            building.setImageUrl(path);
        }
        return toResponse(buildingRepository.save(building));
    }

    @Transactional
    public BuildingResponse update(Long id, BuildingRequest request, MultipartFile image) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Building not found"));
        updateEntity(building, request);
        if (image != null && !image.isEmpty()) {
            if (building.getImageUrl() != null) {
                fileStorageService.deleteFile(building.getImageUrl());
            }
            String path = fileStorageService.storeFile(image, "buildings");
            building.setImageUrl(path);
        }
        return toResponse(buildingRepository.save(building));
    }

    @Transactional
    public void delete(Long id) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Building not found"));
        
        if (building.getImageUrl() != null) {
            fileStorageService.deleteFile(building.getImageUrl());
        }
        buildingRepository.delete(building);
    }

    private void updateEntity(Building building, BuildingRequest request) {
        building.setName(request.name());
        building.setNumber(request.number());
        building.setComplement(request.complement());
    }

    private BuildingResponse toResponse(Building building) {
        String base = baseUrl;
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        String imageUrl = (building.getImageUrl() != null && !building.getImageUrl().isEmpty())
                ? base + "/uploads/" + building.getImageUrl()
                : null;

        return new BuildingResponse(
                building.getId(),
                building.getName(),
                building.getNumber(),
                building.getComplement(),
                imageUrl,
                building.getCreatedAt(),
                building.getUpdatedAt()
        );
    }

}
