package tech.codealpha.hrs.service;

import org.springframework.stereotype.Service;
import tech.codealpha.hrs.model.Reservation;
import tech.codealpha.hrs.repository.ReservationRepository;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public Reservation getReservationByBookingId(String bookingId) {
        return reservationRepository.findAll().stream()
            .filter(r -> bookingId.equals(r.getBookingId()))
            .findFirst().orElse(null);
    }

    public void cancelReservationByBookingId(String bookingId) {
        Reservation reservation = getReservationByBookingId(bookingId);
        if (reservation != null) {
            reservationRepository.delete(reservation);
        }
    }
}
