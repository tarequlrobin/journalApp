package com.tarequlrobin.journalapp.cron;

import com.tarequlrobin.journalapp.scheduler.UserTaskScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserSchedulerTest {

    @Autowired
    private UserTaskScheduler userTaskScheduler;

    @Test
    public void testFetchUsersAndSendSAMail(){
        userTaskScheduler.fetchUserAndSendSAMail();
    }
}
