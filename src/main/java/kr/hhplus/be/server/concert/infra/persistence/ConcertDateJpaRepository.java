package kr.hhplus.be.server.concert.infra.persistence;

import kr.hhplus.be.server.concert.domain.ConcertDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertDateJpaRepository extends JpaRepository<ConcertDate, Long> {

    List<ConcertDate> findByConcertId(Long concertId);

}
