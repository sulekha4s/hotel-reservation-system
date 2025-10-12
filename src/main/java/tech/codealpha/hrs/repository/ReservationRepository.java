package tech.codealpha.hrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.codealpha.hrs.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}

