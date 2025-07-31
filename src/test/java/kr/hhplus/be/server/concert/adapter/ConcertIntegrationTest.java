package kr.hhplus.be.server.concert.adapter;

import kr.hhplus.be.server.concert.domain.Concert;
import kr.hhplus.be.server.concert.domain.ConcertDate;
import kr.hhplus.be.server.concert.domain.Seat;
import kr.hhplus.be.server.concert.domain.SeatStatus;
import kr.hhplus.be.server.concert.infra.persistence.ConcertDateJpaRepository;
import kr.hhplus.be.server.concert.infra.persistence.ConcertJpaRepository;
import kr.hhplus.be.server.concert.infra.persistence.SeatJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ConcertIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ConcertDateJpaRepository concertDateJpaRepository;

    @Autowired
    private SeatJpaRepository seatJpaRepository;

    private Long savedConcertId;
    private Long savedConcertDateId;


    @Container
    public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.33")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");


    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @BeforeEach
    void setUp() {
        seatJpaRepository.deleteAll();
        concertDateJpaRepository.deleteAll();
        concertJpaRepository.deleteAll();

        // 콘서트 저장
        Concert concert = concertJpaRepository.save(new Concert(null, "YEYE"));
        savedConcertId = concert.getId();

        // 콘서트 날짜 저장
        ConcertDate concertDate1 = concertDateJpaRepository.save(new ConcertDate(null, concert, LocalDate.of(2025, 7, 23)));
        ConcertDate concertDate2 = concertDateJpaRepository.save(new ConcertDate(null, concert, LocalDate.of(2025, 7, 24)));
        savedConcertDateId = concertDate1.getId();

        // 좌석 저장 (concertDate1에 5개 좌석 생성)
        seatJpaRepository.saveAll(List.of(
                new Seat(concertDate1, 1, SeatStatus.AVAILABLE, null),
                new Seat(concertDate1, 2, SeatStatus.AVAILABLE, null),
                new Seat(concertDate1, 3, SeatStatus.AVAILABLE, null),
                new Seat(concertDate1, 4, SeatStatus.AVAILABLE, null),
                new Seat(concertDate1, 5, SeatStatus.AVAILABLE, null)
        ));
    }

    @Test
    void 공연_목록_조회_성공() throws Exception {
        mockMvc.perform(get("/concerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").exists());
    }

    @Test
    void 콘서트_날짜_목록_조회_성공() throws Exception {
        mockMvc.perform(get("/concerts/{concertId}/dates", savedConcertId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].concertDate").exists());
    }

    @Test
    void 콘서트_좌석_목록_조회_성공() throws Exception {
        mockMvc.perform(get("/concerts/{concertDateId}/seats", savedConcertDateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].seatNumber").exists());
    }

}

