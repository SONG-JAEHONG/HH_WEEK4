package kr.hhplus.be.server.reservation.adapter;


import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.infra.persistence.ReservationJpaRepository;
import kr.hhplus.be.server.reservation.infra.persistence.ReservationRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationRepositoryAdapterTest {

    @Mock
    private ReservationJpaRepository reservationJpaRepository;

    private ReservationRepositoryAdapter reservationRepositoryAdapter;

    @BeforeEach
    void setUp() {
        reservationRepositoryAdapter = new ReservationRepositoryAdapter(reservationJpaRepository);
    }

    void findReservationById는_예약정보를_가져온다(){

        Reservation reservation = new Reservation();
        when(reservationJpaRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Optional<Reservation> result = reservationRepositoryAdapter.findReservationById(1L);

        assertThat(result).isPresent().contains(reservation);
        verify(reservationJpaRepository).findById(1L);

    }

    @Test
    void save는_reservation을_저장한다() {

        Reservation reservation = new Reservation();


        reservationRepositoryAdapter.save(reservation);


        verify(reservationJpaRepository).save(reservation);
    }
}
