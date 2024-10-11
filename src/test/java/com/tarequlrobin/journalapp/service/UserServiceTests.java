package com.tarequlrobin.journalapp.service;

import com.tarequlrobin.journalapp.entity.User;
import com.tarequlrobin.journalapp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Disabled
    @Test
    public void testFindByUserName(){
        User user = userRepository.findByUserName("Fateen");
        assertTrue(!user.getJournalEntries().isEmpty());
        assertNotNull(userRepository.findByUserName("Fateen"));
        //assertEquals(4, 2+1);
    }

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "3,5,8",
            "5,7,12",
            "1,8,10"
    })
    public void test(int a, int b, int expected){
        assertEquals(expected, a+b);
    }

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {
            "Fateen",
            "Trisha",
            "Robin",
            "ghgfty"
    })
    public void findByUserNameTest(String name){
        assertNotNull(userRepository.findByUserName(name));
    }

    @Disabled
    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    public void createUserNameTest(User user){
        assertTrue(userService.saveNewUser(user));
    }
}
