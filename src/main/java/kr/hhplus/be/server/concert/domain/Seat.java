package kr.hhplus.be.server.concert.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.base.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Seat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int seatNumber;


    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_date_id")
    private ConcertDate concertDate;

    private LocalDateTime expireTime;

    // concertRepositoryAdapter 테스트용
    public Seat(ConcertDate concertDate, int seatNumber, SeatStatus seatStatus) {

        this.concertDate = concertDate;
        this.seatNumber = seatNumber;
        this.status = seatStatus;
    }

    // concertService 테스트용
    public Seat(long l, ConcertDate concertDate, int i, SeatStatus seatStatus, LocalDateTime localDateTime) {

        this.id = l;
        this.concertDate = concertDate;
        this.seatNumber = i;
        this.status = seatStatus;
        this.expireTime = localDateTime;
    }

    // concertIntegration 테스트용
    public Seat(ConcertDate concertDate, int i, SeatStatus seatStatus, Object o) {

        this.concertDate = concertDate;
        this.seatNumber = i;
        this.status = seatStatus;
        // expireTime이 Object로 되어있는데, 필요하다면 적절한 타입으로 변환하거나 할당하세요.
        if (o instanceof LocalDateTime) {
            this.expireTime = (LocalDateTime) o;
        } else {
            this.expireTime = null; // 필요에 따라 기본값 지정
        }
    }



    public void hold() {
        if (!isAvailable()) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }
        this.status = SeatStatus.HOLDING;
        this.expireTime = LocalDateTime.now().plusMinutes(5);
    }

    public boolean isAvailable() {
        return this.status == SeatStatus.AVAILABLE;
    }

    public void reserve() {
        if (this.status != SeatStatus.HOLDING) {
            throw new IllegalStateException("결제 대기 상태가 아닙니다.");
        }
        this.status = SeatStatus.RESERVED;
    }


    public void release() {

         if(this.status != SeatStatus.HOLDING){
             throw new IllegalStateException("임시 배정 좌석이 아닙니다.");
         }
         this.status = SeatStatus.AVAILABLE;
         this.expireTime = null;


    }

}
