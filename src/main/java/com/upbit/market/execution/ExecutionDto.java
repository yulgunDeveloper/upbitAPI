package com.upbit.market.execution;

import lombok.Data;

@Data
public class ExecutionDto {
    /**
     * 마켓 구분 코드
     */
    String market;

    /**
     * 체결 일자(UTC 기준)
     */
    String trade_date_utc;

    /**
     * 체결 시각(UTC 기준)
     */
    String trade_time_utc;

    /**
     * 체결 타임스탬프
     */
    Long timestamp;

    /**
     * 체결 가격
     */
    Double trade_price;

    /**
     * 체결량
     */
    Double trade_volume;

    /**
     * 전일 종가(UTC 0시 기준)
     */
    Double prev_closing_price;

    /**
     * 변화량
     */
    Double change_price;

    /**
     * 매도/매수
     */
    String ask_bid;

    /**
     * 체결 번호(Unique)
     */
    Long sequential_id;
}
