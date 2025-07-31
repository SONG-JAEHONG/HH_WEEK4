package kr.hhplus.be.server.concert.infra.persistence;

import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertDate;
import kr.hhplus.be.server.concert.domain.Seat;
import kr.hhplus.be.server.concert.domain.SeatStatus;
import kr.hhplus.be.server.concert.port.out.ConcertRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@AllArgsConstructor
public class ConcertRepositoryAdapter implements ConcertRepository {

    private ConcertJpaRepository concertJpaRepository;
    private ConcertDateJpaRepository concertDateJpaRepository;
    private SeatJpaRepository seatJpaRepository;


    @Override
    public List<Concert> findAllConcerts() {
        return concertJpaRepository.findAll();
    }

    @Override
    public List<ConcertDate> findConcertDatesByConcertId(Long concertId) {
        return concertDateJpaRepository.findConcertDatesByconcertId(concertId);
    }

    @Override
    public List<Seat> findAvailableSeatsByConcertDateId(Long concertDateId, SeatStatus status) {
        return seatJpaRepository.findAvailableSeatsByConcertDateId(concertDateId, status);
    }

    @Override
    public Optional<ConcertDate> findConcertDateById(Long concertDateId) {
        return concertDateJpaRepository.findById(concertDateId);
    }


}
