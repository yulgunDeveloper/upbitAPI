package com.upbit;

import com.upbit.controll.ControllService;
import com.upbit.config.UpbitConfigureProp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Upbit 데이터를 송수신하기 위한 어플리케이션
 */
@Slf4j
@Component
public class UpbitAPIApplicationRunner implements ApplicationRunner {
    /**
     * property들을 가져오는 객체
     */
    final private UpbitConfigureProp upbitConfigureProp;

    /**
     * 생성자
     * @param upbitConfigureProp DB에 연결할 property들을 가져오는 객체
     */
    public UpbitAPIApplicationRunner(UpbitConfigureProp upbitConfigureProp) {
        this.upbitConfigureProp = upbitConfigureProp;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("run yulgunDev's upbitAutoApllication................");
        ControllService jwtService = new ControllService(upbitConfigureProp);
        if ( jwtService.init() < 0) return;
        jwtService.start();
    }
}
