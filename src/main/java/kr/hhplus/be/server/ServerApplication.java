package kr.hhplus.be.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
		"kr.hhplus.be.server.concert.infra.persistence",
		"kr.hhplus.be.server.payment.infra.persistence",
		"kr.hhplus.be.server.reservation.infra.persistence",
		"kr.hhplus.be.server.user.infra.persistence",
})
@EntityScan(basePackages = "kr.hhplus.be.server")
@EnableScheduling
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
