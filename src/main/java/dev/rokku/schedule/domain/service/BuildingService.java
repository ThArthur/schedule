package dev.rokku.schedule.domain.service;

import dev.rokku.schedule.domain.dto.building.request.BuildingRequest;
import dev.rokku.schedule.domain.dto.building.response.BuildingResponse;
import dev.rokku.schedule.domain.exception.ApiException;
import dev.rokku.schedule.domain.model.building.Building;
import dev.rokku.schedule.domain.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
    public BuildingResponse create(BuildingRequest request) {
        Building building = new Building();
        updateEntity(building, request);
        return toResponse(buildingRepository.save(building));
    }

    @Transactional
    public BuildingResponse update(Long id, BuildingRequest request) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Building not found"));
        updateEntity(building, request);
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
        return new BuildingResponse(
                building.getId(),
                building.getName(),
                building.getNumber(),
                building.getComplement(),
                building.getCreatedAt(),
                building.getUpdatedAt()
        );
    }
}
