package com.upbit.controll;

import com.upbit.account.AccountService;
import com.upbit.config.UpbitConfigureProp;
import com.upbit.market.MarketDto;
import com.upbit.market.MarketSerivce;
import com.upbit.market.execution.ExecutionSerivce;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Properties;

@Slf4j
public class ControllService implements ControllServiceInterface {

    private final UpbitConfigureProp upbitConfigureProp;
    private static Properties prop;
    private static List<MarketDto> marketList; //market list
    private static Double myMoney = 1000000.0;

    MarketSerivce marketSerivce = new MarketSerivce();
    ExecutionSerivce executionSerivce = new ExecutionSerivce();
    AccountService accountService = new AccountService();

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
                if (accountService.getAccounts(accessKey, secretKey) <= 0) {
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
        log.debug("자동 매매 프로그램 실행...");
        try {
            log.info("리스트 받아오는 중...");
            marketList = marketSerivce.marketList();
//             market 리스트 확인하기
//            for (int i = 0; i < marketList.size(); i++) {
//                log.info("int {}", marketList.get(i).getMarket() + "Cnt = 0;");
//            }
//            marketSerivce.checkRSI(marketList);
            marketList = marketSerivce.nowValueInf(marketList); // 거래량 많은 목록만 가져오기
            marketList = marketSerivce.sellBuyFee(marketList); // 매수 매도 수수료 집어넣기
            log.debug("총 {}개 상대로 매매 시작...", marketList.size());

            do {
                log.info("매매/판매 대기중...");
                // 맨처음에 이미 내 계좌에 있던것 marketDto.setSellBuy(true); 랑 buycount Set 해놓기
                // 구매할때 조건 더 넣어야함
                myMoney = executionSerivce.stockExecution(marketList, myMoney);

                Thread.sleep(500);
            } while (true);
        } catch (Exception e) {
            log.warn("MarketList error : {}", e.getMessage());
            e.printStackTrace();
        }
    }
    
    /** 
     * 비트코인과 이더리움만 가지고 실행
     * */
    public void startWithOnlyTwo() {
        log.debug("자동 매매 프로그램 실행...");
        try {
            log.info("리스트 받아오는 중...");
            marketList = marketSerivce.marketListOnlyTwo();
            marketList = marketSerivce.sellBuyFee(marketList); // 매수 매도 수수료 집어넣기
            log.debug("총 {}개 상대로 매매 시작...", marketList.size());

            do {
                log.info("매매/판매 대기중...");
                // 맨처음에 이미 내 계좌에 있던것 marketDto.setSellBuy(true); 랑 buycount Set 해놓기
                // 구매할때 조건 더 넣어야함
                myMoney = executionSerivce.stockExecution(marketList, myMoney);

                Thread.sleep(500);
            } while (true);
        } catch (Exception e) {
            log.warn("MarketList error : {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
