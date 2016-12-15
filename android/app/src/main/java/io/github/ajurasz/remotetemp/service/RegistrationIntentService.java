package io.github.ajurasz.remotetemp.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import io.github.ajurasz.remotetemp.models.Device;
import io.github.ajurasz.remotetemp.rest.RemoteTempApi;
import io.github.ajurasz.remotetemp.rest.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.github.ajurasz.remotetemp.service.DeviceUtil.getDeviceId;
import static io.github.ajurasz.remotetemp.service.DeviceUtil.getDeviceName;
import static io.github.ajurasz.remotetemp.service.DeviceUtil.getRegistrationId;

public class RegistrationIntentService extends IntentService {
    private static final String TAG = RegistrationIntentService.class.getSimpleName();

    public RegistrationIntentService() {
        super("RegistrationIntentService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Device device = new Device();
        device.setDeviceId(getDeviceId());
        device.setDeviceName(getDeviceName());
        device.setRegistrationId(getRegistrationId());

        RemoteTempApi api = RestClient.getInstance().remoteTempApi();
        api.createDevice(device).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(TAG, "Device created");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
