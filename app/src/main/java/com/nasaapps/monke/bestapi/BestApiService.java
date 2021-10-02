package com.nasaapps.monke.bestapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import com.nasaapps.monke.modelo.LocacionRespuesta;

public interface BestApiService {
    @POST("venues/search")
    Call<LocacionRespuesta> obtenerData(@Query("api_key_private") String api,@Query("q") String query);
}
