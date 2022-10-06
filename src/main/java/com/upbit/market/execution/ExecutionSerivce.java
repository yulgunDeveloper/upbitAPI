package com.upbit.market.execution;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.upbit.market.MarketDto;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

@Slf4j
public class ExecutionSerivce {

    /**
     * 체결 강도 계산
     * ( 체결강도 = 매수체결량/매도체결량 × 100% )
     * return String 살지(bid), 팔지(ask), 대기할지(none)
     */
    public static String investmentCal(JSONArray jsonArray) throws JSONException {
        List<ExecutionDto> executionDtoList = new ArrayList<>();
        ExecutionDto executionDto = new ExecutionDto();
        int sell = 0;
        int buy = 0;
        Double sellVolume = 0.0;
        Double buyVolume = 0.0;
        Double tradeVol = 0.0; // 100 보다 크면 매수 우위, 100 보다 작으면 매도 우위
        String result = "none"; // 살지(bid), 팔지(ask), 대기할지(none)
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject ob = (JSONObject) jsonArray.get(i);
//            executionDto.setMarket(ob.getString("market"));
//            executionDto.setTrade_date_utc(ob.getString("trade_date_utc"));
//            executionDto.setTrade_time_utc(ob.getString("trade_time_utc"));
//            executionDto.setTimestamp(ob.getLong("timestamp"));
//            executionDto.setTrade_price(ob.getDouble("trade_price"));
//            executionDto.setTrade_volume(ob.getDouble("trade_volume"));
//            executionDto.setPrev_closing_price(ob.getDouble("prev_closing_price"));
//            executionDto.setChange_price(ob.getDouble("change_price"));
//            executionDto.setAsk_bid(ob.getString("ask_bid"));
//            executionDto.setSequential_id(ob.getLong("sequential_id"));
            if (ob.getString("ask_bid").equals("ASK")) {
                sellVolume += ob.getDouble("trade_volume");
                sell++;
            } else {
                buyVolume += ob.getDouble("trade_volume");
                buy++;
            }

            executionDtoList.add(executionDto);
        }
        tradeVol = ((buyVolume / buy) / (sellVolume / sell)) * 100;
//        log.info("매도 : {}", sell);
//        log.info("매수 : {}", buy);
//        log.info("투자 여부 : {}", tradeVol);
        // 150 이상일 때 사고 50 이하일때 팔아볼까
        if (tradeVol >= 130) {
            result = "bid";
        } else if (tradeVol < 50) {
            result = "ask";
        } else {
            result = "none";
        }
        return result;
    }

    /**
     * 체결 강도 계산 및 바로 사기
     */
    public static void stockExecution(List<MarketDto> marketList, String secKey, String acKey) throws Exception {
        log.info("매매/판매 대기중...");
        List<MarketDto> executionDtoList = new ArrayList<>();
        for (int i = 0; i < marketList.size(); i++) {
            MarketDto marketDto = marketList.get(i);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.upbit.com/v1/trades/ticks?market=" + marketDto.getMarket() + "&count=50")
                    .get()
                    .addHeader("accept", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            String fastenStr = response.body().string();
            JSONArray jsonArray = new JSONArray(fastenStr);
            JSONObject ob = (JSONObject) jsonArray.get(0);
            String sellBuyNone = investmentCal(jsonArray);
            if (sellBuyNone.equals("bid")) {
                if (!marketDto.getSellBuy()) {
                    executionDtoList.add(marketDto);
                    marketDto.setPrev_trade(marketDto.getNow_trade());
                    marketDto.setNow_trade(ob.getDouble("trade_price") + ob.getDouble("trade_price") * marketDto.getBid_fee());
                    log.info("{} 구매, 현재가 : {}", marketDto.getMarket(), ob.getDouble("trade_price"));
                    marketDto.setSellBuy(true);
                }
//                orders(marketDto, secKey, acKey);
            } else if (sellBuyNone.equals("ask")) {
                if (marketDto.getSellBuy()) {
                    if (marketDto.getNow_trade() >= marketDto.getPrev_trade() + marketDto.getPrev_trade() * marketDto.getAsk_fee()) {
                        log.info("{} 판매, 현재가 : {}", marketDto.getMarket(), ob.getDouble("trade_price"));
                        marketDto.setSellBuy(false);
                    }
                }
            }
            if (i % 5 == 0) {
                Thread.sleep(500);
            }
        }
    }

    /**
     * 주문하기
     */
    public static void orders(MarketDto marketDto, String secKey, String acKey) throws Exception {
        String serverUrl = "https://api.upbit.com";

        HashMap<String, String> params = new HashMap<>();
        params.put("market", marketDto.getMarket());
        if (marketDto.getSellBuy()) {
            params.put("side", "bid");
            params.put("ord_type", "price"); // 시장가 주문
        } else {
            params.put("side", "ask");
            params.put("ord_type", "market"); // 시장가 주문
        }
        params.put("price", "100");
        params.put("volume", "0");

        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        Algorithm algorithm = Algorithm.HMAC256(secKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", acKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(serverUrl + "/v1/orders");
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);
            request.setEntity(new StringEntity(new Gson().toJson(params)));

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            JSONArray jsonArray = new JSONArray(result);
            log.info("{}", jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
