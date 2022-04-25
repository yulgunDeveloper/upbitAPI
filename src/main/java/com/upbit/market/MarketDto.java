package com.upbit.market;

import lombok.Data;

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
}
