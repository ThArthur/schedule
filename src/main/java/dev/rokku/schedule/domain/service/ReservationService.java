package dev.rokku.schedule.domain.service;

import dev.rokku.schedule.domain.dto.reservation.request.ReservationRequest;
import dev.rokku.schedule.domain.dto.reservation.response.ReservationResponse;
import dev.rokku.schedule.domain.exception.ApiException;
import dev.rokku.schedule.domain.model.reservation.Reservation;
import dev.rokku.schedule.domain.model.room.Room;
import dev.rokku.schedule.domain.model.user.Users;
import dev.rokku.schedule.domain.repository.ReservationRepository;
import dev.rokku.schedule.domain.repository.RoomRepository;
import dev.rokku.schedule.domain.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationResponse findById(Long id) {
        return reservationRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Reservation not found"));
    }

    @Transactional
    public ReservationResponse create(ReservationRequest request) {
        Reservation reservation = new Reservation();
        updateEntity(reservation, request);
        return toResponse(reservationRepository.save(reservation));
    }

    @Transactional
    public ReservationResponse update(Long id, ReservationRequest request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Reservation not found"));
        updateEntity(reservation, request);
        return toResponse(reservationRepository.save(reservation));
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Reservation not found");
        }
        reservationRepository.deleteById(id);
    }

    private void updateEntity(Reservation reservation, ReservationRequest request) {
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Room not found"));
        Users user = usersRepository.findById(request.userId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        
        reservation.setRoom(room);
        reservation.setUser(user);
        reservation.setStartAt(request.startAt());
        reservation.setEndAt(request.endAt());
        reservation.setTotalPrice(request.totalPrice());
        reservation.setStatus(request.status());
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getRoom().getId(),
                reservation.getUser().getId(),
                reservation.getStartAt(),
                reservation.getEndAt(),
                reservation.getTotalPrice(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt()
        );
    }
}