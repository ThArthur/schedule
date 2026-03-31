package dev.rokku.schedule.domain.service;

import dev.rokku.schedule.domain.dto.room.request.RoomRequest;
import dev.rokku.schedule.domain.dto.room.response.RoomResponse;
import dev.rokku.schedule.domain.exception.ApiException;
import dev.rokku.schedule.domain.model.building.Building;
import dev.rokku.schedule.domain.model.room.Room;
import dev.rokku.schedule.domain.repository.BuildingRepository;
import dev.rokku.schedule.domain.repository.RoomRepository;
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
public class RoomService {
    private final RoomRepository roomRepository;
    private final BuildingRepository buildingRepository;
    private final FileStorageService fileStorageService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Transactional(readOnly = true)
    public List<RoomResponse> findAll() {
        return roomRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomResponse findById(Long id) {
        return roomRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Room not found"));
    }

    @Transactional
    public RoomResponse create(RoomRequest request, MultipartFile image) {
        Room room = new Room();
        updateEntity(room, request);
        if (image != null && !image.isEmpty()) {
            String path = fileStorageService.storeFile(image, "rooms");
            room.setImageUrl(path);
        }
        return toResponse(roomRepository.save(room));
    }

    @Transactional
    public RoomResponse update(Long id, RoomRequest request, MultipartFile image) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Room not found"));
        updateEntity(room, request);
        if (image != null && !image.isEmpty()) {
            if (room.getImageUrl() != null) {
                fileStorageService.deleteFile(room.getImageUrl());
            }
            String path = fileStorageService.storeFile(image, "rooms");
            room.setImageUrl(path);
        }
        return toResponse(roomRepository.save(room));
    }

    @Transactional
    public void delete(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Room not found"));
        
        if (room.getImageUrl() != null) {
            fileStorageService.deleteFile(room.getImageUrl());
        }
        roomRepository.delete(room);
    }

    private void updateEntity(Room room, RoomRequest request) {
        room.setFloor(request.floor());
        room.setNumber(request.number());
        
        Building building = buildingRepository.findById(request.buildingId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Building not found"));
        room.setBuilding(building);
    }

    private RoomResponse toResponse(Room room) {
        String base = baseUrl;
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        String imageUrl = room.getImageUrl() != null ? base + "/uploads/" + room.getImageUrl() : null;
        return new RoomResponse(
                room.getId(),
                room.getFloor(),
                room.getNumber(),
                room.getBuilding().getId(),
                imageUrl,
                room.getCreatedAt(),
                room.getUpdatedAt()
        );
    }
}
