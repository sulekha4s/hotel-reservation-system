package tech.codealpha.hrs.service;

import org.springframework.stereotype.Service;
import tech.codealpha.hrs.model.Room;
import tech.codealpha.hrs.model.RoomType;
import tech.codealpha.hrs.repository.RoomRepository;
import tech.codealpha.hrs.model.Reservation;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    public List<Room> searchRooms(RoomType type, Boolean available, Double maxPrice) {
        return roomRepository.findAll().stream()
            .filter(room -> type == null || room.getType() == type)
            .filter(room -> available == null || room.getAvailable().equals(available))
            .filter(room -> maxPrice == null || room.getPrice() <= maxPrice)
            .toList();
    }

    public List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut, List<Reservation> reservations) {
        return roomRepository.findAll().stream()
            .filter(room -> reservations.stream().noneMatch(res ->
                res.getRoom().getId().equals(room.getId()) &&
                !(res.getCheckOutDate().isBefore(checkIn) || res.getCheckInDate().isAfter(checkOut))
            ))
            .toList();
    }
}
