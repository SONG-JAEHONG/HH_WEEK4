package kr.hhplus.be.server.user.infra.web.contrroller;


import kr.hhplus.be.server.user.infra.web.dto.PointRequest;
import kr.hhplus.be.server.user.port.in.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @PostMapping("/{userId}/charge")
    public void charge(
            @PathVariable("userId") Long userId,
            @RequestBody PointRequest request
    ){
        userUseCase.chargePoint(userId, request.amount());
    }


    @PostMapping("/{userId}/use")
    public ResponseEntity<Void> usePoint(
            @PathVariable("userId") Long userId,
            @RequestBody PointRequest request
    ) {
        userUseCase.usePoint(userId, request.amount());
        return ResponseEntity.ok().build();
    }


}
