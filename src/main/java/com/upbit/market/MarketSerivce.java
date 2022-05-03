package com.upbit.market;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.configurationprocessor.json.JSONArray;

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

        //출력
        String marketList = response.body().string();
        JSONArray jsonArray = new JSONArray(marketList);
        List<MarketDto> marketDtoList = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++) {
            MarketDto marketDto = new MarketDto();
            marketDto.setMarket_warning((String)jsonArray.getJSONObject(i).get("market_warning"));
            marketDto.setMarket((String)jsonArray.getJSONObject(i).get("market"));
            marketDto.setKorean_name((String)jsonArray.getJSONObject(i).get("korean_name"));
            marketDto.setEnglish_name((String)jsonArray.getJSONObject(i).get("english_name"));
            marketDtoList.add(marketDto);
        }
        log.trace("market 리스트 : {}", jsonArray);
        log.trace("market 리스트 : {}", jsonArray.length());
        return marketDtoList;
    }

//    public static List<MarketDto> ()
}
