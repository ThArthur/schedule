package dev.rokku.schedule.domain.repository;

import dev.rokku.schedule.domain.model.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByBuildingId(Long buildingId);
}
