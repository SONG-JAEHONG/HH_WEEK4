package kr.hhplus.be.server.payment.adpter;

import kr.hhplus.be.server.payment.domain.Payment;
import kr.hhplus.be.server.payment.domain.PaymentStatus;
import kr.hhplus.be.server.payment.infra.persistence.PaymentJpaRepository;
import kr.hhplus.be.server.payment.infra.persistence.PaymentRepositoryAdapter;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PaymentRepositoryAdapterTest {

    @Mock
    private PaymentJpaRepository paymentJpaRepository;

    private PaymentRepositoryAdapter paymentRepositoryAdapter;


    @BeforeEach
    void setUp(){
        paymentRepositoryAdapter = new PaymentRepositoryAdapter(paymentJpaRepository);
    }

    @Test
     void save는_payment를_저장한다(){

        User user = new User(1L, 10000L);
        Reservation reservation = new Reservation();
        Payment payment = new Payment(
                null,
                5000L,
                user,
                reservation,
                PaymentStatus.SUCCESS
        );

        paymentRepositoryAdapter.save(payment);

        verify(paymentJpaRepository).save(payment);

    }

}
