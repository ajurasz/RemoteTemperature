package io.github.ajurasz.remotetemp.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static final String BASE_API_URL = "<nodejs application base url>";
    private static RestClient ourInstance = new RestClient();

    private RemoteTempApi remoteTempApi;

    public static RestClient getInstance() {
        return ourInstance;
    }

    private RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        remoteTempApi = retrofit.create(RemoteTempApi.class);
    }

    public RemoteTempApi remoteTempApi() {
        return remoteTempApi;
    }
}
