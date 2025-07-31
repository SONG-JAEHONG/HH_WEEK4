package kr.hhplus.be.server.concert.infra.web.controller;


import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertDate;
import kr.hhplus.be.server.concert.infra.web.dto.ConcertDateResponse;
import kr.hhplus.be.server.concert.infra.web.dto.ConcertResponse;
import kr.hhplus.be.server.concert.infra.web.dto.SeatResponse;
import kr.hhplus.be.server.concert.port.in.ConcertUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concerts")
public class ConcertController {

    private final ConcertUseCase concertUseCase;

    @GetMapping
    public List<ConcertResponse> getAllConcerts(){
        return concertUseCase.getAllConcerts();
    }

    @GetMapping("/{concertId}/dates")
    public List<ConcertDateResponse> getConcertDates(@PathVariable("concertId") Long concertId){
        return concertUseCase.getConcertDates(concertId);
    }

    @GetMapping("/{concertDateId}/seats")
    public List<SeatResponse> getSeats(@PathVariable("concertDateId") Long concertDateId){
        return concertUseCase.getSeats(concertDateId);
    }

}
