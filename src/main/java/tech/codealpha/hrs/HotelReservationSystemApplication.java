package tech.codealpha.hrs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tech.codealpha.hrs.model.Room;
import tech.codealpha.hrs.model.RoomType;
import tech.codealpha.hrs.repository.RoomRepository;

@SpringBootApplication
public class HotelReservationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelReservationSystemApplication.class, args);
	}

	@Bean
	CommandLineRunner initRooms(RoomRepository roomRepository) {
		return args -> {
			if (roomRepository.count() == 0) {
				roomRepository.save(new Room(null, "101", RoomType.STANDARD, 100.0, true));
				roomRepository.save(new Room(null, "102", RoomType.DELUXE, 150.0, true));
				roomRepository.save(new Room(null, "201", RoomType.SUITE, 200.0, true));
				roomRepository.save(new Room(null, "202", RoomType.STANDARD, 110.0, true));
				roomRepository.save(new Room(null, "203", RoomType.DELUXE, 160.0, true));
			}
		};
	}

}
