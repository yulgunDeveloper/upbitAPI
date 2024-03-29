package com.upbit.market;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.upbit.account.AccountDto;
import com.upbit.controll.ControllService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

@Slf4j
public class MarketSerivce {

    static ControllService controllService;

    public static List<MarketDto> jsonArrayToList(JSONArray jsonArray,MarketDto marketDto) throws JSONException {
        List<MarketDto> fastenStrengthList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject ob = (JSONObject) jsonArray.get(i);
            marketDto.setMarket(ob.getString("market"));
            marketDto.setTimestamp(ob.getLong("trade_date_utc"));
            marketDto.setTimestamp(ob.getLong("trade_time_utc"));
            marketDto.setTimestamp(ob.getLong("timestamp"));
            fastenStrengthList.add(marketDto);
        }
        return fastenStrengthList;
    }

    /**
     * 24시간 누적 거래량 계산하기
     */
    public static MarketDto hourOfTradingVol(JSONArray jsonArray, MarketDto marketDto) throws JSONException {
        Double volume = 0.0;
        Double highP = 0.0;
        Double lowP = 0.0;
        Double nowP = 0.0;
        String volumestr = "";
        String highPstr = "";
        String lowPstr = "";
        String nowPstr = "";
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject ob = (JSONObject) jsonArray.get(i);
//            marketDto.setTimestamp(ob.getLong("trade_date_utc"));
//            marketDto.setTimestamp(ob.getLong("trade_time_utc"));
//            marketDto.setTimestamp(ob.getLong("timestamp"));
            volume = ob.getDouble("acc_trade_volume_24h");
            highP = ob.getDouble("highest_52_week_price"); // 52주 신고가
            lowP = ob.getDouble("lowest_52_week_price"); // 52주 신저가
            nowP = ob.getDouble("trade_price"); // 현재가
//            volume = doubleMinusE(volume);
//            highP = doubleMinusE(highP);
//            lowP = doubleMinusE(lowP);
//            nowP = doubleMinusE(nowP);
//            volumestr = doubleMinusEstr(volume);
//            highPstr = doubleMinusEstr(highP);
//            lowPstr = doubleMinusEstr(lowP);
//            nowPstr = doubleMinusEstr(nowP);
        }
        marketDto.setAcc_trade_volume_24h(volume); // 24시간 누적 거래량(09시 기준)
        marketDto.setHighest_52_week_price(highP); // 52주 신고가
        marketDto.setLowest_52_week_price(lowP); // 52주 신저가
        marketDto.setTrade_price(nowP); // 현재가
//        log.info("{}  24시간 누적거래량:{}  52주 신고가 : {}, 52주 신저가 : {}, 현재가 : {}",marketDto.getMarket(), volumestr, highPstr, lowPstr, nowPstr);
        return marketDto;
    }

//    public static Double doubleMinusE(Double doubleNum) {
//        String doubleStr = String.valueOf(doubleNum);
//        BigDecimal bigDecimal = new BigDecimal(doubleNum);
//        if (doubleStr.contains("E")) {
//            doubleStr = bigDecimal.toString();
//        }
//        return doubleStr;
//    }
//    public static String doubleMinusEstr(Double doubleNum) {
//        String doubleStr = String.valueOf(doubleNum);
//        BigDecimal bigDecimal = new BigDecimal(doubleNum);
//        if (doubleStr.contains("E")) {
//            doubleStr = bigDecimal.toString();
//        }
//        return doubleStr;
//    }

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

    public static List<MarketDto>  marketListOnlyTwo() throws Exception {
        List<MarketDto> marketDtoList = new ArrayList<>();

        // 비트코인과 이더리움만 리스트에 추가
        MarketDto marketDto = new MarketDto();
        marketDto.setMarket("KRW-BTC");
        marketDto.setMarket_warning(("NONE"));
        marketDto.setKorean_name("비트코인");
        marketDto.setEnglish_name("Bitcoin");
        marketDtoList.add(marketDto);

        marketDto = new MarketDto();
        marketDto.setMarket("KRW-ETH");
        marketDto.setMarket_warning(("NONE"));
        marketDto.setKorean_name("이더리움");
        marketDto.setEnglish_name("Ethereum");
        marketDtoList.add(marketDto);


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
            // 생각해보니깐.. tradeDayList에서 JsonArray로 받아와야지.. 14일치로 그리고 거기서 계산해야지퓨ㅠㅠ
            log.trace("getKorean_name : {}", marketDto.getKorean_name());
            log.trace("getChange_price : {}", marketDto.getChange_price());
            log.trace("getChange_rate : {}", marketDto.getChange_rate());
            System.out.println("test");
        }
        sumU /= marketDtoList.size();
        sumD *= -1;
        sumD /= marketDtoList.size();
        log.info("sumU : {}", sumU);
        log.info("sumD : {}", sumD);
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

    // 현재가 정보
    public static List<MarketDto> nowValueInf(List<MarketDto> marketList) throws Exception {
        List<MarketDto> volMarketList = new ArrayList<>(); // 24시간 누적 거래량이 5000 넘어가는 종목만 선택
        for (int i = 0; i < marketList.size(); i++) {
            MarketDto marketDto = marketList.get(i);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.upbit.com/v1/ticker?markets=" + marketDto.getMarket())
                    .get()
                    .addHeader("accept", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            String fastenStr = response.body().string();
            JSONArray jsonArray = new JSONArray(fastenStr);
            marketDto = hourOfTradingVol(jsonArray, marketDto);
            if (marketDto.getAcc_trade_volume_24h() >= 10000.0 && marketDto.getHighest_52_week_price() >= 1000) {
                volMarketList.add(marketDto);
                log.trace("{}  24시간 누적거래량:{}  52주 신고가 : {}, 52주 신저가 : {}, 현재가 : {}",marketDto.getMarket(), marketDto.getAcc_trade_volume_24h(), marketDto.getHighest_52_week_price(), marketDto.getLowest_52_week_price(), marketDto.getTrade_price());
            }
            if (i % 7 == 0) {
                Thread.sleep(500);
            }
        }
        return volMarketList;
    }

    // 호가(Orderbook) 응답
    public static MarketDto oderBook(MarketDto marketDto) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.upbit.com/v1/orderbook?markets=" + marketDto.getMarket())
                .get()
                .addHeader("accept", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String oderBookStr = response.body().string();
        JSONArray jsonArray = new JSONArray(oderBookStr);
        return marketDto;// 체결 매도, 매수 호가 가져오기
    }

    /**
     * 계산을 위한 매수/매도 수수료 비율
     */
    public static List<MarketDto> sellBuyFee(List<MarketDto> marketList) throws Exception {
        String acKey = controllService.getAccessKey();
        String secKey = controllService.getSecretKey();
        List<MarketDto> sellBuyFeeList = new ArrayList<>();
        for (int i = 0; i < marketList.size(); i++) {
            MarketDto marketDto = marketList.get(i);
            HashMap<String, String> params = new HashMap<>();
            params.put("market", marketDto.getMarket());

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
                HttpGet request = new HttpGet("https://api.upbit.com/v1/orders/chance?" + queryString);
                request.setHeader("Content-Type", "application/json");
                request.addHeader("Authorization", authenticationToken);

                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                JSONObject ob = new JSONObject(result);
                marketDto.setBid_fee(Double.valueOf(ob.getString("bid_fee")));
                marketDto.setAsk_fee(Double.valueOf(ob.getString("ask_fee")));
                sellBuyFeeList.add(marketDto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (i % 5 == 0) {
                Thread.sleep(500);
            }
        }

        return sellBuyFeeList;
    }
}
