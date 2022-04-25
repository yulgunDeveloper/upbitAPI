package com.upbit.account;

import lombok.Data;

@Data
public class AccountDto {
    /**
     * 화폐
     */
    String currency;

    /**
     * 주문가능 금액/수량
     */
    Long balance;

    /**
     * 주문 중 묶여있는 금액/수량
     */
    Long locked;

    /**
     * 매수평균가
     */
    Long avg_buy_price;

    /**
     * 매수평균가 수정 여부
     */
    Boolean avg_buy_price_modified;

    /**
     * 평단가 기준 화폐
     */
    String unit_currency;
}
