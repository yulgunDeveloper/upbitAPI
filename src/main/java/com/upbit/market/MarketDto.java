package com.upbit.market;

import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONArray;

@Data
public class MarketDto {
    /**
     * 업비트에서 제공중인 시장 정보
     */
    String market;

    /**
     * 거래 대상 암호화폐 한글명
     */
    String korean_name;

    /**
     * 거래 대상 암호화폐 영문명
     */
    String english_name;

    /**
     * 유의 종목 여부
     * NONE (해당 사항 없음), CAUTION(투자유의)
     */
    String market_warning;

//    /**
//     * 캔들 기준 시각(UTC 기준)
//     */
//    String candle_date_time_utc;

    /**
     * 캔들 기준 시각(KST 기준)
     */
    String candle_date_time_kst;

//    /**
//     * 시가
//     */
//    Double opening_price;
//
//    /**
//     * 고가
//     */
//    Double high_price;
//
//    /**
//     * 저가
//     */
//    Double low_price;

    /**
     * 종가
     */
    Double trade_price;

    /**
     * 해당 캔들에서 마지막 틱이 저장된 시각
     */
    Long timestamp;

//    /**
//     * 누적 거래 금액
//     */
//    Double candle_acc_trade_price;
//
//    /**
//     * 누적 거래량
//     */
//    Double candle_acc_trade_volume;
//
//    /**
//     * 분 단위(유닛)
//     */
//    Integer unit;

    /**
     * 전일 종가(UTC 0시 기준)
     */
    Double prev_closing_price;

    /**
     * 전일 종가 대비 변화 금액
     */
    Double change_price = 0.0;

    /**
     * 전일 종가 대비 변화량
     */
    Double change_rate = 0.0;

    /**
     * minus 인지 아닌지
     */
    Boolean minusTF;

    /**
     * U : 음수를 0으로
     */
    Double U = 0.0;

    /**
     * D : 양수를 0으로
     */
    Double D = 0.0;

    /**
     * 호가 매도 총 잔량
     */
    Double total_ask_size = 0.0;

    /**
     * 호가 매수수 총 량
     */
    Double total_bid_size = 0.0;

    /**
     * 호가
     */
    JSONArray orderbook_units = new JSONArray();
//
//    /**
//     * 매도호가
//     */
//    Double ask_price = 0.0;
//
//    /**
//     * 매수호가
//     */
//    Double bid_price = 0.0;
//
//    /**
//     * 매도 잔량
//     */
//    Double ask_size = 0.0;
//
//    /**
//     * 매수 잔량
//     */
//    Double bid_size = 0.0;
}
