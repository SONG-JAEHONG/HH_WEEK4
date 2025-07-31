package kr.hhplus.be.server.reservation.port.out;

import kr.hhplus.be.server.reservation.domain.Reservation;

import java.util.Optional;

public interface ReservationRepository {

        Optional<Reservation> findReservationById(Long reservationId);

        void save(Reservation reservation);
}
