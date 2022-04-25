package com.upbit.market;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.configurationprocessor.json.JSONArray;

@Slf4j
public class MarketSerivce {

    public static void marketList() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.upbit.com/v1/market/all?isDetails=false")
                .get()
                .addHeader("Accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();
//            List<AccountDto> accountList = accountService.jsonArrayToList(jsonArray);
//            model.addAttribute("accountList", accountList);
    }
}
