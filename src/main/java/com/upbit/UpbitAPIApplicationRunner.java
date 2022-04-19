package com.upbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 시리얼 통신에서 메세지를 송수신 하기 위한 어플리케이션을 실행하는 부분임
 */
@Slf4j
@Component
public class UpbitAPIApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("run yulgunDev's upbitAutoApllication................");
    }
}
