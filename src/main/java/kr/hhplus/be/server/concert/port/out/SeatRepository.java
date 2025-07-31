package kr.hhplus.be.server.concert.port.out;

import kr.hhplus.be.server.concert.domain.Seat;
import kr.hhplus.be.server.concert.domain.SeatStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeatRepository {

    Optional<Seat> findSeatById(Long id);
    void save(Seat seat);
    List<Seat> findByStatusAndBeforeExpire(SeatStatus status, LocalDateTime time);

}
