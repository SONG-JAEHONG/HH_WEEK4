package kr.hhplus.be.server.user.adapter;


import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.infra.persistence.UserJpaRepository;
import kr.hhplus.be.server.user.infra.persistence.UserRepositoryAdapter;
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
public class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    private UserRepositoryAdapter userRepositoryAdapter;

    @BeforeEach
    void setUp() {
        userRepositoryAdapter = new UserRepositoryAdapter(userJpaRepository);
    }


    @Test
    void findUserById는_유저의_정보를_반환한다(){

        User user = new User(1L, 5000L);
        when(userJpaRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userRepositoryAdapter.findUserById(1L);

        assertThat(result).isPresent().contains(user);
        verify(userJpaRepository).findById(1L);

    }

}
