package io.github.ajurasz.remotetemp.service;

import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import io.github.ajurasz.remotetemp.models.Device;
import io.github.ajurasz.remotetemp.rest.RemoteTempApi;
import io.github.ajurasz.remotetemp.rest.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.github.ajurasz.remotetemp.service.DeviceUtil.getDeviceId;
import static io.github.ajurasz.remotetemp.service.DeviceUtil.getRegistrationId;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = RegistrationIntentService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        RemoteTempApi api = RestClient.getInstance().remoteTempApi();

        Device device = new Device();
        device.setDeviceId(getDeviceId());
        device.setRegistrationId(getRegistrationId());
        api.updateDevice(device).enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                Log.i(TAG, "Device updated");
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
