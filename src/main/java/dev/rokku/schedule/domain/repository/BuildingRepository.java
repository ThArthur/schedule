package dev.rokku.schedule.domain.repository;

import dev.rokku.schedule.domain.model.building.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Long> {

}
