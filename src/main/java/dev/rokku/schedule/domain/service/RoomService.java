package dev.rokku.schedule.domain.service;

import dev.rokku.schedule.domain.dto.room.request.RoomRequest;
import dev.rokku.schedule.domain.dto.room.response.RoomResponse;
import dev.rokku.schedule.domain.exception.ApiException;
import dev.rokku.schedule.domain.model.building.Building;
import dev.rokku.schedule.domain.model.room.Room;
import dev.rokku.schedule.domain.repository.BuildingRepository;
import dev.rokku.schedule.domain.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
    public RoomResponse create(RoomRequest request) {
        Room room = new Room();
        updateEntity(room, request);
        return toResponse(roomRepository.save(room));
    }

    @Transactional
    public RoomResponse update(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Room not found"));
        updateEntity(room, request);
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
        return new RoomResponse(
                room.getId(),
                room.getFloor(),
                room.getNumber(),
                room.getBuilding().getId(),
                room.getCreatedAt(),
                room.getUpdatedAt()
        );
    }
}
