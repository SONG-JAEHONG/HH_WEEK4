package kr.hhplus.be.server.seat.adapter;


import kr.hhplus.be.server.concert.domain.Seat;
import kr.hhplus.be.server.concert.domain.SeatStatus;
import kr.hhplus.be.server.concert.infra.persistence.SeatJpaRepository;
import kr.hhplus.be.server.concert.infra.persistence.SeatRepositoryAdpter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeatRepositoryAdapterTest {

    @Mock
    private SeatJpaRepository seatJpaRepository;

    private SeatRepositoryAdpter seatRepositoryAdpter;

    @BeforeEach
    void setUp(){
        seatRepositoryAdpter = new SeatRepositoryAdpter(seatJpaRepository);
    }


    @Test
    void save는_seat을_저장한다(){

        Seat seat = new Seat();

        seatRepositoryAdpter.save(seat);

        verify(seatJpaRepository).save(seat);

    }

    @Test
    void findByStatusAndBeforeExpire는_호출결과를_반환한다() {

        SeatStatus status = SeatStatus.HOLDING;
        LocalDateTime now = LocalDateTime.now();
        List<Seat> expected = Collections.singletonList(mock(Seat.class));
        when(seatJpaRepository.findByStatusAndExpireTimeBefore(status, now)).thenReturn(expected);


        List<Seat> result = seatRepositoryAdpter.findByStatusAndBeforeExpire(status, now);


        assertThat(result).isEqualTo(expected);
        verify(seatJpaRepository).findByStatusAndExpireTimeBefore(status, now);
    }

}
