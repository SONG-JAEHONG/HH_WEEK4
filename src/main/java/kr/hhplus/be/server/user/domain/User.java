package kr.hhplus.be.server.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.common.base.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long point;




    public void chargePoint(long amount){

        this.point += amount;
    }

    public void usePoint(long amount){
        if(this.point < amount){
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        this.point -= amount;
    }

    //UserRepositoryAdapter 테스트용
    public User(long userId, long point) {
        this.id = userId;
        this.point = point;
    }



}
