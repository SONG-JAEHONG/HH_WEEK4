package kr.hhplus.be.server.waiting.infra.web.controller;

import kr.hhplus.be.server.waiting.infra.web.dto.WaitingStatusRespone;
import kr.hhplus.be.server.waiting.port.in.WaitingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/waiting")
@RequiredArgsConstructor
public class WaitingController {

    private final WaitingUseCase waitingUseCase;

    @PostMapping
    public String enterWaiting(){
        return waitingUseCase.enterWaiting();
    }

    @GetMapping("/{userId}")
    public WaitingStatusRespone getPosition(@PathVariable("userId") String userID){
        return waitingUseCase.getStatus(userID);
    }
}
