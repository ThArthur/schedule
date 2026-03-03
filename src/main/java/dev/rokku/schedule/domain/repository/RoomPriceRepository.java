package dev.rokku.schedule.domain.repository;

import dev.rokku.schedule.domain.model.room_price.RoomPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomPriceRepository extends JpaRepository<RoomPrice, Long> {

}
