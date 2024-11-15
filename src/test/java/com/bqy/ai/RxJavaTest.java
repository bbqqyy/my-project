package com.bqy.ai;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RxJavaTest {
    @Test
    public void test() throws InterruptedException{
        Flowable<Long> flowable = Flowable.interval(1, TimeUnit.SECONDS)
                .map(i->i+1)
                .subscribeOn(Schedulers.io());
        //订阅Flowable流，并打印每个接受的数字
        flowable.observeOn(Schedulers.io())
                .doOnNext(item -> System.out.println(item.toString()))
                .subscribe();
        //主线程睡眠以便观察结果
        Thread.sleep(10000);
    }
}
