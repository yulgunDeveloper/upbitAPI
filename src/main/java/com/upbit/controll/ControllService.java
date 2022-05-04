package com.upbit.controll;

import com.upbit.account.AccountService;
import com.upbit.config.UpbitConfigureProp;
import com.upbit.market.MarketDto;
import com.upbit.market.MarketSerivce;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Properties;

@Slf4j
public class ControllService implements ControllServiceInterface {

    private final UpbitConfigureProp upbitConfigureProp;
    private static Properties prop;
    private static List<MarketDto> marketList; //market list

    MarketSerivce marketSerivce = new MarketSerivce();

    /**
     * upbit 연결 정보를 받아오는 생성자
     *
     * @param upbitConfigureProp property를 가져오는 객체
     */
    public ControllService(UpbitConfigureProp upbitConfigureProp) {
        this.upbitConfigureProp = upbitConfigureProp;
        prop = upbitConfigureProp.setProperties("config.properties");
    }

    public static String getAccessKey() {
        return prop.getProperty("success.access.key");
    }

    public static String getSecretKey() {
        return prop.getProperty("success.secret.key");
    }

    public int init() {
        log.info("내 정보 가져오는 중...");
        String accessKey = getAccessKey();
        String secretKey = getSecretKey();
        log.debug("accessKey, secretKey 확인 중...");
        do {
            try {
                if (AccountService.getAccounts(accessKey, secretKey) <= 0) {
                    Thread.sleep(5000);
                    log.warn("accessKey, secretKey 없음. 다시 확인해 주세요...");
                    log.warn("프로그램을 종료 후 재시작해 주세요...");
                    return -1;
                }
                break;
            } catch (Exception e) {
                log.warn("accessKey, secretKey error : {}", e.getMessage());
                e.printStackTrace();
            }
        } while (true);
        return 1;
    }

    public void start() {
        log.info("자동 매매 프로그램 실행...");
        try {
            log.info("리스트 받아오는 중...");
            marketList = marketSerivce.marketList();
            marketSerivce.checkRSI(marketList);
            log.info("대기 중...");
//            do {
//                marketList = MarketSerivce.marketList();
//                log.info("실행중...");
//                Thread.sleep(2000);
//            } while (true);
        } catch (Exception e) {
            log.warn("MarketList error : {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
