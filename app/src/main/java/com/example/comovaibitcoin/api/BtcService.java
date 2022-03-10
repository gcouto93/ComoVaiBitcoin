package com.example.comovaibitcoin.api;

import com.example.comovaibitcoin.model.TickerItem;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BtcService {

    @GET("BTC/ticker/")
    Call<TickerItem> recuperarBtc();

//    @GET("/prices/spot?currency=BRL")
//    Call<BTCReal> recuperarBtcReal();

}
