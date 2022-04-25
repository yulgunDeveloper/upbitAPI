package com.upbit.account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class AccountService {
    public static List<AccountDto> jsonArrayToList(JSONArray jsonArray) throws JSONException {
        List<AccountDto> accountList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject ob = (JSONObject) jsonArray.get(i);
            AccountDto dto = new AccountDto();
            dto.setCurrency(ob.getString("currency"));
            dto.setBalance(ob.getLong("balance"));
            dto.setAvg_buy_price(ob.getLong("avg_buy_price"));
            dto.setAvg_buy_price_modified(ob.getBoolean("avg_buy_price_modified"));
            dto.setUnit_currency(ob.getString("unit_currency"));
            accountList.add(dto);
        }
        return accountList;
    }

    public static int getAccounts(String accessKey, String secretKey) throws Exception {
        String serverUrl = "https://api.upbit.com";
        if (accessKey == null || secretKey == null || accessKey == "" || secretKey == "") {
            return -1;
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(serverUrl + "/v1/accounts");
        request.setHeader("Content-Type", "application/json");
        request.addHeader("Authorization", authenticationToken);

        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        JSONArray jsonArray = new JSONArray(result);
//            List<AccountDto> accountList = accountService.jsonArrayToList(jsonArray);
//            model.addAttribute("accountList", accountList);
        log.info("accessKey, secretKey 확인 완료...");
        return 1;
    }
}