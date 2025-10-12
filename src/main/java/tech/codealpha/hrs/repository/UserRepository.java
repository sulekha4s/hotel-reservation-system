package tech.codealpha.hrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.codealpha.hrs.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

