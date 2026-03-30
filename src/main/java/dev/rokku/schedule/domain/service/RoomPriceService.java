package dev.rokku.schedule.domain.service;

import dev.rokku.schedule.domain.dto.room_price.request.RoomPriceRequest;
import dev.rokku.schedule.domain.dto.room_price.response.RoomPriceResponse;
import dev.rokku.schedule.domain.exception.ApiException;
import dev.rokku.schedule.domain.model.room.Room;
import dev.rokku.schedule.domain.model.room_price.RoomPrice;
import dev.rokku.schedule.domain.repository.RoomPriceRepository;
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
public class RoomPriceService {

    private final RoomPriceRepository roomPriceRepository;
    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public List<RoomPriceResponse> findAll() {
        return roomPriceRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomPriceResponse findById(Long id) {
        return roomPriceRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Room price not found"));
    }

    @Transactional
    public RoomPriceResponse create(RoomPriceRequest request) {
        RoomPrice roomPrice = new RoomPrice();
        roomPrice.setCreatedAt(OffsetDateTime.now());
        updateEntity(roomPrice, request);
        return toResponse(roomPriceRepository.save(roomPrice));
    }

    @Transactional
    public RoomPriceResponse update(Long id, RoomPriceRequest request) {
        RoomPrice roomPrice = roomPriceRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Room price not found"));
        updateEntity(roomPrice, request);
        return toResponse(roomPriceRepository.save(roomPrice));
    }

    @Transactional
    public void delete(Long id) {
        if (!roomPriceRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Room price not found");
        }
        roomPriceRepository.deleteById(id);
    }

    private void updateEntity(RoomPrice roomPrice, RoomPriceRequest request) {
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Room not found"));
        roomPrice.setRoom(room);
        roomPrice.setValue(request.value());
        roomPrice.setPeriodHours(request.periodHours());
        roomPrice.setActive(request.active());
    }

    private RoomPriceResponse toResponse(RoomPrice roomPrice) {
        return new RoomPriceResponse(
                roomPrice.getId(),
                roomPrice.getRoom().getId(),
                roomPrice.getValue(),
                roomPrice.getPeriodHours(),
                roomPrice.isActive(),
                roomPrice.getCreatedAt()
        );
    }
}
