package kr.hhplus.be.server.concert.adapter;

import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertDate;
import kr.hhplus.be.server.concert.domain.Seat;
import kr.hhplus.be.server.concert.domain.SeatStatus;
import kr.hhplus.be.server.concert.infra.persistence.ConcertDateJpaRepository;
import kr.hhplus.be.server.concert.infra.persistence.ConcertJpaRepository;
import kr.hhplus.be.server.concert.infra.persistence.SeatJpaRepository;
import kr.hhplus.be.server.concert.port.out.ConcertRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ConcertRepositoryAdapterIntegrationTest {


    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertDateJpaRepository concertDateJpaRepository;

    @Autowired
    private SeatJpaRepository seatJpaRepository;

    @Test
    @DisplayName("통합 테스트 - Concert 전체 조회")
    void findAllConcerts() {
        // given
        concertJpaRepository.save(new Concert("Test Concert"));

        // when
        List<Concert> result = concertRepository.findAllConcerts();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Concert");
    }

    @Test
    @DisplayName("통합 테스트 - ConcertDate 조회")
    void findConcertDatesByConcertId() {
        // given
        Concert concert = concertJpaRepository.save(new Concert("Test"));
        ConcertDate date1 = new ConcertDate(concert, LocalDate.of(2025, 8, 1));
        ConcertDate date2 = new ConcertDate(concert, LocalDate.of(2025, 8, 2));
        concertDateJpaRepository.saveAll(List.of(date1, date2));

        // when
        List<ConcertDate> result = concertRepository.findConcertDatesByConcertId(concert.getId());

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("통합 테스트 - 좌석 조회")
    void findAvailableSeatsByConcertDateId() {
        // given
        Concert concert = concertJpaRepository.save(new Concert("Seat Concert"));
        ConcertDate date = concertDateJpaRepository.save(new ConcertDate(concert, LocalDate.of(2025, 8, 3)));

        Seat s1 = new Seat(date, "A1", SeatStatus.AVAILABLE);
        Seat s2 = new Seat(date, "A2", SeatStatus.RESERVED);
        Seat s3 = new Seat(date, "A3", SeatStatus.AVAILABLE);
        seatJpaRepository.saveAll(List.of(s1, s2, s3));

        // when
        List<Seat> result = concertRepository.findAvailableSeatsByConcertDateId(date.getId(), SeatStatus.AVAILABLE);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("seatNumber").containsExactlyInAnyOrder("A1", "A3");
    }

    @Test
    @DisplayName("통합 테스트 - ConcertDate 단건 조회")
    void findConcertDateById() {
        // given
        Concert concert = concertJpaRepository.save(new Concert("One Date"));
        ConcertDate date = concertDateJpaRepository.save(new ConcertDate(concert, LocalDate.of(2025, 8, 5)));

        // when
        Optional<ConcertDate> result = concertRepository.findConcertDateById(date.getId());

        // then
        assertThat(result).isPresent().contains(date);
    }
}
