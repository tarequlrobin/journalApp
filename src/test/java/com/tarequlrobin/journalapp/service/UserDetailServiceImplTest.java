package com.tarequlrobin.journalapp.service;

import com.tarequlrobin.journalapp.entity.User;
import com.tarequlrobin.journalapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.mockito.Mockito.*;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

public class UserDetailServiceImplTest {

    @InjectMocks
    private UserDetailServiceImpl userDetailServiceImpl;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void loadUserByUsernameTest() {
        when(userRepository.findByUserName(ArgumentMatchers.anyString()))
                .thenReturn(User.builder().userName("Robin").password("dnfjsdfb")
                        .roles(new ArrayList<>()).build());
        UserDetails user = userDetailServiceImpl.loadUserByUsername("Robin");
        Assertions.assertNotNull(user);
    }
}
