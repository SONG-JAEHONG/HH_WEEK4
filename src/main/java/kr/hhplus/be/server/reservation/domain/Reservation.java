package kr.hhplus.be.server.reservation.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.base.BaseTimeEntity;
import kr.hhplus.be.server.concert.domain.ConcertDate;
import kr.hhplus.be.server.concert.domain.Seat;
import kr.hhplus.be.server.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concertDateId")
    private ConcertDate concertDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seatId")
    private Seat seat;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;


    public static Reservation holding(User user,ConcertDate concertDate, Seat seat) {
        Reservation reservation = new Reservation();
        reservation.user = user;
        reservation.concertDate = concertDate;
        reservation.seat = seat;
        reservation.status = ReservationStatus.HOLDING;

        return reservation;
    }

    public void reserve() {
        if (this.status != ReservationStatus.HOLDING) {
            throw new IllegalStateException("예약 상태가 HOLDING 상태여야 합니다.");
        }
        this.status = ReservationStatus.RESERVED;
    }


}
