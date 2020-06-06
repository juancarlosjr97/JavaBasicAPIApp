package com.jcbd.retrofitbasicapp;

import retrofit2.Call;
import retrofit2.http.GET;
public interface APIService {

    @GET("/Journey/JourneyResults/Becontree%20Underground%20Station/to/London%20Bridge%20Underground%20Station?nationalSearch=false&journeyPreference=LeastTime&mode=tube")

    Call<Object> getJourneyResult();


}
