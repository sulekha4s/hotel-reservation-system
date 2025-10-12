package tech.codealpha.hrs.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.codealpha.hrs.model.Room;
import tech.codealpha.hrs.model.RoomType;
import tech.codealpha.hrs.service.ReservationService;
import tech.codealpha.hrs.service.RoomService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
public class RoomController {
    private final RoomService roomService;
    private final ReservationService reservationService;

    public RoomController(RoomService roomService, ReservationService reservationService) {
        this.roomService = roomService;
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public String welcomePage() {
        return "welcome";
    }

    @PostMapping("/available-rooms")
    public String availableRooms(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                                 Model model) {
        List<Room> availableRooms = roomService.findAvailableRooms(checkInDate, checkOutDate, reservationService.getAllReservations());
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (days < 1) days = 1; // Minimum 1 day stay
        model.addAttribute("rooms", availableRooms);
        model.addAttribute("roomTypes", RoomType.values());
        model.addAttribute("checkInDate", checkInDate);
        model.addAttribute("checkOutDate", checkOutDate);
        model.addAttribute("days", days);
        return "available-rooms";
    }
}
