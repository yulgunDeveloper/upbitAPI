package com.upbit.auth;

import com.upbit.config.UpbitConfigureProp;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Properties;

@Slf4j
public class JwtService {

    private final UpbitConfigureProp upbitConfigureProp;
    private static Properties prop;

    /**
     * upbit 연결 정보를 받아오는 생성자
     * @param upbitConfigureProp property를 가져오는 객체
     */
    public JwtService(UpbitConfigureProp upbitConfigureProp) {
        this.upbitConfigureProp = upbitConfigureProp;
        prop = upbitConfigureProp.setProperties("application.properties");
    }

    public static String getAccessKey(){
        return prop.getProperty("success.access.key");
    }
    public static String getSecretKey(){
        return prop.getProperty("success.secret.key");
    }
}
