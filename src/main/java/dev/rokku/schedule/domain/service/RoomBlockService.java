package dev.rokku.schedule.domain.service;

import dev.rokku.schedule.domain.dto.room_block.request.RoomBlockRequest;
import dev.rokku.schedule.domain.dto.room_block.response.RoomBlockResponse;
import dev.rokku.schedule.domain.exception.ApiException;
import dev.rokku.schedule.domain.model.room.Room;
import dev.rokku.schedule.domain.model.room_block.RoomBlock;
import dev.rokku.schedule.domain.repository.RoomBlockRepository;
import dev.rokku.schedule.domain.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomBlockService {

    private final RoomBlockRepository roomBlockRepository;
    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public List<RoomBlockResponse> findAll() {
        return roomBlockRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomBlockResponse findById(Long id) {
        return roomBlockRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Room block not found"));
    }

    @Transactional
    public RoomBlockResponse create(RoomBlockRequest request) {
        RoomBlock roomBlock = new RoomBlock();
        roomBlock.setCreatedAt(OffsetDateTime.now());
        updateEntity(roomBlock, request);
        return toResponse(roomBlockRepository.save(roomBlock));
    }

    @Transactional
    public RoomBlockResponse update(Long id, RoomBlockRequest request) {
        RoomBlock roomBlock = roomBlockRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Room block not found"));
        updateEntity(roomBlock, request);
        return toResponse(roomBlockRepository.save(roomBlock));
    }

    @Transactional
    public void delete(Long id) {
        if (!roomBlockRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Room block not found");
        }
        roomBlockRepository.deleteById(id);
    }

    private void updateEntity(RoomBlock roomBlock, RoomBlockRequest request) {
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Room not found"));
        roomBlock.setRoom(room);
        roomBlock.setStartAt(request.startAt());
        roomBlock.setEndAt(request.endAt());
        roomBlock.setReason(request.reason());
        roomBlock.setNotes(request.notes());
    }

    private RoomBlockResponse toResponse(RoomBlock roomBlock) {
        return new RoomBlockResponse(
                roomBlock.getId(),
                roomBlock.getRoom().getId(),
                roomBlock.getStartAt(),
                roomBlock.getEndAt(),
                roomBlock.getReason(),
                roomBlock.getNotes(),
                roomBlock.getCreatedAt()
        );
    }
}
