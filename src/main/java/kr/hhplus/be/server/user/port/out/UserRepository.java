package kr.hhplus.be.server.user.port.out;

import kr.hhplus.be.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository  {
    Optional<User> findUserById(Long userId);
}
