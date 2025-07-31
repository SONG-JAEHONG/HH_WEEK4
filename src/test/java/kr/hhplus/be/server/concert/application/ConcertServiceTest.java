package kr.hhplus.be.server.concert.application;

import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertDate;
import kr.hhplus.be.server.concert.domain.Seat;
import kr.hhplus.be.server.concert.domain.SeatStatus;
import kr.hhplus.be.server.concert.infra.web.dto.ConcertDateResponse;
import kr.hhplus.be.server.concert.infra.web.dto.ConcertResponse;
import kr.hhplus.be.server.concert.infra.web.dto.SeatResponse;
import kr.hhplus.be.server.concert.port.out.ConcertRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;
    private ConcertService concertService;

    @BeforeEach
    void setUp(){

        concertService = new ConcertService(concertRepository);

    }

    @Test
     void 콘서트_전체_목록_조회(){
        List<Concert> DummyConcert = List.of(
                new Concert(1L, "Jamie xx"),
                new Concert(2L, "YeYe")
        );
        given(concertRepository.findAllConcerts()).willReturn(DummyConcert);

        List<ConcertResponse> result = concertService.getAllConcerts();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Jamie xx");
        assertThat(result.get(1).getTitle()).isEqualTo("YeYe");

    }
    @Test
    void 콘서트_날짜_목록_조회(){

        Long concertId = 1L;
        Concert concert = new Concert(concertId, "YeYe");


        List<ConcertDate> DummyConcertDate = List.of(
                new ConcertDate(1L,concert, LocalDate.of(2025,7,23)),
                new ConcertDate(2L,concert, LocalDate.of(2025,7,24)),
                new ConcertDate(3L,concert, LocalDate.of(2025,7,25))
        );
        given(concertRepository.findConcertDatesByConcertId(concertId)).willReturn(DummyConcertDate);

        List<ConcertDateResponse> result = concertService.getConcertDates(concertId);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getConcertDate()).isEqualTo(LocalDate.of(2025,7,23));
        assertThat(result.get(1).getConcertDate()).isEqualTo(LocalDate.of(2025,7,24));
        assertThat(result.get(2).getConcertDate()).isEqualTo(LocalDate.of(2025,7,25));

    }




    @Test
    void 전체_좌석_목록_조회(){
        Concert concert = new Concert(1L, "YeYe");
        ConcertDate concertDate = new ConcertDate(1L,concert,LocalDate.of(2025,7,23));

        List<Seat> DummySeat = List.of(
            new Seat(1L,concertDate,10, SeatStatus.AVAILABLE, LocalDate.now().atStartOfDay()),
            new Seat(2L,concertDate,11, SeatStatus.AVAILABLE, LocalDate.now().atStartOfDay())
        );

        given(concertRepository.findAvailableSeatsByConcertDateId(1L, SeatStatus.AVAILABLE)).willReturn(DummySeat);

        List<SeatResponse> result = concertService.getSeats(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSeatNumber()).isEqualTo(10);
        assertThat(result.get(1).getSeatNumber()).isEqualTo(11);

    }


    @Test
    void 콘서트_리스트가_null이면_NPE_발생() {
        // given
        given(concertRepository.findAllConcerts()).willReturn(null);

        // when / then
        assertThatThrownBy(() -> {
            List<ConcertResponse> result = concertService.getAllConcerts();
            result.size(); // 👈 여기서 NPE 발생
        }).isInstanceOf(NullPointerException.class);
    }


    @Test
    void 존재하지_않는_콘서트날짜의_빈_좌석_리스트_반환() {

       Long fakeConcertDatId = 999L;

        // given
        given(concertRepository.findAvailableSeatsByConcertDateId(fakeConcertDatId, SeatStatus.AVAILABLE)).willReturn(List.of());

        List<SeatResponse> result = concertService.getSeats(fakeConcertDatId);

        assertThat(result).isEmpty();
    }



}
