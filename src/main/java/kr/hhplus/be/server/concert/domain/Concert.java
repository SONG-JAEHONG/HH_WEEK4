package kr.hhplus.be.server.concert.domain;


import jakarta.persistence.*;
import kr.hhplus.be.server.common.base.BaseTimeEntity;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Concert extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String title;



    public Concert(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    //concertRepositoryAdapter 테스트용
    public Concert(String testConcert) {
        super();
    }
}
