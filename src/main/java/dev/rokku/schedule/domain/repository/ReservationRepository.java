package dev.rokku.schedule.domain.repository;

import dev.rokku.schedule.domain.model.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
