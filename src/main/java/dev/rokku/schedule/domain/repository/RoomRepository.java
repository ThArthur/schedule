package dev.rokku.schedule.domain.repository;

import dev.rokku.schedule.domain.model.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
