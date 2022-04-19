package com.upbit.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * upbit 민감정보를 관리하는 클래스
 */
@Slf4j
@Getter
@Component
public class UpbitConfigureProp {
    /**
     * 속성정보를 관리함
     */
    Properties prop= new Properties();

    /**
     * 생성자
     */
    public UpbitConfigureProp(){

    }

    /**
     * 정의된 속성파일로부터 데이터베이스 연결정보를 설정함
     * @param propFileName 속성파일명
     * @return 속성정보
     */
    public Properties setProperties(String propFileName) {
        InputStream  inputStream=null;
        try {
            String filePath=System.getProperty("upbit.config.path");
            if(filePath==null ){
                log.info(">>>>> resource config file load:{}", propFileName);
                inputStream  = getClass().getClassLoader().getResourceAsStream(propFileName);
            }
            else{
                String fullPath=String.format("%s/%s",filePath,propFileName);
                log.info(">>>>>> system propery config file load:{}", fullPath);
                File file = new File(fullPath);
                inputStream = new FileInputStream(file);
            }
            prop.load(inputStream);
        } catch (Exception e) {
            log.error("property load error:{}",e.getMessage());
            e.printStackTrace();
        }finally{
            try {
                if (inputStream != null) inputStream.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return prop;
    }
}
