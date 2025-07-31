package kr.hhplus.be.server.concert.infra.persistence;

import kr.hhplus.be.server.concert.domain.Seat;
import kr.hhplus.be.server.concert.domain.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeatJpaRepository extends JpaRepository<Seat, Long> {

    List<Seat> findAvailableSeatsByConcertDateId(Long concertDateId, SeatStatus status);
    List<Seat> findByStatusAndExpireTimeBefore(SeatStatus status, LocalDateTime time);
}
