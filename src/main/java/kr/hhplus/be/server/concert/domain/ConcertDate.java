package kr.hhplus.be.server.concert.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.base.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class ConcertDate extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate concertDate;


    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;


    //concertIntegraion 테스트용

    public ConcertDate(Long id, Concert concert, LocalDate concertDate) {
        this.id = id;
        this.concert = concert;
        this.concertDate = concertDate;
    }

    public ConcertDate(Concert concert, LocalDate concertDate) {
        this.concert = concert;
        this.concertDate = concertDate;
    }
}
