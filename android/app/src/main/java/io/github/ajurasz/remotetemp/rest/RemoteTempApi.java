package io.github.ajurasz.remotetemp.rest;

import io.github.ajurasz.remotetemp.models.Device;
import io.github.ajurasz.remotetemp.models.Temperature;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RemoteTempApi {

    @POST("temperature")
    Call<Temperature> temperature();

    @POST("device")
    Call<Void> createDevice(@Body Device device);

    @PUT("device")
    Call<Device> updateDevice(@Body Device device);

    @DELETE("device/{deviceId}")
    Call<Void> deleteDevice(@Path("deviceId") String deviceId);
}
