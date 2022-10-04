package com.upbit.market;

import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONArray;

@Data
public class FastenStrengthDto {
    /**
     * 매도호가
     */
    Double ask_price = 0.0;

    /**
     * 매수호가
     */
    Double bid_price = 0.0;

    /**
     * 매도 잔량
     */
    Double ask_size = 0.0;

    /**
     * 매수 잔량
     */
    Double bid_size = 0.0;
}
