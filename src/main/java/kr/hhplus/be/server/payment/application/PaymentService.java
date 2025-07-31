package kr.hhplus.be.server.payment.application;

import kr.hhplus.be.server.payment.domain.Payment;
import kr.hhplus.be.server.payment.domain.PaymentStatus;
import kr.hhplus.be.server.payment.port.in.PaymentUseCase;
import kr.hhplus.be.server.payment.port.out.PaymentRepository;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.port.out.ReservationRepository;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentUseCase {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;


    @Override
    public void pay(Long userId, Long reservationId, Long amount) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        user.usePoint(amount);
        Reservation reservation = reservationRepository.findReservationById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        Payment payment = new Payment(null, amount, user, reservation, PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
    }
}
