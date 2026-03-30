package dev.rokku.schedule.domain.controller;

import dev.rokku.schedule.domain.dto.room_block.request.RoomBlockRequest;
import dev.rokku.schedule.domain.dto.room_block.response.RoomBlockResponse;
import dev.rokku.schedule.domain.service.RoomBlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-blocks")
@RequiredArgsConstructor
public class RoomBlockController {

    private final RoomBlockService roomBlockService;

    @GetMapping
    public List<RoomBlockResponse> findAll() {
        return roomBlockService.findAll();
    }

    @GetMapping("/{id}")
    public RoomBlockResponse findById(@PathVariable Long id) {
        return roomBlockService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public RoomBlockResponse create(@RequestBody @Valid RoomBlockRequest request) {
        return roomBlockService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoomBlockResponse update(@PathVariable Long id, @RequestBody @Valid RoomBlockRequest request) {
        return roomBlockService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        roomBlockService.delete(id);
    }
}
