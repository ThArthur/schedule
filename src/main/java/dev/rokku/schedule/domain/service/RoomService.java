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
public class RoomService {
    private final RoomRepository roomRepository;
    private final BuildingRepository buildingRepository;

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
            try {
                room.setImage(image.getBytes());
            } catch (IOException e) {
                log.error("Error reading image bytes", e);
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing image");
            }
        }
        return toResponse(roomRepository.save(room));
    }

    @Transactional
    public RoomResponse update(Long id, RoomRequest request, MultipartFile image) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Room not found"));
        updateEntity(room, request);
        if (image != null && !image.isEmpty()) {
            try {
                room.setImage(image.getBytes());
            } catch (IOException e) {
                log.error("Error reading image bytes", e);
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing image");
            }
        }
        return toResponse(roomRepository.save(room));
    }

    @Transactional
    public void delete(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Room not found");
        }
        roomRepository.deleteById(id);
    }

    private void updateEntity(Room room, RoomRequest request) {
        room.setFloor(request.floor());
        room.setNumber(request.number());
        
        Building building = buildingRepository.findById(request.buildingId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Building not found"));
        room.setBuilding(building);
    }

    private RoomResponse toResponse(Room room) {
        String imageUrl = room.getImage() != null ? "/api/rooms/" + room.getId() + "/image" : null;
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

    @Transactional(readOnly = true)
    public byte[] getImage(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Room not found"));
        if (room.getImage() == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Image not found for this room");
        }
        return room.getImage();
    }
}
