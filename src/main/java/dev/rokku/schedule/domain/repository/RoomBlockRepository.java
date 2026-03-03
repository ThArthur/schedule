package dev.rokku.schedule.domain.repository;

import dev.rokku.schedule.domain.model.room_block.RoomBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomBlockRepository extends JpaRepository<RoomBlock, Long> {

}
