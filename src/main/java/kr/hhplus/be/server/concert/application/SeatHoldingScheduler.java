package kr.hhplus.be.server.concert.application;

import kr.hhplus.be.server.concert.domain.Seat;
import kr.hhplus.be.server.concert.domain.SeatStatus;
import kr.hhplus.be.server.concert.port.out.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SeatHoldingScheduler {

    @Autowired
    private SeatRepository seatRepository;

    @Scheduled(fixedRate =  60000)
    public void releaseExpiredSeats(){

        List<Seat> expiredSeats = seatRepository.findByStatusAndBeforeExpire(SeatStatus.HOLDING, LocalDateTime.now());

        for (Seat seat : expiredSeats) {
            seat.release();
        }

        if(!expiredSeats.isEmpty()){
            for(Seat seat : expiredSeats){
                seatRepository.save(seat);
            }
        }

    }


}
