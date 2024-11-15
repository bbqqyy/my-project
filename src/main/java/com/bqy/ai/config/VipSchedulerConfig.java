package com.bqy.ai.config;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


@Configuration
@Data
public class VipSchedulerConfig {

    @Bean
    public Scheduler vipScheduler() {

        ThreadFactory factory = new ThreadFactory() {

            private final AtomicInteger threadNum = new AtomicInteger(1);

            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread thread = new Thread(r, "VIPThreadPool-" + threadNum.getAndIncrement());
                thread.setDaemon(false);
                return thread;

            }
        };
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10, factory );
        return  Schedulers.from(executorService);
    }


}
