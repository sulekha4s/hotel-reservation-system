package tech.codealpha.hrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.codealpha.hrs.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}

