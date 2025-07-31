package kr.hhplus.be.server.concert.port.out;

import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertDate;
import kr.hhplus.be.server.concert.domain.Seat;
import kr.hhplus.be.server.concert.domain.SeatStatus;


import java.util.List;
import java.util.Optional;

public interface ConcertRepository {

    List<Concert> findAllConcerts();
    List<ConcertDate> findConcertDatesByConcertId(Long concertId);
    List<Seat> findAvailableSeatsByConcertDateId(Long concertDateId, SeatStatus status);
    Optional<ConcertDate> findConcertDateById(Long concertDateId);


}