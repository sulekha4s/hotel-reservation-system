package tech.codealpha.hrs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tech.codealpha.hrs.model.Reservation;
import tech.codealpha.hrs.model.Room;
import tech.codealpha.hrs.model.User;
import tech.codealpha.hrs.service.ReservationService;
import tech.codealpha.hrs.service.RoomService;
import tech.codealpha.hrs.service.UserService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Controller
public class ReservationController {
    private final ReservationService reservationService;
    private final RoomService roomService;
    private final UserService userService;

    public ReservationController(ReservationService reservationService, RoomService roomService, UserService userService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
        this.userService = userService;
    }

    @GetMapping("/reserve/{roomId}")
    public String showReservationForm(@PathVariable Long roomId,
                                      @RequestParam(required = false) String checkInDate,
                                      @RequestParam(required = false) String checkOutDate,
                                      Model model) {
        Room room = roomService.getRoomById(roomId);
        long days = ChronoUnit.DAYS.between(LocalDate.parse(checkInDate), LocalDate.parse(checkOutDate));
        if (days < 1) days = 1; // Minimum 1 day stay

        room.setPrice(days*room.getPrice()); // Update price based on stay duration

        model.addAttribute("room", room);
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("user", new User());
        model.addAttribute("checkInDate", checkInDate);
        model.addAttribute("checkOutDate", checkOutDate);
        return "reservation-form";
    }

    @PostMapping("/reserve")
    public String makeReservation(@ModelAttribute Reservation reservation,
                                  @RequestParam Long roomId,
                                  @RequestParam String checkInDate,
                                    @RequestParam String checkOutDate,
                                  RedirectAttributes redirectAttributes) {
        // Set the Room on the Reservation
        Room room = roomService.getRoomById(roomId);
        reservation.setRoom(room);
        // Save user if not already persisted
        if (reservation.getUser() != null) {
            userService.saveUser(reservation.getUser());
        }
        // Generate unique booking ID
        reservation.setBookingId(java.util.UUID.randomUUID().toString().substring(0,8));
        reservation.setCheckInDate(LocalDate.parse(checkInDate));
        reservation.setCheckOutDate(LocalDate.parse(checkOutDate));
        reservation.setStatus("pending");

        Reservation savedReservation = reservationService.saveReservation(reservation);
        // Simulate payment: redirect to payment page
        redirectAttributes.addFlashAttribute("reservationId", savedReservation.getId());
        return "redirect:/payment?reservationId=" + savedReservation.getId();
    }

    @GetMapping("/payment")
    public String showPayment(@RequestParam Long reservationId, Model model) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        model.addAttribute("reservation", reservation);
        return "payment";
    }

    @PostMapping("/payment/confirm")
    public String confirmPayment(@RequestParam Long reservationId, RedirectAttributes redirectAttributes) {
        // Simulate payment success
        Reservation reservation = reservationService.getReservationById(reservationId);
        reservation.setStatus("confirmed");
        reservationService.saveReservation(reservation);
        redirectAttributes.addFlashAttribute("bookingId", reservation.getBookingId());
        return "redirect:/booking-details?bookingId=" + reservation.getBookingId();
    }

    @GetMapping("/booking-details")
    public String bookingDetails(@RequestParam String bookingId, Model model) {
        Reservation reservation = reservationService.getReservationByBookingId(bookingId);
        model.addAttribute("reservation", reservation);
        model.addAttribute("checkInDate", reservation.getCheckInDate());
        model.addAttribute("checkOutDate", reservation.getCheckOutDate());
        return "booking-details";
    }

    @PostMapping("/cancel-booking")
    public String cancelBooking(@RequestParam String bookingId, RedirectAttributes redirectAttributes) {
        reservationService.cancelReservationByBookingId(bookingId);
        redirectAttributes.addFlashAttribute("message", "Reservation cancelled successfully.");
        return "redirect:/reservations";
    }

    @GetMapping("/reservations")
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "reservations";
    }
}
