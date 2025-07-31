package kr.hhplus.be.server.reservation.adapter;

import kr.hhplus.be.server.concert.domain.*;
import kr.hhplus.be.server.reservation.domain.ReservationStatus;
import kr.hhplus.be.server.reservation.infra.persistence.ReservationJpaRepository;
import kr.hhplus.be.server.concert.infra.persistence.*;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.infra.persistence.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReservationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertDateJpaRepository concertDateJpaRepository;

    @Autowired
    private SeatJpaRepository seatJpaRepository;

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Container
    public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    private Long savedUserId;
    private Long savedConcertDateId;
    private Long savedSeatId;

    @BeforeEach
    void setUp() {
        reservationJpaRepository.deleteAll();
        seatJpaRepository.deleteAll();
        concertDateJpaRepository.deleteAll();
        concertJpaRepository.deleteAll();
        userJpaRepository.deleteAll();

        User user = new User(null, 10000L);
        user = userJpaRepository.save(user);
        savedUserId = user.getId();

        Concert concert = new Concert(null, "YEYE");
        concert = concertJpaRepository.save(concert);

        ConcertDate concertDate = new ConcertDate(null, concert, LocalDate.of(2025, 8, 1));
        concertDate = concertDateJpaRepository.save(concertDate);
        savedConcertDateId = concertDate.getId();

        Seat seat = new Seat(concertDate, 1, SeatStatus.AVAILABLE, null);
        seat = seatJpaRepository.save(seat);
        savedSeatId = seat.getId();
    }

    @Test
    void 예약_생성_API_성공() throws Exception {
        String requestBody = String.format("""
            {
                "userId": %d,
                "concertDateId": %d,
                "seatId": %d
            }
            """, savedUserId, savedConcertDateId, savedSeatId);

        mockMvc.perform(post("/reservation")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk());

        var reservations = reservationJpaRepository.findAll();
        assertThat(reservations).hasSize(1);

        var reservation = reservations.get(0);
        assertThat(reservation.getUser().getId()).isEqualTo(savedUserId);
        assertThat(reservation.getConcertDate().getId()).isEqualTo(savedConcertDateId);
        assertThat(reservation.getSeat().getId()).isEqualTo(savedSeatId);
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.HOLDING);
    }
}
