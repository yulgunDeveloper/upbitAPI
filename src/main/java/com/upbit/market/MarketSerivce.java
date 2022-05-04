package com.upbit.market;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MarketSerivce {

    public static List<MarketDto>  marketList() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.upbit.com/v1/market/all?isDetails=true")
                .get()
                .addHeader("Accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        String marketList = response.body().string();
        JSONArray jsonArray = new JSONArray(marketList);
        List<MarketDto> marketDtoList = new ArrayList<>();
        
        // 유의종목 및 BTC로 구매하는 코인 제외하고 리스트에 추가
        for (int i=0; i<jsonArray.length(); i++) {
            MarketDto marketDto = new MarketDto();
            marketDto.setMarket_warning(jsonArray.getJSONObject(i).getString("market_warning"));
            marketDto.setMarket(jsonArray.getJSONObject(i).getString("market"));
            marketDto.setKorean_name(jsonArray.getJSONObject(i).getString("korean_name"));
            marketDto.setEnglish_name(jsonArray.getJSONObject(i).getString("english_name"));
            if (marketDto.getMarket_warning().equals("NONE") && marketDto.getMarket().contains("KRW")) {
                marketDtoList.add(marketDto);
                log.trace("marketDto : {}", marketDto.getMarket());
                log.trace("marketDto : {}", marketDto.getMarket_warning());
                log.trace("marketDto : {}", marketDto.getKorean_name());
                log.trace("marketDto : {}", marketDto.getEnglish_name());
                log.trace("------------");
            }
        }
        log.trace("market 리스트 : {}", marketDtoList);
        log.trace("market 리스트 : {}", marketDtoList.size());
        return marketDtoList;
    }

// https://primestory.tistory.com/8
    public static void checkRSI(List<MarketDto> marketDtoList) throws Exception {
        Double sumU = 0.0;
        Double sumD = 0.0;
        for (int i = 0; i < marketDtoList.size(); i++) {
            MarketDto marketDto = tradeDayList(marketDtoList.get(i));
            sumU += marketDto.getU();
            sumD += marketDto.getD();
            log.info("getKorean_name : {}", marketDto.getKorean_name());
            log.info("getChange_price : {}", marketDto.getChange_price());
            log.info("getChange_rate : {}", marketDto.getChange_rate());
        }
    }

    // 1일 단위 종가 리스트
    public static MarketDto tradeDayList(MarketDto marketDto) throws Exception{
        String market = marketDto.getMarket();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.upbit.com/v1/candles/days?market=" + market + "&count=1")
                .get()
                .addHeader("Accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        String takeDayList = response.body().string();
        JSONArray jsonArray = new JSONArray(takeDayList);
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        // 유의종목 및 BTC로 구매하는 코인 제외하고 리스트에 추가
//            marketDto.setCandle_date_time_utc((String)jsonArray.getJSONObject(i).get("candle_date_time_utc"));
//            marketDto.setCandle_date_time_kst((String)jsonArray.getJSONObject(i).getString("candle_date_time_kst"));
//            marketDto.setOpening_price((Double) jsonArray.getJSONObject(i).get("opening_price"));
//            marketDto.setHigh_price((Double)jsonArray.getJSONObject(i).get("high_price"));
//            marketDto.setLow_price((Double)jsonArray.getJSONObject(i).get("low_price"));
            marketDto.setTrade_price(jsonObject.getDouble("trade_price"));
//            marketDto.setTimestamp((Long)jsonArray.getJSONObject(i).get("timestamp"));
//            marketDto.setCandle_acc_trade_price((Double)jsonArray.getJSONObject(i).get("candle_acc_trade_price"));
//            marketDto.setCandle_acc_trade_volume((Double)jsonArray.getJSONObject(i).get("candle_acc_trade_volume"));
//            marketDto.setUnit((Integer) jsonArray.getJSONObject(i).get("unit"));
            marketDto.setPrev_closing_price(jsonObject.getDouble("prev_closing_price"));
            if (jsonObject.getString("change_price").equals("null")) {
                marketDto.setChange_price(0.0);
            } else {
                marketDto.setChange_price(jsonObject.getDouble("change_price"));
            }

            if (jsonObject.getString("change_rate").equals("null")) {
                marketDto.setChange_rate(0.0);
            } else {
                marketDto.setChange_rate(jsonObject.getDouble("change_rate"));
            }
        Double diff = marketDto.getTrade_price() - marketDto.getChange_price();
        Double U = diff;
        Double D = diff;
        if (diff < 0) U = 0.0;
        if (diff > 0) D = 0.0;
        marketDto.setU(U);
        marketDto.setD(D);
        return marketDto;
    }
}
