package kr.hhplus.be.server.concert.infra.web.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ConcertDateResponse {

    private Long id;
    private LocalDate concertDate;

}
