package com.rezz.accountservice.service;

import com.rezz.accountservice.entity.User;
import com.rezz.accountservice.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;
    @Mock
    private UserRepository repo;
    private User user;

    @BeforeAll
    public static void blockHoundSetup(){
        BlockHound.install();
    }

    @BeforeEach
    public void init() {
        user = new User(1, "John", "Doe", "123456789");
    }

    @Test
    public void verifyBlockHoundWorks(){
        try{
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("This should of failed");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @Test
    public void findUserById_userDoesntExist() {
        when(repo.findById(1)).thenReturn(Mono.empty());
        StepVerifier.create(service.findUserById(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    public void findUserById_userExists() {
        when(repo.findById(1)).thenReturn(Mono.just(user));
        StepVerifier.create(service.findUserById(1))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    public void failToCreateUserWhenSsnExists() {
        User mockUser = new User(2, "John", "Deer", "123456789");
        when(repo.findBySsn(anyString())).thenReturn(Mono.just(mockUser));

        StepVerifier.create(service.createUser(user))
                .expectSubscription()
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    public void successfullyCreateUser() {
        when(repo.findBySsn(anyString())).thenReturn(Mono.empty());
        when(repo.save(any())).thenReturn(Mono.just(user));

        StepVerifier.create(service.createUser(user))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();

    }
}