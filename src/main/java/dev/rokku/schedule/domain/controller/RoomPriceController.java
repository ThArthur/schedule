package dev.rokku.schedule.domain.controller;

import dev.rokku.schedule.domain.dto.room_price.request.RoomPriceRequest;
import dev.rokku.schedule.domain.dto.room_price.response.RoomPriceResponse;
import dev.rokku.schedule.domain.service.RoomPriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-prices")
@RequiredArgsConstructor
public class RoomPriceController {

    private final RoomPriceService roomPriceService;

    @GetMapping
    public List<RoomPriceResponse> findAll() {
        return roomPriceService.findAll();
    }

    @GetMapping("/{id}")
    public RoomPriceResponse findById(@PathVariable Long id) {
        return roomPriceService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public RoomPriceResponse create(@RequestBody @Valid RoomPriceRequest request) {
        return roomPriceService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoomPriceResponse update(@PathVariable Long id, @RequestBody @Valid RoomPriceRequest request) {
        return roomPriceService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        roomPriceService.delete(id);
    }
}
