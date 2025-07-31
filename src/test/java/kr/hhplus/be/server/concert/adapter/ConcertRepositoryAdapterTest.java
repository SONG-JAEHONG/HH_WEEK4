package kr.hhplus.be.server.concert.adapter;

import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertDate;
import kr.hhplus.be.server.concert.domain.Seat;
import kr.hhplus.be.server.concert.domain.SeatStatus;
import kr.hhplus.be.server.concert.infra.persistence.ConcertDateJpaRepository;
import kr.hhplus.be.server.concert.infra.persistence.ConcertJpaRepository;
import kr.hhplus.be.server.concert.infra.persistence.ConcertRepositoryAdapter;
import kr.hhplus.be.server.concert.infra.persistence.SeatJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConcertRepositoryAdapterTest {

    @Mock
    private ConcertJpaRepository concertJpaRepository;

    @Mock
    private ConcertDateJpaRepository concertDateJpaRepository;

    @Mock
    private SeatJpaRepository seatJpaRepository;

    private ConcertRepositoryAdapter concertRepositoryAdapter;

    @BeforeEach
    void setUp() {
        concertRepositoryAdapter = new ConcertRepositoryAdapter(concertJpaRepository,
                concertDateJpaRepository,
                seatJpaRepository
        );
    }

    @Test
    void findAllConcerts는_JPA에서_데이터를_가져온다() {
        // given
        List<Concert> concerts = List.of(new Concert("Test Concert"));
        when(concertJpaRepository.findAll()).thenReturn(concerts);

        // when
        List<Concert> result = concertRepositoryAdapter.findAllConcerts();

        // then
        assertThat(result).isEqualTo(concerts);
        verify(concertJpaRepository).findAll();
    }

    @Test
    void findConcertDateById는_Optional을_정상_반환한다() {
        // given
        Concert concert = new Concert("콘서트");
        ConcertDate concertDate = new ConcertDate(concert, LocalDate.of(2025, 8, 1));
        when(concertDateJpaRepository.findById(1L)).thenReturn(Optional.of(concertDate));

        // when
        Optional<ConcertDate> result = concertRepositoryAdapter.findConcertDateById(1L);

        // then
        assertThat(result).isPresent().contains(concertDate);
        verify(concertDateJpaRepository).findById(1L);
    }

    @Test
    void findConcertDatesByConcertId는_관련된_날짜들을_반환한다() {
        // given
        Concert concert = new Concert("BTS 콘서트");
        List<ConcertDate> concertDates = List.of(
                new ConcertDate(concert, LocalDate.of(2025, 8, 1)),
                new ConcertDate(concert, LocalDate.of(2025, 8, 2))
        );
        when(concertDateJpaRepository.findConcertDatesByconcertId(100L)).thenReturn(concertDates);

        // when
        List<ConcertDate> result = concertRepositoryAdapter.findConcertDatesByConcertId(100L);

        // then
        assertThat(result).hasSize(2).isEqualTo(concertDates);
        verify(concertDateJpaRepository).findConcertDatesByconcertId(100L);
    }

    @Test
    void findAvailableSeatsByConcertDateId는_Available_상태의_좌석만_반환한다() {
        // given
        Concert concert = new Concert("콘서트");
        ConcertDate concertDate = new ConcertDate(concert, LocalDate.of(2025, 8, 3));
        List<Seat> seats = List.of(
                new Seat(concertDate, "A1", SeatStatus.AVAILABLE),
                new Seat(concertDate, "A2", SeatStatus.AVAILABLE)
        );
        when(seatJpaRepository.findAvailableSeatsByConcertDateId(10L, SeatStatus.AVAILABLE)).thenReturn(seats);

        // when
        List<Seat> result = concertRepositoryAdapter.findAvailableSeatsByConcertDateId(10L, SeatStatus.AVAILABLE);

        // then
        assertThat(result).hasSize(2).isEqualTo(seats);
        verify(seatJpaRepository).findAvailableSeatsByConcertDateId(10L, SeatStatus.AVAILABLE);
    }

}
