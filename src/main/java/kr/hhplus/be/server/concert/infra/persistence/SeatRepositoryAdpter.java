package kr.hhplus.be.server.concert.infra.persistence;


import kr.hhplus.be.server.concert.domain.Seat;
import kr.hhplus.be.server.concert.domain.SeatStatus;
import kr.hhplus.be.server.concert.port.out.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryAdpter implements  SeatRepository{

    private final SeatJpaRepository seatJpaRepository;

    @Override
    public Optional<Seat> findSeatById(Long id) {
        return seatJpaRepository.findById(id);
    }

    @Override
    public void save(Seat seat) {
        seatJpaRepository.save(seat);
    }

    @Override
    public List<Seat> findByStatusAndBeforeExpire(SeatStatus status, LocalDateTime time) {
        return seatJpaRepository.findByStatusAndExpireTimeBefore(status, time);
    }
}
