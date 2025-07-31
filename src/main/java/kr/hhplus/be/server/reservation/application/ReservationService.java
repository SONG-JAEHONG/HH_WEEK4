package kr.hhplus.be.server.reservation.application;

import kr.hhplus.be.server.concert.domain.ConcertDate;
import kr.hhplus.be.server.concert.domain.Seat;
import kr.hhplus.be.server.concert.port.out.ConcertRepository;
import kr.hhplus.be.server.concert.port.out.SeatRepository;
import kr.hhplus.be.server.payment.port.out.PaymentRepository;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.infra.web.dto.ReservationRequest;
import kr.hhplus.be.server.reservation.infra.web.dto.ReservationResponse;
import kr.hhplus.be.server.reservation.port.in.ReservationUseCase;
import kr.hhplus.be.server.reservation.port.out.ReservationRepository;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService implements ReservationUseCase {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;


    @Override
    public ReservationResponse reserve(ReservationRequest reservationRequest, Long userId) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Seat seat = seatRepository.findSeatById(reservationRequest.seatId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좌석입니다."));

        if(!seat.isAvailable()){ //좌석 상태 Available 인지 확인
            throw new IllegalStateException("이미 예약 중인 좌석입니다.");
        }
        seat.hold(); //좌석 상태 Available -> Holding
        seatRepository.save(seat);

        ConcertDate concertDate = concertRepository.findConcertDateById(reservationRequest.concertDateId()) //콘서트 날짜 ID 로 conertDate 조회
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 날짜입니다."));

        Reservation reservation = Reservation.holding(user,concertDate,seat);
        reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(), reservationRequest.seatId(), reservation.getStatus().name()) ;




    }
}
