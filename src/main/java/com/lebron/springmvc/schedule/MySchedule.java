package com.lebron.springmvc.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MySchedule {

    @Scheduled(initialDelay = 5000, fixedDelayString = "${time}")
    public void myTask() {

    }

    @Scheduled(initialDelay = 5000, fixedDelay = 500)
    public void myTask2() {

    }
}
