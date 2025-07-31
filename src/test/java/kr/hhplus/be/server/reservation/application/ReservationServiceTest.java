package kr.hhplus.be.server.reservation.application;

import kr.hhplus.be.server.concert.application.SeatHoldingScheduler;
import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertDate;
import kr.hhplus.be.server.concert.domain.Seat;
import kr.hhplus.be.server.concert.domain.SeatStatus;
import kr.hhplus.be.server.concert.port.out.ConcertRepository;
import kr.hhplus.be.server.concert.port.out.SeatRepository;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.domain.ReservationStatus;
import kr.hhplus.be.server.reservation.infra.web.dto.ReservationRequest;
import kr.hhplus.be.server.reservation.infra.web.dto.ReservationResponse;
import kr.hhplus.be.server.reservation.port.in.ReservationUseCase;
import kr.hhplus.be.server.reservation.port.out.ReservationRepository;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.port.out.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    Seat expiredSeat;

    @InjectMocks
    SeatHoldingScheduler scheduler;


    @InjectMocks
    private ReservationService reservationService;

    @Test
    void 좌석이_예약가능하면_HOLDING으로_변경_예약이_생성() {
        // given

        Long concertDateId = 10L;
        Long seatId = 100L;
        Long concertId = 1L;
        ReservationRequest request = new ReservationRequest(1L, concertDateId, seatId);

        Concert concert = new Concert(concertId,"YeYe");
        ConcertDate concertDate = new ConcertDate(concertDateId,concert, LocalDate.of(2025,7,24) );
        Seat seat = new Seat(seatId, concertDate, 1, SeatStatus.AVAILABLE, LocalDate.now().atStartOfDay());
        User user = new User(1L, 10000L); // 필요시 생성자 또는 빌더 추가

        ConcertDate dummyconcertDate = mock(kr.hhplus.be.server.concert.domain.ConcertDate.class);

        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        when(seatRepository.findSeatById(seatId)).thenReturn(Optional.of(seat));
        when(concertRepository.findConcertDateById(concertDateId)).thenReturn(Optional.of(dummyconcertDate));

        // when
        ReservationResponse response = reservationService.reserve(request, 1L);

        // then
        assertThat(response.getStatus()).isEqualTo(ReservationStatus.HOLDING.name());
        assertThat(response.getSeatId()).isEqualTo(seatId);
        verify(seatRepository).save(seat);
        verify(reservationRepository).save(any(Reservation.class));
    }



    @Test
    void 만료된_좌석_해제_및_저장() {
        // given
        List<Seat> expiredSeats = List.of(expiredSeat);
        when(seatRepository.findByStatusAndBeforeExpire(eq(SeatStatus.HOLDING), any(LocalDateTime.class)))
                .thenReturn(expiredSeats);

        // when
        scheduler.releaseExpiredSeats();

        // then
        verify(expiredSeat).release();
        verify(seatRepository, times(1)).save(expiredSeat);
    }
}
