package dev.rokku.schedule.domain.service;

import dev.rokku.schedule.domain.dto.building.request.BuildingRequest;
import dev.rokku.schedule.domain.dto.building.response.BuildingResponse;
import dev.rokku.schedule.domain.exception.ApiException;
import dev.rokku.schedule.domain.model.building.Building;
import dev.rokku.schedule.domain.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuildingService {
    private final BuildingRepository buildingRepository;

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
            try {
                building.setImage(image.getBytes());
            } catch (IOException e) {
                log.error("Error reading image bytes", e);
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing image");
            }
        }
        return toResponse(buildingRepository.save(building));
    }

    @Transactional
    public BuildingResponse update(Long id, BuildingRequest request, MultipartFile image) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Building not found"));
        updateEntity(building, request);
        if (image != null && !image.isEmpty()) {
            try {
                building.setImage(image.getBytes());
            } catch (IOException e) {
                log.error("Error reading image bytes", e);
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing image");
            }
        }
        return toResponse(buildingRepository.save(building));
    }

    @Transactional
    public void delete(Long id) {
        if (!buildingRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Building not found");
        }
        buildingRepository.deleteById(id);
    }

    private void updateEntity(Building building, BuildingRequest request) {
        building.setName(request.name());
        building.setNumber(request.number());
        building.setComplement(request.complement());
    }

    private BuildingResponse toResponse(Building building) {
        String imageUrl = building.getImage() != null ? "/api/buildings/" + building.getId() + "/image" : null;
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

    @Transactional(readOnly = true)
    public byte[] getImage(Long id) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Building not found"));
        if (building.getImage() == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Image not found for this building");
        }
        return building.getImage();
    }
}
